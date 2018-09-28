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
package SDTL.TransportLayer;

import SDTL.Protocol.DownlinkFrame;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.Date;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author Stefano Speretta <s.speretta@tudelft.nl>
 */
public class TestTransportLayer 
{
    private DownlinkFrame received = null;
    
    @Test
    public void testTransportLayer() throws InterruptedException, TransportException
    {
        System.out.println("-> " + Thread.currentThread().getStackTrace()[1].getMethodName());
        
        SocketServerController sc = new SocketServerController();
        sc.start(); 
        
        // give some time to the server to be ready to accept conenctions
        Thread.sleep(200);
        
        TCPSocketConnector cc = new TCPSocketConnector("localhost", sc.getPort());
        TransportClient tc = new TransportClient("./testDB", cc, 20);
        tc.clearRepository();
        tc.start();
        
        // give some time to the client to be initialized
        Thread.sleep(200);
        
        DownlinkFrame f0 = DownlinkFrame.newBuilder()
                .setReceptionTime(new Date().getTime())
                .setClientSubmissionTime(new Date().getTime())
                .setServerReceptionTime(new Date().getTime())
                .setData(ByteBuffer.wrap(new byte[10]))
                .setGs("A")
                .build();
        tc.submitDownlinkFrame(f0);
        
        // ensure the frame has been transfered
        Thread.sleep(500);
        
        sc.close();
        assertEquals(f0, received);
    }
    
    private class SocketServerController extends Thread 
    {
        private ServerSocket server = null;
        private boolean open;
        
        public void close()
        {
            open = false;
            if (server != null)
            {
                try 
                {
                    server.close();
                } catch (IOException ex) 
                {
                    // I do not care
                }
            }
        }
        
        public int getPort()
        {
            if (server != null)
            {
                return server.getLocalPort();
            }
            else
            {
                return 0;
            }
        }
        
        @Override
        public void run() 
        {
            open = true;
            try 
            {
                server = new ServerSocket(0);
                while (open) 
                {
                    /**
                     * create a new {@link SocketServer} object for each connection
                     * this will allow multiple client connections
                     */
                    new SocketServer(server.accept());
                }
            } catch (IOException ex) 
            {
                if (open)
                {
                    ex.printStackTrace();
                }
            } finally 
            {
                try 
                {
                    if (server != null)
                        server.close();
                } catch (IOException ex) 
                {
                    if (open)
                    {
                        ex.printStackTrace();
                    }
                }
            }
        }
    }
    
    private class SocketServer extends Thread 
    {
        protected Socket socket;

        private SocketServer(Socket socket) 
        {
            this.socket = socket;
            start();
        }

        @Override
        public void run() 
        {
            try 
            {
                TransportServer ts = new TransportServer(socket.getInputStream(), 
                        socket.getOutputStream(), new SDTLServerConnector()
                        {
                            @Override
                            public void receivedDownlinkFrame(DownlinkFrame f) throws TransportException 
                            {
                                received = f; 
                            }
                        });
                ts.run();
            } catch (IOException ex) 
            {
                ex.printStackTrace();
            } 
        }
    }
}
