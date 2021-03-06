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

import SDTL.Protocol.TransportFrame;
import java.nio.ByteBuffer;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Stefano Speretta <s.speretta@tudelft.nl>
 */
public class TestTransportBuffer 
{
    @Test
    public void testBuffer() throws TransportException 
    {
        System.out.println("-> " + Thread.currentThread().getStackTrace()[1].getMethodName());
        
        TransportBuffer tb = new TransportBuffer("./testDB");
                
        tb.cleanDB();
        assertEquals(0, tb.getCount());
        
        TransportFrame t0 = TransportFrame.newBuilder()
                .setID(0)
                .setPayload(ByteBuffer.wrap(new byte[10]))
                .build();
        
        TransportFrame t1 = TransportFrame.newBuilder()
                .setID(1)
                .setPayload(ByteBuffer.wrap(new byte[10]))
                .build();
                
        TransportFrame t2 = TransportFrame.newBuilder()
                .setID(2)
                .setPayload(ByteBuffer.wrap(new byte[10]))
                .build();
        
        tb.insertFrame(t0);
        tb.insertFrame(t1);
        
        assertEquals(2, tb.getCount());
                
        TransportFrame r0 = tb.getNextFrame();
        assertEquals(t0, r0);
        assertEquals(true, tb.remove(t0.hashCode()));

        assertEquals(1, tb.getCount());
        
        tb.insertFrame(t2);
        assertEquals(2, tb.getCount());
        
        TransportFrame r1 = tb.getNextFrame();
        assertEquals(t1, r1);
        
        TransportFrame r21 = tb.getFrame(t2.hashCode());
        assertEquals(t2, r21);
        
        // provide a wrong hash and check if the remove fails
        assertEquals(false, tb.remove(t1.hashCode()+1));
        
        assertEquals(true, tb.remove(t1.hashCode()));
        
        TransportFrame r2 = tb.getNextFrame();
        assertEquals(t2, r2);
        
        assertEquals(1, tb.getCount());
        
        tb.cleanDB();
        assertEquals(0, tb.getCount());
        
        TransportFrame empty = tb.getNextFrame();
        assertEquals(null, empty);
    }
}
