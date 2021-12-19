package top.kylinbot.demo.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.tillerino.osuApiModel.Mods;
import org.tillerino.osuApiModel.types.BitwiseMods;
import top.kylinbot.demo.util.MysqlUtil;

public class OsuInquireService extends OsuService {

    public OsuInquireService() {

    }

    public String parseRecentScoreJson(JSONArray js) {
        JSONObject object;
        try {
            object = js.getJSONObject(0);
        } catch (IndexOutOfBoundsException e) {
            return "你最近还没有打过图呢";
        }
        JSONObject statistics = object.getJSONObject("statistics");
        JSONObject beatmap = object.getJSONObject("beatmap");
        long bid = beatmap.getLong("id");
        Double AR = beatmap.getDoubleValue("ar");
        Double OD = beatmap.getDoubleValue("accuracy");
        Double BPM = beatmap.getDoubleValue("bpm");
        Double HP = beatmap.getDoubleValue("drain");
        Double CS = beatmap.getDoubleValue("cs");

        JSONObject beatmapObject = getMapPerformancePoint(bid, 0);
        String ppList = getBeatmapPP(beatmapObject);
        StringBuilder msg = new StringBuilder();
        msg.append("BID:").append(bid).append("\n")
                .append("CS").append(CS).append(" AR").append(AR).append(" OD").append(OD).append(" HP").append(HP).append("\n")
                .append("BPM").append(BPM).append("\n")
                .append(ppList).append("\n")
                .append("Mods:").append("None");
        JSONArray mods = object.getJSONArray("mods");
        return msg.toString();
    }

    public String parsePlayerRecentScore(String qq) {
        int id = MysqlUtil.getIDByQQ(qq);
        JSONArray js = getOsuRecent(id, 0, 1);
        return parseRecentScoreJson(js);
    }

    public String parsePlayerAllRecentScore(String qq) {
        int id = MysqlUtil.getIDByQQ(qq);
        JSONArray js = getOsuAllRecent(id, 0, 1);
        return parseRecentScoreJson(js);
    }

    public String parsePerformancePoint(long beatmap, String mods) {
        @BitwiseMods long mod = 0;
        if (mods.contains("NF")) {
            mod = Mods.add(mod, Mods.NoFail);
        }
        if (mods.contains("EZ")) {
            mod = Mods.add(mod, Mods.Easy);
        }
        if (mods.contains("HD")) {
            mod = Mods.add(mod, Mods.Hidden);
        }
        if (mods.contains("HR")) {
            mod = Mods.add(mod, Mods.HardRock);
        }
        if (mods.contains("DT")) {
            mod = Mods.add(mod, Mods.DoubleTime);
        }
        if (mods.contains("HT")) {
            mod = Mods.add(mod, Mods.HalfTime);
        }
        if (mods.contains("NC")) {
            mod = Mods.add(mod, Mods.Nightcore);
        }
        if (mods.contains("FL")) {
            mod = Mods.add(mod, Mods.Flashlight);
        }
        if (mods.contains("SO")) {
            mod = Mods.add(mod, Mods.SpunOut);
        }
        JSONObject beatmapObject = getMapPerformancePoint(beatmap, mod);
        String ppList = getBeatmapPP(beatmapObject);
        StringBuilder msg = new StringBuilder();
        msg.append("BID:").append(beatmap).append("\n")
                .append(ppList).append("\n")
                .append("Mods:").append(mods);
        return msg.toString();
    }

    public String getBeatmapPP(JSONObject beatmapObject) {
        StringBuilder pp = new StringBuilder();
        JSONObject ppForAcc = beatmapObject.getJSONObject("ppForAcc");
        String diff = beatmapObject.getString("starDiff");
        String acc_93 = ppForAcc.getString("0.93").substring(0, 5);
        String acc_95 = ppForAcc.getString("0.95").substring(0, 5);
        String acc_97 = ppForAcc.getString("0.97").substring(0, 5);
        String acc_98 = ppForAcc.getString("0.98").substring(0, 5);
        String acc_99 = ppForAcc.getString("0.99").substring(0, 5);
        String acc_100 = ppForAcc.getString("1.0").substring(0, 5);
        pp.append("star:").append(diff).append("\n")
                .append("100%:").append(acc_100).append("\n")
                .append("99%:").append(acc_99).append("\n")
                .append("98%:").append(acc_98).append("\n")
                .append("97%:").append(acc_97).append("\n")
                .append("95%:").append(acc_95).append("\n")
                .append("93%:").append(acc_93);
        return pp.toString();
    }

}
