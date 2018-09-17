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
package SDTL.StreamOperations;
        
import SDTL.Protocol.DownlinkFrame;
import java.nio.ByteBuffer;
import java.util.Date;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author Stefano Speretta <s.speretta@tudelft.nl>
 */      
public class TestStreamOperations 
{
    @Test
    public void SerDesTest() throws Exception 
    {
        LoopbackStream ls = new LoopbackStream();
        
        SDTLOutputStream os = new SDTLOutputStream(ls.getOutputStream());
        SDTLInputStream is = new SDTLInputStream(ls.getInputStream());
        
        
        DownlinkFrame f1 = DownlinkFrame.newBuilder()
                            .setReceptionTime(new Date().getTime())
                            .setClientSubmissionTime(new Date().getTime())
                            .setServerReceptionTime(new Date().getTime())
                            .setData(ByteBuffer.wrap(new byte[10]))
                            .setGs("1")
                            .build();
        DownlinkFrame f2 = DownlinkFrame.newBuilder()
                            .setReceptionTime(new Date().getTime())
                            .setClientSubmissionTime(new Date().getTime())
                            .setServerReceptionTime(new Date().getTime())
                            .setData(ByteBuffer.wrap(new byte[10]))
                            .setGs("2")
                            .build();
        DownlinkFrame f3 = DownlinkFrame.newBuilder()
                            .setReceptionTime(new Date().getTime())
                            .setClientSubmissionTime(new Date().getTime())
                            .setServerReceptionTime(new Date().getTime())
                            .setData(ByteBuffer.wrap(new byte[10]))
                            .setGs("3")
                            .build();
        os.write(f1);
        os.write(f2);
        os.write(f3);
        os.flush();

        assertEquals(f1, is.read());
        assertEquals(f2, is.read());
        assertEquals(f3, is.read());
    }
}
