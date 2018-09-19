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
import java.io.IOException;
import java.nio.ByteBuffer;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Stefano Speretta <s.speretta@tudelft.nl>
 */
public class TransportBuffer 
{
    private final Connection conn;
    
    public TransportBuffer(String filePath) throws ClassNotFoundException, SQLException
    {

        String driver = "org.apache.derby.jdbc.EmbeddedDriver";
        String connectionURL = "jdbc:derby:" + filePath + ";create=true";
        Class.forName(driver);
        conn = DriverManager.getConnection(connectionURL);  
        
        // ensure the table already exists, otherwise create it
        createTable();         
    }  
    
    private void createTable() throws SQLException
    {
        try
        {
            String DDL = "CREATE TABLE storage (" +
                            "ID int NOT NULL PRIMARY KEY " +
                            "GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1)," +
                            "Hash integer NOT NULL," +
                            "Data BLOB)";

            try (Statement stmnt = conn.createStatement()) 
            {
                stmnt.executeUpdate(DDL);
            }
        } catch (SQLException ex)
        {
            // if the error is not "Table already exists", re-throw the exception
            if (!ex.getSQLState().equals("X0Y32"))
            {                
                throw ex;
            }
        }
    }
    
    public int getCount() throws SQLException
    {
        Statement stmnt = conn.createStatement();
        try (ResultSet rs = stmnt.executeQuery("SELECT COUNT(ID) FROM storage"))
        {
            if (rs.next())
            {
                return rs.getInt(1);
            }
        }
        return 0;
    }
    
    public void insertFrame(TransportFrame f) throws IOException, SQLException
    {
        int hash = f.hashCode();
        PreparedStatement stmnt = conn.prepareStatement("INSERT INTO storage (Hash, Data) VALUES (?, ?)");
        stmnt.setInt(1, hash);
        stmnt.setBytes(2, f.toByteBuffer().array());
        stmnt.execute();
    }
    
    public TransportFrame getFrame() throws IOException, SQLException
    {
        Statement stmnt = conn.createStatement();
        try (ResultSet rs = stmnt.executeQuery("SELECT * FROM storage ORDER BY ID ASC FETCH FIRST 1 ROW ONLY"))
        {
            while (rs.next())
            {
                System.out.println(rs.getInt("ID"));
                byte[] data = rs.getBlob("Data").getBinaryStream().readAllBytes();
                TransportFrame f = TransportFrame.fromByteBuffer(ByteBuffer.wrap(data));
                return f;
            }
        }
        return null;        
    }
    
    public TransportFrame getFrame(int hash) throws IOException, SQLException
    {
        Statement stmnt = conn.createStatement();
        try (ResultSet rs = stmnt.executeQuery("SELECT * FROM storage WHERE Hash = " + hash + " FETCH FIRST 1 ROW ONLY"))
        {
            while (rs.next())
            {
                System.out.println(rs.getInt("ID"));
                byte[] data = rs.getBlob("Data").getBinaryStream().readAllBytes();
                TransportFrame f = TransportFrame.fromByteBuffer(ByteBuffer.wrap(data));
                return f;
            }
        }
        return null;        
    }
    
    public void remove(int hash) throws SQLException
    {
        Statement stmnt = conn.createStatement();
        stmnt.executeUpdate("DELETE FROM storage WHERE Hash = " + hash);
        
    }
    
    public void cleanDB() throws SQLException
    {
        Statement stmnt = conn.createStatement();
        stmnt.executeUpdate("DELETE FROM storage");        
    }
}