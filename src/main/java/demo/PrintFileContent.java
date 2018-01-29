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
