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

import SDTL.Protocol.TransportFrame;

/**
 *
 * @author Stefano Speretta <s.speretta@tudelft.nl>
 */
public abstract class TransportConnector 
{
    public abstract void registerReceiveHandler(ReceiveHandler rx);
    
    public abstract void send(TransportFrame tf);       
            
    public abstract void connect();
    
    public abstract void disconnect();
    
    public abstract boolean connected();

}
