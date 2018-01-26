package demo;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Calendar;
import java.util.NoSuchElementException;

import SDTL.FileOperations.FileReader;
import SDTL.Protocol.DownlinkFrame;

public class PrintFileContent 
{
	public static void main(String[] args)
	{
		try 
		{
			int index = 0;
			FileReader<DownlinkFrame> reader = new FileReader<DownlinkFrame>(DownlinkFrame.SCHEMA$, "frames.avro");
			DownlinkFrame frame = new DownlinkFrame();
			while (reader.hasNext()) 
		    {
		      reader.next(frame);
		      System.out.print(index++ + ": " + frame.hashCode() + " ");
		      Calendar calendar = Calendar.getInstance();
		      calendar.setTimeInMillis(frame.getTimestamp());
		      ByteBuffer data = frame.getData();
		      
		      System.out.print(calendar.getTime() + ", ");
		      while (data.hasRemaining())
		          System.out.print(String.format("%02X ", data.get()));
		      System.out.println();
		    }
			reader.close();
		    
		} catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchElementException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
