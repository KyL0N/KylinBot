package top.kylinbot.demo.util;

import catcode.CatCodeUtil;
import com.alibaba.fastjson.JSONObject;

import static java.lang.String.valueOf;

public class JsonUtil {

    public static String parseOsuInfoJson(JSONObject json) {
        StringBuilder builder = new StringBuilder();
        String username = json.getString("username");
        String avatar = json.getString("avatar_url");
        JSONObject statics = json.getJSONObject("statistics");
        String level = statics.getJSONObject("level").getString("current");
        String progress = statics.getJSONObject("level").getString("progress");
        String global_rank = statics.getString("global_rank");
        String pp = statics.getString("pp");
        String accuracy = statics.getString("hit_accuracy");
        if (!accuracy.equals("100")) {
            accuracy = accuracy.substring(0, 5);
        }
        String play_count = statics.getString("play_count");
        String country_rank = statics.getString("country_rank");
        long play_time = statics.getLong("play_time") / 3600;
        CatCodeUtil util = CatCodeUtil.INSTANCE;
        avatar = util.getStringTemplate().image(avatar);
        builder.append(username).append("'s").append(" info").append("\n")
                .append(avatar).append("\n")
                .append(pp).append(" pp").append("\n")
                .append(accuracy).append("% acc").append("\n")
                .append(play_count).append(" pc").append("\n")
                .append("全球排名: #").append(global_rank).append("\n")
                .append("国内排名: #").append(country_rank).append("\n")
                .append("游玩时长: ").append(play_time).append("h");
        return builder.toString();
    }
}
