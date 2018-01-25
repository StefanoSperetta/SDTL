package demo;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;

import SDTL.FileOperations.FileWriter;
import SDTL.Protocol.DownlinkFrame;

public class ImportPyTrack 
{
	public static void main(String[] args)
	{
		String filename = "/Users/stefanosperett/Documents/Delfi-C3/Telemetry/PyTrack/20180113_085605z_145868900_32789_packets.log";
		
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss.SSS z");
		
		try(BufferedReader br = new BufferedReader(new FileReader(filename))) 
		{
			int index = 0;
			FileWriter<DownlinkFrame> fw = new FileWriter<DownlinkFrame>(DownlinkFrame.SCHEMA$, "frames.avro");
			
		    for(String line; (line = br.readLine()) != null; ) 
		    {
		        // process the line.
		    		System.out.println(line);
		    		String[] separated = line.split(",");		    		
		    		
		    		Date result =  df.parse(separated[0]); 
		    		ByteBuffer data = ByteBuffer.allocate(separated[1].length() / 2);

		    		for (int i = 0; i < separated[1].length(); i+=2)
		    		{
		    			data.put((byte)Integer.parseInt(separated[1].substring(i, i+2), 16));
		    			//System.out.print( String.format("%02X", data[i / 2]) + " ");
		    		}
		    		data.flip();
		    		System.out.println();
		    		System.out.println(result + " --- " + separated[1] );
		    		
		    		DownlinkFrame f0 = DownlinkFrame.newBuilder()
	    				.setId(index++)
	    				.setTimestamp(result.getTime())
	    				.setData(data)
	    				.build();
		    		
		    		fw.append(f0);
		    		System.out.println(f0);
		    }
		    
		    fw.close();
		    // line is not visible here.
		} catch (FileNotFoundException e) 
		{
			Logger.getLogger(ImportPyTrack.class).error("File not found.", e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
}
