/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SDTL.TransportLayer;

import SDTL.Protocol.AckFrame;
import SDTL.Protocol.TransportFrame;
import java.io.IOException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Stefano Speretta <s.speretta@tudelft.nl>
 */
public class DebugConnector extends TransportConnector
{
    private boolean connected = false;
    private boolean enabled = true;
    private ReceiveHandler rx =  null;
    
    public void enable()
    {
        enabled = true;
    }
    
    public void disable()
    {
        enabled = false;
    }
    
    @Override
    public void send(final TransportFrame tf) 
    {
        System.out.println("Sending " + tf.toString());
        System.out.println("Received hash " + tf.hashCode());
        
        // sending an acknowledge
        new Thread(new Runnable()
        {
            @Override
            public void run() 
            {
                try 
                {
                    Thread.sleep(1000);
                    AckFrame a = AckFrame.newBuilder()
                            .setHash(tf.hashCode())
                            .setTimestamp(new Date().getTime())
                            .setAck(true)
                            .build();
                    TransportFrame t = TransportFrame.newBuilder()
                            .setID(TransportID.ACK)
                            .setPayload(a.toByteBuffer())
                            .build();
                    sendMessage(t);
                } catch (IOException | InterruptedException ex) 
                {
                    Logger.getLogger(DebugConnector.class.getName()).log(Level.SEVERE, null, ex);
                }
            }            
            }).start();
    }

    @Override
    public void connect() 
    {
        System.out.println("Connect " + enabled);
        connected = enabled;
    }
    
    @Override
    public void disconnect() 
    {
        System.out.println("Disconnect");
        connected = false;
    }

    @Override
    public boolean connected() 
    {
        return connected;
    }
    
    public void sendMessage(TransportFrame tf)
    {
        System.out.println("sendMessage");
        if (rx != null)
        {
            rx.messageReceived(tf);
        }
    }

    @Override
    public void registerReceiveHandler(ReceiveHandler rx) 
    {
        this.rx = rx;
    }
    
}
