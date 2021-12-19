package top.kylinbot.demo.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import top.kylinbot.demo.util.MysqlUtil;

public class OsuInquireService extends OsuService {

    public OsuInquireService(){

    }

    public String parseRecentScoreJson(JSONArray js){
        JSONObject object = js.getJSONObject(0);
        JSONObject statistics = object.getJSONObject("statistics");
        JSONObject beatmap = object.getJSONObject("beatmap");
        long bid = beatmap.getLong("id");
        Double AR = beatmap.getDoubleValue("ar");
        Double OD = beatmap.getDoubleValue("accuracy");
        Double BPM = beatmap.getDoubleValue("bpm");
        Double HP = beatmap.getDoubleValue("drain");
        Double CS = beatmap.getDoubleValue("cs");

        JSONObject beatmapObject = getMapPerformancePoint(bid, 0);
        JSONObject ppForAcc = beatmapObject.getJSONObject("ppForAcc");
        String diff = beatmapObject.getString("starDiff");
        String acc_93 = ppForAcc.getString("0.93").substring(0, 5);
        String acc_95 = ppForAcc.getString("0.95").substring(0, 5);
        String acc_97 = ppForAcc.getString("0.97").substring(0, 5);
        String acc_98 = ppForAcc.getString("0.98").substring(0, 5);
        String acc_99 = ppForAcc.getString("0.99").substring(0, 5);
        String acc_100 = ppForAcc.getString("1.0").substring(0, 5);
        StringBuilder msg = new StringBuilder();
        msg.append("BID:").append(bid).append("\n")
                .append("CS").append(CS).append(" AR").append(AR).append(" OD").append(OD).append(" HP").append(HP).append("\n")
                .append("BPM").append(BPM)
                .append("star:").append(diff).append("\n")
                .append("100%:").append(acc_100).append("\n")
                .append("99%:").append(acc_99).append("\n")
                .append("98%:").append(acc_98).append("\n")
                .append("97%:").append(acc_97).append("\n")
                .append("95%:").append(acc_95).append("\n")
                .append("93%:").append(acc_93).append("\n")
                .append("Mods:").append("None");
        JSONArray mods = object.getJSONArray("mods");
        return msg.toString();
    }

    public String parsePlayerRecentScore(String qq) {
        int id = MysqlUtil.getIDByQQ(qq);
        JSONArray js = getOsuRecent(id, 0, 1);
        return parseRecentScoreJson(js);
    }

    public String parsePlayerAllRecentScore(String qq){
        int id = MysqlUtil.getIDByQQ(qq);
        JSONArray js = getOsuAllRecent(id, 0, 1);
        return parseRecentScoreJson(js);
    }
}
