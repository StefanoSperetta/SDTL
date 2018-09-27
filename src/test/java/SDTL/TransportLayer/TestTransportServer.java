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
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Date;
import org.junit.Test;

/**
 *
 * @author Stefano Speretta <s.speretta@tudelft.nl>
 */
public class TestTransportServer 
{    
    @Test
    public void testServer() throws TransportException, InterruptedException, IOException 
    {
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
        
        TransportFrame tf0 = TransportFrame.newBuilder()
                .setID(TransportID.SUBMITDOWNLINKFRAME)
                .setPayload(f0.toByteBuffer())
                .build();
        TransportFrame tf1 = TransportFrame.newBuilder()
                .setID(TransportID.SUBMITDOWNLINKFRAME)
                .setPayload(f1.toByteBuffer())
                .build();
        TransportFrame tf2 = TransportFrame.newBuilder()
                .setID(TransportID.SUBMITDOWNLINKFRAME)
                .setPayload(f2.toByteBuffer())
                .build();
        TransportFrame tf3 = TransportFrame.newBuilder()
                .setID(124)
                .setPayload(ByteBuffer.wrap(new byte[10]))
                .build();
        
        LoopbackStream ls = new LoopbackStream();
        
        SDTLOutputStream os = new SDTLOutputStream(ls.getOutputStream());
        
        os.write(tf0);
        os.write(tf1);
        os.write(tf2);
        os.write(tf3);
        os.flush();

        ByteArrayOutputStream os2 = new ByteArrayOutputStream( );
        TransportServer ts = new TransportServer(ls.getInputStream(), os2, new myServerConnector());
        ts.start();
        
        Thread.sleep(1000);

        SDTLInputStream ns = new SDTLInputStream(new ByteArrayInputStream(os2.toByteArray()));
        
        for (int i = 0; i < 4; i++)
        {
            TransportFrame t = ns.read();
            AckFrame af = AckFrame.fromByteBuffer(t.getPayload());
            System.out.println(af);
        }
    }
    
    private class myServerConnector implements SDTLServerConnector
    {
        @Override
        public void receivedDownlinkFrame(DownlinkFrame f) throws TransportException 
        {
            System.out.println("myServerConnector.receivedDownlinkFrame " + f);
        }

    }
}
