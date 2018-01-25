package SDTL.FileOperations;

import java.io.File;
import java.io.IOException;

import org.apache.avro.Schema;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.generic.GenericDatumWriter;
import org.apache.avro.io.DatumWriter;

/**
 * @author Stefano Speretta <s.speretta@tudelft.nl>
 *
 * @param <T> Generic class type used to initialize the internal methods
 */
public class FileWriter<T>
{
	private final DataFileWriter<T> dataFileWriter;
	
	/** Function constructor
	 * @param schema schema needed to save data to file
	 * @param filename file name to create with path
	 * @throws NullPointerException	thrown with null filename
	 * @throws IOException thrown when file creation fails
	 */
	public FileWriter(Schema schema, String filename) throws NullPointerException, IOException
	{
		File file = new File(filename);
		
		DatumWriter<T> datumWriter = new GenericDatumWriter<T>(schema);
	    dataFileWriter = new DataFileWriter<T>(datumWriter);
	    dataFileWriter.create(schema, file);
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
	public void append(T record) throws IOException
	{
		dataFileWriter.append(record);
	}
}
