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
import SDTL.StreamOperations.LoopbackStream;
import SDTL.StreamOperations.SDTLInputStream;
import SDTL.StreamOperations.SDTLOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

/**
 *
 * @author Stefano Speretta <s.speretta@tudelft.nl>
 */
public class DebugConnector implements SDTLClientConnector
{
    private boolean connected = false;
    private boolean enabled = true;
    private final LoopbackStream input = new LoopbackStream();
    private final LoopbackStream output = new LoopbackStream();
    private handler hnd = null;
    private FrameReceivedEvent fre = null;
    
    public void enable()
    {
        enabled = true;       
    }
    
    public void disable()
    {
        enabled = false;
    }

    public void registerFrameReceivedEvent(FrameReceivedEvent fr)
    {
        fre = fr;
    }
    
    @Override
    public void connect() 
    {
        connected = enabled;
        hnd = new handler();
        hnd.start();
        if (connected)
        {
            System.out.println("Connection established");
        }
        else
        {
            System.out.println("Connection failed");
        }
    }
    
    @Override
    public void disconnect() throws IOException 
    {
        connected = false;
        if (hnd != null)
        {
            input.close();
            output.close();
        }
        System.out.println("Disconnected");
    }

    @Override
    public boolean connected() 
    {
        return connected;
    }

    @Override
    public InputStream getInputStream() throws IOException
    {
        return output.getInputStream();
    }

    @Override
    public OutputStream getOutputStream() throws IOException
    {
        return input.getOutputStream();
    }    

    private class handler extends Thread
    {
        @Override
        public void run() 
        {
            try 
            {
                SDTLInputStream is = new SDTLInputStream(input.getInputStream());
                SDTLOutputStream os = new SDTLOutputStream(output.getOutputStream());                
                
                while (connected)
                {
                    TransportFrame tf = is.read();
                    if (fre != null)
                    {
                        fre.frameReceived(tf);
                    }

                    switch(tf.getID())
                    {
                        case TransportID.SUBMITDOWNLINKFRAME:
                            DownlinkFrame a = DownlinkFrame.fromByteBuffer(tf.getPayload());                            
                            
                            // ACK the other frames that I do not support
                            AckFrame ack = AckFrame.newBuilder()
                                    .setAck(true)
                                    .setHash(tf.hashCode())
                                    .setTimestamp(new Date().getTime())
                                    .build();
                            TransportFrame reply = TransportFrame.newBuilder()
                                    .setID(TransportID.ACK)
                                    .setPayload(ack.toByteBuffer())
                                    .build();
                            System.out.println("Server Replying: " + reply);
                            os.write(reply);
                            os.flush();
                            break;
                            
                        default:
                            // ACK the other frames that I do not support
                            AckFrame nack = AckFrame.newBuilder()
                                    .setAck(false)
                                    .setHash(tf.hashCode())
                                    .setTimestamp(new Date().getTime())
                                    .build();
                            TransportFrame replyNack = TransportFrame.newBuilder()
                                    .setID(TransportID.ACK)
                                    .setPayload(nack.toByteBuffer())
                                    .build();
                            System.out.println("Reply: " + replyNack);
                            os.write(replyNack);
                            os.flush();
                            break;
                    }
                }
            } catch (IOException ex) 
            {
                ex.printStackTrace();
            }
        }
    }
}
