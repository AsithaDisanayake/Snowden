/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package snowden;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author kasun
 */
public class DB {

    private static Connection com;

    public static Connection conect() throws Exception {
        Class.forName("com.mysql.jdbc.Driver").newInstance();
        com = DriverManager.getConnection("jdbc:mysql://localhost:3306/snowden", "root", ""
                + "");
        return com;
    }
    
    
   

    
}