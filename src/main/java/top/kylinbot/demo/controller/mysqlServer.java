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
                System.out.println("Success enter mysql\nversion:"+rs.getString(1));
            }

        } catch (SQLException ex) {
            System.out.println("Failed");
//            Logger lgr = Logger.getLogger(JdbcMySQLVersion.class.getName());
//            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    public String getUserCode(osuUser osuUser) {
        String url = "jdbc:mysql://localhost:3306/osuDB?useSSL=false";
        String user = "admin";
        String password = "admin0";
        String query = "SELECT * FROM osu WHERE qq = '" + osuUser.getQQ() + "'";
        try (Connection con = DriverManager.getConnection(url, user, password);
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(query)) {
            if (rs.next()) {
                int qq = Integer.parseInt(rs.getString(1));
                String osuID = rs.getString(2);
                String token = rs.getString(3);

                osuUser.setQQ(qq);
                osuUser.setCode(token);
                osuUser.setOsuID(osuID);
                //获取第一列qq
                System.out.println("qq:" + qq);
                //获取第二列osuID
                System.out.println("osuID:" + osuID);
                //获取第三列token
                System.out.println("token:" + token);

                System.out.println("Success");
                return rs.getString(3);
            }
        } catch (SQLException ex) {
            System.out.println("Failed");

        }
        return null;
    }



}
