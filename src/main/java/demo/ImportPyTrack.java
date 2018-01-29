/*
 *  SDTL: A data transfer layer to transfer data between a ground station and a data archive.
 *  Copyright (C) 2017  Stefano Speretta - Delft University of Technology
 * 
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 * 
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package demo;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import SDTL.FileOperations.FileWriter;
import SDTL.Protocol.DownlinkFrame;

public class ImportPyTrack 
{
	public static void main(String[] args) throws IOException
	{
		String filename = "/Users/stefanosperett/Documents/Delfi-C3/Telemetry/PyTrack/201801*_145868900_32789_packets.log";
		
		Path p = Paths.get(filename);
		
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss.SSS z");
		List<Path> directory = new ArrayList<Path>();
		
		int counter = 0;
		FileWriter<DownlinkFrame> fw = new FileWriter<DownlinkFrame>(DownlinkFrame.SCHEMA$, "frames.avro");
		
		
		try (DirectoryStream<Path> dirStream = Files.newDirectoryStream(Paths.get(p.getParent().toString()), p.getFileName().toString()))
		{
			//dirStream.forEach(path -> System.out.println(path));
			dirStream.forEach(directory::add);
		}
		directory.sort(Comparator.comparing(Path::toString));
		for (Path entry : directory)
		{
			System.out.println("----> " + entry);
		
			try(BufferedReader br = new BufferedReader(new FileReader(entry.toFile()))) 
			{
				
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
			    		}
			    		data.flip();
	
			    		System.out.println(result + " --- " + separated[1] );
			    		
			    		DownlinkFrame f0 = DownlinkFrame.newBuilder()
		    				.setTimestamp(result.getTime())
		    				.setData(data)
		    				.setGs("delfiground_primary")
		    				.build();
			    		
			    		fw.append(f0);
			    		System.out.println(f0);
			    		counter++;
			    }
			    
			    
			    
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
		System.out.println();
	    System.out.println("Added " + counter + " frames");
		fw.close();
	}
}
