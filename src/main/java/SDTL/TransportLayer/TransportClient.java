/*
 * Copyright (C) 2018 , Stefano Speretta
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package SDTL.TransportLayer;

import SDTL.Protocol.AckFrame;
import SDTL.Protocol.DownlinkFrame;
import SDTL.Protocol.TransportFrame;
import SDTL.StreamOperations.SDTLInputStream;
import SDTL.StreamOperations.SDTLOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Stefano Speretta <s.speretta@tudelft.nl>
 */
public class TransportClient extends Thread
{
    private final TransportBuffer tb;
    private final SDTLClientConnector tc;
    private final ScheduledExecutorService scheduler;
    private final int pollrate;
    private static boolean periodicTaskRunning = false;
    private final List<Integer> ackList = new LinkedList<>();
    private final Semaphore semaphore = new Semaphore(0);
    
    public TransportClient(String db, SDTLClientConnector connector, int updateRate) throws TransportException
    {
        pollrate = updateRate;
        tb = new TransportBuffer(db);
        tc = connector;
        
        scheduler = Executors.newScheduledThreadPool(1);
    }
    
    @Override
    public void start()
    {
        super.start();
        scheduler.scheduleAtFixedRate(new Runnable()
        {
            @Override
            public void run() 
            {
                semaphore.release();
            }
        }, 0, pollrate, TimeUnit.SECONDS);
    }
    
    public void clearRepository() throws TransportException
    {
        tb.cleanDB();
    }
    
    public int getQueueSize() throws TransportException
    {
        return tb.getCount();
    }
    
    private void send(TransportFrame frame) throws TransportException
    {        
        tb.insertFrame(frame);
        semaphore.release();
    }
    
    public void submitDownlinkFrame(DownlinkFrame f) throws TransportException
    {
        try 
        {
            TransportFrame t = TransportFrame.newBuilder()
                .setID(TransportID.SUBMITDOWNLINKFRAME)
                .setPayload(f.toByteBuffer())
                .build();
            send(t);  
            ackList.add(t.hashCode());
        } catch (IOException ex) 
        {
            throw new TransportException(ex);
        }
    }

    private void connect() throws IOException
    {
        tc.connect();
        
        // send the schema
    }
    
    @Override
    public void run()
    {        
        periodicTaskRunning = true;
        while(periodicTaskRunning)
        {            
            try 
            {
                semaphore.acquire();
                semaphore.drainPermits();

                // if not connected, try to connect
                if (!tc.connected() && (tb.getCount() != 0))
                {
                    connect();
                }

                // if connect was succesfull, transfer the frames
                if (tc.connected())
                {
                    MessageReceiver mr = new MessageReceiver(tc.getInputStream());
                    mr.start();
                    SDTLOutputStream os = new SDTLOutputStream(tc.getOutputStream());

                    TransportFrame f = tb.getNextFrame();
                    while ((f != null) && tc.connected())
                    {
                        os.write(f);
                        os.flush();
                        f = tb.getNextFrame();
                    }

                    // wait for the acknowledges to come
                    int countMax = pollrate * 1000 / 100;
                    for (int i = 0; i < countMax; i++ )
                    {
                        if (ackList.isEmpty())
                        {
                            break;
                        }
                        try
                        {
                            Thread.sleep(100);
                        } catch (InterruptedException ex) 
                        {
                            // ignore exception
                        }
                    }

                    // if we did not receive all the ACKs, drop the connection
                    if (!ackList.isEmpty())
                    {
                        tc.disconnect();
                    }
                }
            } catch (TransportException | IOException ex) 
            {
                try 
                {
                    tc.disconnect();
                } catch (IOException ex1) 
                {
                    // Ignore errors on disconnect, connection has probably already been terminated
                }
                Logger.getLogger(TransportClient.class.getName()).log(Level.SEVERE, null, ex);
            }   catch (InterruptedException ex) 
            {
                Logger.getLogger(TransportClient.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
            
    private class MessageReceiver extends Thread
    {
        private final SDTLInputStream input;
        
        public MessageReceiver(InputStream is)
        {
            input = new SDTLInputStream(is);
        }
        
        @Override
        public void run()
        {
            TransportFrame tf = null;
        
            try 
            {
                while (true)
                {                    
                    tf = input.read();

                    switch(tf.getID())
                    {
                        case TransportID.ACK:
                            AckFrame a = AckFrame.fromByteBuffer(tf.getPayload());
                            ackList.remove(a.getHash());                            
                            tb.remove(a.getHash());
                            break;

                        default:
                            // ACK the other frames that I do not support
                            AckFrame ack = AckFrame.newBuilder()
                                    .setAck(false)
                                    .setHash(tf.hashCode())
                                    .setTimestamp(new Date().getTime())
                                    .build();
                            TransportFrame t = TransportFrame.newBuilder()
                                    .setID(TransportID.ACK)
                                    .setPayload(ack.toByteBuffer())
                                    .build();
                            send(t);
                            break;
                    }
                }
            } catch (TransportException | IOException ex) 
            {
                Logger.getLogger(TransportClient.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
