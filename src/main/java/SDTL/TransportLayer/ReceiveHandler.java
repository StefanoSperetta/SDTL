/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SDTL.TransportLayer;

import SDTL.Protocol.TransportFrame;

/**
 *
 * @author Stefano Speretta <s.speretta@tudelft.nl>
 */
public interface ReceiveHandler 
{
    public void messageReceived(TransportFrame tf);
}
