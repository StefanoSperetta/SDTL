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
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import org.apache.avro.io.BinaryDecoder;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.specific.SpecificDatumReader;

/**
 *
 * @author Stefano Speretta <s.speretta@tudelft.nl>
 */
public class SDTLInputStream
{
    private final DatumReader<TransportFrame> datumReader;
    private final BinaryDecoder binaryDecoder;
    private final InputStream is;
    private boolean open = true;
    
    public SDTLInputStream(InputStream is)
    {
        datumReader = new SpecificDatumReader<>(TransportFrame.class);
        binaryDecoder = DecoderFactory.get().binaryDecoder(new InputStreamWrapper(is), null);
        this.is = is;
    }
    
    public void close() throws IOException
    {
        open = false;
        is.close();
    }
    
    public TransportFrame read() throws IOException
    {
        while (open)
        {
            try
            {
                TransportFrame t1 = datumReader.read(null, binaryDecoder);
                return t1;
            } catch (EOFException ex)
            {
                // there is no byte available, simply retry...
                // the function is supposed to block till a frame is received
                try 
                {
                    Thread.sleep(10);
                } catch (InterruptedException ex1) 
                {
                    // ignore this exception
                }
            }
        }
        throw new IOException("Connection closed.");
    }  
    
    private class InputStreamWrapper extends InputStream
    {
        InputStream father;
        
        public InputStreamWrapper (InputStream f)
        {
            father = f;
        }
        
        @Override
        public int available() throws IOException
        {
            return father.available();
        }
        
        @Override
        public int read() throws IOException 
        {
            if (father.available() > 0)
            {
                return father.read();
            }
            else
            {
                return -1;
            }
        }        
    }
}
