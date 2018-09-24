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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 *
 * @author Stefano Speretta <s.speretta@tudelft.nl>
 */
public class TCPSocketConnector implements SDTLClientConnector
{
    private boolean enabled = true;
    private final String address;
    private final int port;
    private Socket skt = null;
    
    public TCPSocketConnector(String address, int port)
    {
        this.address = address;
        this.port = port;
    }
    
    public void enable()
    {
        enabled = true;
    }
    
    public void disable()
    {
        enabled = false;
    }    

    @Override
    public void connect() throws IOException
    {
        if (enabled)
        {
            skt = new Socket(address, port);
        }
    }
    
    @Override
    public void disconnect() throws IOException
    {
        skt.close();
        skt = null;        
    }

    @Override
    public boolean connected() 
    {
        if (skt!= null)
        {
            return skt.isConnected();
        }
        else
        {
            return false;
        }
    }

    @Override
    public InputStream getInputStream() throws IOException
    {
        if (skt != null)
        {
            return skt.getInputStream();
        }
        else
        {
            throw new IOException("Socket not connected");
        }
    }

    @Override
    public OutputStream getOutputStream() throws IOException
    {
        if (skt != null)
        {
            return skt.getOutputStream();            
        }
        else
        {
            throw new IOException("Socket not connected");
        }
    }    
}
