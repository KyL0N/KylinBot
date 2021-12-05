package top.kylinbot.demo.util;

import top.kylinbot.demo.modle.osuUser;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;


public class MysqlUtil {
    private static String url;
    private static String user;
    private static String password;

    static {
        try {
            Properties props = new Properties();
            InputStream inputStream = MysqlUtil.class.getClassLoader().getResourceAsStream("KylinBot.properties");
            props.load(inputStream);
            url = props.getProperty("MysqlUrl");
            user = props.getProperty("MysqlUser");
            password = props.getProperty("MysqlPassword");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


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
//            System.out.println(query);
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
                    + osuPlayer.getOsuID() + "','" + osuPlayer.getOsuID() + "')";
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

    public static String getOsuIDByQQ(String qq) {
        try {
            Connection con = getConnect();
            String query = "SELECT osuID FROM osuDB.osu WHERE qq = '" + qq + "'";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query);
            if (rs.next()) {
//                System.out.println(rs.getString(1));
                return rs.getString(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static String getUserCode(osuUser osuUser) {
        String query = "SELECT * FROM osu WHERE qq = '" + osuUser.getQQ() + "'";
        try (Connection con = MysqlUtil.getConnect();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(query)) {
            if (rs.next()) {
                int qq = Integer.parseInt(rs.getString(1));
                String osuID = rs.getString(2);
                String token = rs.getString(3);

                osuUser.setQQ(qq);
                osuUser.setRefreshToken(token);
                osuUser.setOsuID(osuID);
                //获取第一列qq
                System.out.println("qq:" + qq);
                //获取第二列osuID
                System.out.println("osuID:" + osuID);
                //获取第三列token
                System.out.println("token:" + token);

                System.out.println("Success");
                st.close();
                return rs.getString(3);
            }
        } catch (SQLException ex) {
            System.out.println("Failed");

        }
        return null;
    }


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
            ps.close();
            con.close();
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
