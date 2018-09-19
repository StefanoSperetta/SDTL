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
import java.io.IOException;
import java.io.OutputStream;
import org.apache.avro.io.BinaryEncoder;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.specific.SpecificDatumWriter;

/**
 *
 * @author Stefano Speretta <s.speretta@tudelft.nl>
 */
public class SDTLOutputStream
{
    private final DatumWriter<TransportFrame> datumWriter;
    private final BinaryEncoder binaryEncoder;
    
    public SDTLOutputStream(OutputStream os)
    {
        datumWriter = new SpecificDatumWriter<>(TransportFrame.class);
        EncoderFactory enc = new EncoderFactory();
        binaryEncoder = enc.binaryEncoder(os, null);
    }
    
    public void write(TransportFrame record) throws IOException
    {
        datumWriter.write(record, binaryEncoder);
    }
    
    public void flush() throws IOException
    {
        binaryEncoder.flush();
    }
}
