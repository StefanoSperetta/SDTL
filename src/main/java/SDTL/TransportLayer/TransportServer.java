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
import java.io.OutputStream;
import java.util.Date;

/**
 *
 * @author Stefano Speretta <s.speretta@tudelft.nl>
 */
public class TransportServer extends Thread
{
    private final SDTLInputStream is;
    private final SDTLOutputStream os;
    private boolean running = false;
    private final SDTLServerConnector sc;
    
    public TransportServer(InputStream is, OutputStream os, SDTLServerConnector sc)
    {
        this.is = new SDTLInputStream(is);
        this.os = new SDTLOutputStream(os);
        this.sc = sc;
    }

    public void close() throws IOException
    {
        running = false;
        is.close();
        os.close();
    }
    
    @Override
    public void run() 
    {
        running = true;

        try 
        {
            while (running)
            {
                TransportFrame tf = is.read();
                if (tf != null)
                {
                    switch(tf.getID())
                    {
                        case TransportID.SUBMITDOWNLINKFRAME:
                            DownlinkFrame a = DownlinkFrame.fromByteBuffer(tf.getPayload());                            
                            
                            sc.receivedDownlinkFrame(a);
                            
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
                            os.write(replyNack);
                            os.flush();
                            break;
                    }
                }
                else
                {
                    // in case of a read error, terminate the connection
                    running = false;
                }
            }
        } catch (IOException ex)
        {
            // not an error, stream has been closed
        } catch (TransportException ex) 
        {
            ex.printStackTrace();
            try   
            {
                is.close();
                os.close();
            } catch (IOException ex1) 
            {
                // ignore this exception
            }
        }
        running = false;
    }
}
