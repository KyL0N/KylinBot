package top.kylinbot.demo.util;

import top.kylinbot.demo.modle.osuUser;

import java.sql.*;


public class MysqlUtil {
    private static String url = "jdbc:mysql://kyl1n.top:3306/osuDB?useSSL=false";
    private static String user = "admin";
    private static String password = "admin0";

    /**
     * write userinfo into mysql schema osuDB.osu
     *
     * @param osuPlayer userinfo
     */
    public static void writeUser(osuUser osuPlayer) {
        try {
            Connection con = getConnect();
            String query = "INSERT INTO osuDB.osu VALUES ('" + osuPlayer.getQQ() + "','"
                    + osuPlayer.getOsuID() + "','" + osuPlayer.getAccessToken() + "','"
                    + osuPlayer.getRefreshToken() + "','" + osuPlayer.getExpire() + "')";
            System.out.println(query);
            PreparedStatement st = con.prepareStatement(query);
            st.executeUpdate();
            System.out.println("executeSuccess");
        } catch (SQLException ex) {
            System.out.println("executeFailed");
        }
    }

    public static void updateUser(osuUser osuPlayer) {
        try {
            Connection con = getConnect();
            String query = "UPDATE osuDB.osu SET ('" + osuPlayer.getQQ() + "','"
                    + osuPlayer.getOsuID() + "','" + osuPlayer.getRefreshToken() + "','"
                    + osuPlayer.getOsuID() + "','" + osuPlayer.getOsuID()        + "')";
            System.out.println(query);
            PreparedStatement st = con.prepareStatement(query);
            st.executeUpdate();
            System.out.println("executeSuccess");
        } catch (SQLException ex) {
            System.out.println("executeFailed");
        }
    }

//    UPDATE osuDB.osu t
//    SET t.osuID = '1'
//    WHERE t.qq = 0
    /**
     * test class for using Mysql
     */
    public static void testConnect() {
        try {
            Connection con = getConnect();
            osuUser osuPlayer = new osuUser(1579525246, null);
//            String query = "INSERT INTO osuDB.osu VALUES ('" + osuPlayer.getQQ() + "','" + osuPlayer.getOsuID() + "','" + osuPlayer.getCode() + "')";
            String query = "SELECT * FROM osu WHERE qq = '" + osuPlayer.getQQ() + "'";
            PreparedStatement ps = con.prepareStatement(query);
            System.out.println(ps.toString());
            ps.executeQuery();
            System.out.println("Success");
        } catch (Exception e) {
            System.out.println("Fail");
            e.printStackTrace();
        }
    }

    public static Connection getConnect() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(url, user, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connection;
    }
}
