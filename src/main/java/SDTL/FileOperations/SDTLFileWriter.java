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
