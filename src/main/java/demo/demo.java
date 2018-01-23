package demo;

import java.io.File;
import java.io.IOException;

import org.apache.avro.Schema;
import org.apache.avro.file.DataFileReader;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericDatumWriter;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.io.parsing.Parser;

import Protocol.DownlinkFrame;

public class demo 
{

	public demo()
	{
		System.out.println("A");
		DownlinkFrame f = new DownlinkFrame();
		f.put("id", 100);
		
		try {
			//System.out.println(getClass().getResourceAsStream("/avro/transport.avsc"));
			//Schema schema=Schema.parse(getClass().getResourceAsStream("Pair.avsc"));
			Schema schema = new Schema.Parser().parse(getClass().getResourceAsStream("/avro/DownlinkFrame.avsc"));
			
			//System.out.println(schema);
			File file = new File("users.avro");
			
			DatumWriter<DownlinkFrame> datumWriter = new GenericDatumWriter<DownlinkFrame>(schema);
		    DataFileWriter<DownlinkFrame> dataFileWriter = new DataFileWriter<DownlinkFrame>(datumWriter);
		    dataFileWriter.create(schema, file);
		    dataFileWriter.append(f);
		    dataFileWriter.append(f);
		    dataFileWriter.close();
		    
		 // Write some records to deserialize.
		    //DatumWriter<CharSequence> datumWriter = new GenericDatumWriter<CharSequence>(writerSchema);
		    //ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		    //Encoder encoder = EncoderFactory.get().binaryEncoder(outputStream, null);
		    //datumWriter.write("record1", encoder);
		    //datumWriter.write("record2", encoder);
		    //encoder.flush();
		    
		 // Deserialize users from disk
		    DatumReader<DownlinkFrame> datumReader = new GenericDatumReader<DownlinkFrame>(schema);
		    DataFileReader<DownlinkFrame> dataFileReader = new DataFileReader<DownlinkFrame>(file, datumReader);
		    GenericRecord user = null;
		    while (dataFileReader.hasNext()) 
		    {
		      // Reuse user object by passing it to next(). This saves us from
		      // allocating and garbage collecting many objects for files with
		      // many items.
		      user = dataFileReader.next();
		      System.out.println(user);
		    }
		    dataFileReader.close();
		    
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public static void main(String[] args)
	{
		new demo();
	}
}