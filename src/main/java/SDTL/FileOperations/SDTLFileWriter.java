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
package SDTL.FileOperations;

import SDTL.Protocol.DownlinkFrame;
import java.io.File;
import java.io.IOException;

import org.apache.avro.file.DataFileWriter;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.specific.SpecificDatumWriter;

/**
 * @author Stefano Speretta <s.speretta@tudelft.nl>
 *
 */
public class SDTLFileWriter
{
    private final DataFileWriter<DownlinkFrame> dataFileWriter;

    /** Function constructor
     * @param filename file name to create with path
     * @throws NullPointerException	thrown with null filename
     * @throws IOException thrown when file creation fails
     */
    public SDTLFileWriter(String filename) throws NullPointerException, IOException
    {
        File file = new File(filename);

        DatumWriter<DownlinkFrame> datumWriter = new SpecificDatumWriter<>(DownlinkFrame.class);
        dataFileWriter = new DataFileWriter<>(datumWriter);
        dataFileWriter.create(DownlinkFrame.SCHEMA$, file);
    }

    /** Close the file
     * @throws IOException thrown upon error closing the file
     */
    public void close() throws IOException
    {
        dataFileWriter.close();
    }

    /** Append one record to the file
     * @param record record to append
     * @throws IOException thrown upon file error
     */
    public void append(DownlinkFrame record) throws IOException
    {
        dataFileWriter.append(record);
    }

    public void flush() throws IOException
    {
        dataFileWriter.flush();
    }
}
