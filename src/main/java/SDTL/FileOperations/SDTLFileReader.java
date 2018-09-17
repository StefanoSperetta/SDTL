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
import java.util.NoSuchElementException;

import org.apache.avro.file.DataFileReader;
import org.apache.avro.io.DatumReader;
import org.apache.avro.specific.SpecificDatumReader;

public class SDTLFileReader
{
    private final DataFileReader<DownlinkFrame> dataFileReader;

    public SDTLFileReader(String filename) throws IOException
    {
        File file = new File(filename);

        DatumReader<DownlinkFrame> datumReader = new SpecificDatumReader<>(DownlinkFrame.class);
        dataFileReader = new DataFileReader<>(file, datumReader);
    }

    /** Close the file
     * @throws IOException thrown upon error closing the file
     */
    public void close() throws IOException
    {
        dataFileReader.close();
    }

    public boolean hasNext()
    {
        return dataFileReader.hasNext();
    }

    /** Read one record from the file
     * @throws NoSuchElementException thrown if the iteration has no more elements
     * @return object read from file
     */
    public DownlinkFrame next() throws NoSuchElementException
    {
        return dataFileReader.next();
    }

    /** Read one record from the file and reuse the object
     * @param t reuse this object instead of creating a new one
     * @throws NoSuchElementException thrown if the iteration has no more elements
     * @throws IOException
     */
    public void next(DownlinkFrame t) throws NoSuchElementException, IOException
    {
        dataFileReader.next(t);
    }
}
