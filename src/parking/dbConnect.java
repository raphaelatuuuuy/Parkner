package parking;
import java.sql.*;

public class dbConnect {
    private static String username = "root";
    private static String password = "root";
    private static String con = "jdbc:mysql://localhost:3307/parking";
    
    public static Connection dbcon(){
        Connection conn = null;
        
        try{
            conn = DriverManager.getConnection(con, username, password);
            System.out.println("You have successfully connected to the database.");
        } catch(SQLException e){
            System.err.println(e);
        }
        return conn;
    }
}

// TITE