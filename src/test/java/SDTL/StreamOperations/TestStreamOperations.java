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
        
import SDTL.Protocol.TransportFrame;
import java.nio.ByteBuffer;
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
                
        TransportFrame t0 = TransportFrame.newBuilder()
                .setID(0)
                .setPayload(ByteBuffer.wrap(new byte[10]))
                .build();
        TransportFrame t1 = TransportFrame.newBuilder()
                .setID(2)
                .setPayload(ByteBuffer.wrap(new byte[10]))
                .build();
        TransportFrame t2 = TransportFrame.newBuilder()
                .setID(3)
                .setPayload(ByteBuffer.wrap(new byte[10]))
                .build();
        
        os.write(t0);
        os.write(t1);
        os.write(t2);
        os.flush();

        assertEquals(t0, is.read());
        assertEquals(t1, is.read());
        assertEquals(t2, is.read());
    }
}
