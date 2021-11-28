package top.kylinbot.demo.controller;

import top.kylinbot.demo.modle.osuUser;

import java.sql.*;

public class mysqlServer {

    public mysqlServer() {
        String url = "jdbc:mysql://localhost:3306/osuDB?useSSL=false";
        String user = "admin";
        String password = "admin0";

        String query = "SELECT VERSION()";

        try (Connection con = DriverManager.getConnection(url, user, password);
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(query)) {

            if (rs.next()) {
                System.out.println(rs.getString(1));
                System.out.println("Success");
            }

        } catch (SQLException ex) {
            System.out.println("Failed");
//            Logger lgr = Logger.getLogger(JdbcMySQLVersion.class.getName());
//            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    public void getUserCode(osuUser osuUser) {
        String url = "jdbc:mysql://localhost:3306/osuDB?useSSL=false";
        String user = "admin";
        String password = "admin0";
//        String query = "SELECT INSERT INTO osuUer(" +
//                osuUser.getQQ() + "," + osuUser.getOsuID() + "," + osuUser.getCode() +
//                ");";
        String query = "SELECT * FROM osuUser;";
        try (Connection con = DriverManager.getConnection(url, user, password);
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(query)) {
            if (rs.next()) {
                //获取第一列qq
                System.out.println("qq:"+rs.getString(1));
                //获取第二列osuID
                System.out.println("osuID"+rs.getString(2));
                //获取第三列token
                System.out.println("token:"+rs.getString(3));

                System.out.println("Success");
//                return rs.getString(1);
            }
        } catch (SQLException ex) {
            System.out.println("Failed");

        }
//        return null;
    }

}
