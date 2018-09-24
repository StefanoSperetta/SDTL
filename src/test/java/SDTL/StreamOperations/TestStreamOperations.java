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
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import org.junit.Assert;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author Stefano Speretta <s.speretta@tudelft.nl>
 */      
public class TestStreamOperations 
{
    @Test
    public void SerDesTest() throws Exception 
    {
        System.out.println("-> " + Thread.currentThread().getStackTrace()[1].getMethodName());
        
        LoopbackStream ls = new LoopbackStream();
        
        SDTLOutputStream os = new SDTLOutputStream(ls.getOutputStream());
        SDTLInputStream is = new SDTLInputStream(ls.getInputStream());
                
        TransportFrame t0 = TransportFrame.newBuilder()
                .setID(0)
                .setPayload(ByteBuffer.wrap(new byte[10]))
                .build();
        TransportFrame t1 = TransportFrame.newBuilder()
                .setID(2)
                .setPayload(ByteBuffer.wrap(new byte[10]))
                .build();
        TransportFrame t2 = TransportFrame.newBuilder()
                .setID(3)
                .setPayload(ByteBuffer.wrap(new byte[10]))
                .build();
        
        os.write(t0);
        os.write(t1);
        os.write(t2);
        os.flush();

        assertEquals(t0, is.read());
        assertEquals(t1, is.read());
        assertEquals(t2, is.read());
    }
    
    @Test
    public void SerializeOnSocketTest() throws Exception 
    {
        System.out.println("-> " + Thread.currentThread().getStackTrace()[1].getMethodName());
        
        TransportFrame[] input = new TransportFrame[10];
        for (int i = 0; i < input.length; i++)
        {
            input[i] = TransportFrame.newBuilder()
                .setID(3)
                .setPayload(ByteBuffer.wrap(String.format("%d", i).getBytes()))
                .build();
        }
        
        SocketServer ss = new SocketServer(input);
        ss.start();

        // wait for the server to be ready to accept connections
        Thread.sleep(1000);
        
        Socket skt = new Socket("localhost", 56000);
        SDTLInputStream is = new SDTLInputStream(skt.getInputStream());
        
        for (TransportFrame input1 : input) 
        {
            assertEquals(input1, is.read());
        }
        
        is.close();
                
        try
        {
            is.read();
            Assert.fail( "An IOException is expected to be thrown" );
        } catch (IOException ex)
        {
            // correct behaviour
        }
    }
    
    private class SocketServer extends Thread 
    {
        private ServerSocket server;
        private final TransportFrame[] data;
        
        public SocketServer(TransportFrame[] input)
        {
            data = input;
        }
        
        @Override
        public void run() 
        {
            try 
            {
                server = new ServerSocket(56000);
                
                Socket conn = server.accept();
                SDTLOutputStream os = new SDTLOutputStream(conn.getOutputStream());
                
                for (int i = 0; i < data.length; i++)
                {
                    os.write(data[i]);
                }
                os.flush();
                
                Thread.sleep(1000);
                
            } catch (IOException ex) 
            {
                ex.printStackTrace();                
            } catch (InterruptedException ex) 
            {
                
            } finally 
            {
                try 
                {
                    if (server != null)
                        server.close();
                } catch (IOException ex) 
                {
                    ex.printStackTrace();
                }
            }
        }
    }
}
