package top.kylinbot.demo.controller;

import top.kylinbot.demo.modle.osuUser;
import top.kylinbot.demo.util.MysqlUtil;

import java.sql.*;

public class MysqlServer {

    public MysqlServer() {
        String query = "SELECT VERSION()";

        try (Connection con = MysqlUtil.getConnect();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(query)) {

            if (rs.next()) {
                System.out.println("Success enter mysql\nversion:"+rs.getString(1));
            }

        } catch (SQLException ex) {
            System.out.println("Failed");
        }
    }

    public String getUserCode(osuUser osuUser) {
        String query = "SELECT * FROM osu WHERE qq = '" + osuUser.getQQ() + "'";
        try (Connection con = MysqlUtil.getConnect();
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
