package top.kylinbot.demo.util;

//import sun.java2d.marlin.Version;

import top.kylinbot.demo.modle.osuUser;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MysqlUtil {
    private static String url = "jdbc:mysql://kyl1n.top:3306/osuDB?useSSL=false";
    private static String user = "admin";
    private static String password = "admin0";
    private static String schema = "osuUser";


    public static void writeUser(osuUser osuPlayer) {


        try {
            Connection con = DriverManager.getConnection(url, user, password);
            String query = "INSERT INTO osuDB.osu VALUES ('" + osuPlayer.getQQ() + "','" + osuPlayer.getOsuID() + "','" + osuPlayer.getCode() + "')";
            System.out.println(query);
            PreparedStatement st = con.prepareStatement(query);

//            st.setInt(1, osuPlayer.getQQ());
//            st.setString(2, osuPlayer.getOsuID());
//            st.setString(3, osuPlayer.getRefreshToken());

            st.executeUpdate();
            System.out.println("Success");

        } catch (SQLException ex) {
            System.out.println("Failed");
//            Logger lgr = Logger.getLogger(Version.class.getName());
//            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }

    }


    public static void testConnect() {
        try {
            Connection con = DriverManager.getConnection(url, user, password);

            osuUser osuPlayer = new osuUser(1579525246, "unknown ID", "def5020061015f83ca35da5b34e1b4ce83f08904de24a3d2ce61f328e12c0543fc7003c54c0d72f4e1d85496c86104a8b3cf44e230874f42c87cb81a08c075955751b682798d86bdac51c658cc3b3568a86b09a95ca7204c6efa794f3d4f31462040e91e018dbfb061ad84ab13cbd549f90fa25222efdf07eff71c91836a45e1d0c110939daec869aaacd76c1b53f3d43b84eae24ecca21350fb4fd31dd190638756b6231f4cd3280d4fe4e50d6e43be458dcfa73d378eddc00aa56ba3d57b3b6373306e433156d7acae3c8ae039b25812e1460f18a6bd22a49e0663df01de1c8f488c1d5c8c0cc3b5358385e20f800494f9f6ab056384e094600f8ebb9933e37edbf939ee5fcd278ecc9320e9b82f2ab7d6c4c21d3b00e1955c0987e4e50f8a1f3eb766807e58f3e38d37646c81e335638c37beb8b38181b11444471f24337a8712fbd32e9219fe1e3383a159058cbbf4cdf0a5701722ed5a9af6b81dae38a113c48b581b5eea7cac57150fb4e69b4183f09aeb065761391d0c9810d5b315c2c3e86bb6715ebfa515bb00a4fc43"
                    , null, null, 0);
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
