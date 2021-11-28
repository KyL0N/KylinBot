package top.kylinbot.demo.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class mysqlServer {
    public mysqlServer(){
        String url = "jdbc:mysql://localhost:3306/osuDB?useSSL=false";
        String user = "admin";
        String password = "admin0";

        String query = "SELECT VERSION()";

        try (Connection con = DriverManager.getConnection(url, user, password);
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(query)) {

            if (rs.next()) {

                System.out.println(rs.getString(1));
                System.out.println("mysql test success");
            }

        } catch (SQLException ex) {
//            Logger lgr = Logger.getLogger(JdbcMySQLVersion.class.getName());
//            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }
}
