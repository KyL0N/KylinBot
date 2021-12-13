package top.kylinbot.demo.service;

import com.alibaba.fastjson.JSONObject;
import top.kylinbot.demo.util.JsonUtil;

public class OsuInquireService extends OsuService {

    public static String recent(String osuNickName) {
        JSONObject testInfo = getPlayerOsuInfo(osuNickName);
        return JsonUtil.parseOsuInfoJson(testInfo);
    }
}
