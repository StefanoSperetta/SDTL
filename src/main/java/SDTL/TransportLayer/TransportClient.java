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
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Stefano Speretta <s.speretta@tudelft.nl>
 */
public class TransportClient implements ReceiveHandler
{
    private final TransportBuffer tb;
    private final TransportConnector tc;
    private final ScheduledExecutorService scheduler;
    private final int pollrate;
    private static boolean periodicTaskRunning = false;
    private final List<Integer> ackList = new LinkedList<>();
    
    public TransportClient(String db, TransportConnector connector, int updateRate) throws TransportException
    {
        pollrate = updateRate;
        tb = new TransportBuffer(db);
        tc = connector;
        
        scheduler = Executors.newScheduledThreadPool(1);
    }
    
    public void start()
    {
        tc.registerReceiveHandler(this);
        scheduler.scheduleAtFixedRate(new Runnable()
        {
            @Override
            public void run() 
            {
                periodicTask();
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

        // start the sender, in case it is not running
        new Thread(new Runnable()
        {
            @Override
            public void run() 
            {
                periodicTask();
            }                
        }).start();
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

    private void connect()
    {
        tc.connect();
        
        // send the schema
    }
    
    public void periodicTask() 
    {
        if (periodicTaskRunning)
        {
            return;
        }
        
        periodicTaskRunning = true;
        try 
        {
            System.out.println("TransportClient thread " + new Date());

            // if not connected, try to connect
            if (!tc.connected())
            {
                connect();
            }

            // if connect was succesfull, transfer the frames
            if (tc.connected())
            {
                TransportFrame f = tb.getNextFrame();
                while ((f != null) && tc.connected())
                {
                    tc.send(f);
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
                System.out.println("remaining " + ackList.size());
                // if we did not receive all the ACKs, drop the connection
                if (!ackList.isEmpty())
                {
                    tc.disconnect();
                }
            }
        } catch (TransportException ex) 
        {
            Logger.getLogger(TransportClient.class.getName()).log(Level.SEVERE, null, ex);
        } 
        periodicTaskRunning = false;
    }
    
    @Override
    public void messageReceived(TransportFrame tf)
    {
        try 
        {
            switch(tf.getID())
            {
                case TransportID.ACK:
                    AckFrame a = AckFrame.fromByteBuffer(tf.getPayload());
                    System.out.println("Removing " + a.getHash());
                    tb.remove(a.getHash());
                    System.out.println("Found " + ackList.remove(a.getHash()));
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
        } catch (TransportException | IOException ex) 
        {
            Logger.getLogger(TransportClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
