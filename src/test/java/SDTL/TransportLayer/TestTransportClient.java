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

import SDTL.Protocol.DownlinkFrame;
import SDTL.Protocol.TransportFrame;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Date;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author Stefano Speretta <s.speretta@tudelft.nl>
 */
public class TestTransportClient 
{
    private int frameCounter = 0;
            
    @Test
    public void testClient() throws TransportException, InterruptedException 
    {
        DebugConnector dc = new DebugConnector();
        dc.disable();
        
        TransportClient tc = new TransportClient("./testDB", dc, 3);
        tc.clearRepository();
        assertEquals(0, tc.getQueueSize());
        
        tc.start();
        
        final DownlinkFrame[] df = new DownlinkFrame[3];
        
        df[0] = DownlinkFrame.newBuilder()
                .setReceptionTime(new Date().getTime())
                .setClientSubmissionTime(new Date().getTime())
                .setServerReceptionTime(new Date().getTime())
                .setData(ByteBuffer.wrap(new byte[10]))
                .setGs("A")
                .build();

        df[1] = DownlinkFrame.newBuilder()
                .setReceptionTime(new Date().getTime())
                .setClientSubmissionTime(new Date().getTime())
                .setServerReceptionTime(new Date().getTime())
                .setData(ByteBuffer.wrap(new byte[10]))
                .setGs("B")
                .build();
        
        df[2] = DownlinkFrame.newBuilder()
                .setReceptionTime(new Date().getTime())
                .setClientSubmissionTime(new Date().getTime())
                .setServerReceptionTime(new Date().getTime())
                .setData(ByteBuffer.wrap(new byte[10]))
                .setGs("C")
                .build(); 
        
        dc.registerFrameReceivedEvent(new FrameReceivedEvent()
        {
            @Override
            public void frameReceived(TransportFrame tf) 
            {
                try 
                {
                    System.out.println("Server received: " + tf);
                    TransportFrame expected = TransportFrame.newBuilder()
                            .setID(1)
                            .setPayload(df[frameCounter].toByteBuffer())
                            .build();
                    
                    assertEquals(expected, tf);
                    frameCounter++;
                } catch (IOException ex) 
                {
                    ex.printStackTrace();
                }
            }
            
        });
        tc.submitDownlinkFrame(df[0]);
        tc.submitDownlinkFrame(df[1]);
        
        Thread.sleep(4000);
        assertEquals("Link is disabled, 2 messages are supposed to be in the queue.",2, tc.getQueueSize());
        
        System.out.println("Enabling connecction...");
        dc.enable();
        Thread.sleep(4000);
        
        // all messages transmitted
        assertEquals("All messages are supposed to have been sent.", 0, tc.getQueueSize());
        
        Thread.sleep(4000);
        
        tc.submitDownlinkFrame(df[2]);

        Thread.sleep(4000);
        
        assertEquals("All messages are supposed to have been sent.",0, tc.getQueueSize());
    }
}
