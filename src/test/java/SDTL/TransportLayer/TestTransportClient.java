/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SDTL.TransportLayer;

import SDTL.Protocol.DownlinkFrame;
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
    @Test
    public void testClient() throws TransportException, InterruptedException 
    {
        int delay = 10;
        DebugConnector dc = new DebugConnector();
        dc.disable();
        
        TransportClient tc = new TransportClient("./testDB", dc, delay);
        tc.clearRepository();
        assertEquals(0, tc.getQueueSize());
        
        tc.start();
        
        DownlinkFrame f0 = DownlinkFrame.newBuilder()
                .setReceptionTime(new Date().getTime())
                .setClientSubmissionTime(new Date().getTime())
                .setServerReceptionTime(new Date().getTime())
                .setData(ByteBuffer.wrap(new byte[10]))
                .setGs("A")
                .build();

        DownlinkFrame f1 = DownlinkFrame.newBuilder()
                .setReceptionTime(new Date().getTime())
                .setClientSubmissionTime(new Date().getTime())
                .setServerReceptionTime(new Date().getTime())
                .setData(ByteBuffer.wrap(new byte[10]))
                .setGs("B")
                .build();
        
        DownlinkFrame f2 = DownlinkFrame.newBuilder()
                .setReceptionTime(new Date().getTime())
                .setClientSubmissionTime(new Date().getTime())
                .setServerReceptionTime(new Date().getTime())
                .setData(ByteBuffer.wrap(new byte[10]))
                .setGs("C")
                .build(); 
        
        tc.submitDownlinkFrame(f0);
        tc.submitDownlinkFrame(f1);
        
        Thread.sleep(12000);
        
        dc.enable();
        
        Thread.sleep(24000);
        
        tc.submitDownlinkFrame(f2);
        
        Thread.sleep(24000);
        
        assertEquals(0, tc.getQueueSize());
    }
}
