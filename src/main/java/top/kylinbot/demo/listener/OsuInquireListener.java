package top.kylinbot.demo.listener;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import love.forte.common.ioc.annotation.Beans;
import love.forte.simbot.annotation.Filter;
import love.forte.simbot.annotation.FilterValue;
import love.forte.simbot.annotation.OnGroup;
import love.forte.simbot.annotation.OnPrivate;
import love.forte.simbot.api.message.events.GroupMsg;
import love.forte.simbot.api.message.events.PrivateMsg;
import love.forte.simbot.api.sender.MsgSender;
import love.forte.simbot.filter.MatchType;
import org.tillerino.osuApiModel.Mods;
import org.tillerino.osuApiModel.types.BitwiseMods;
import top.kylinbot.demo.service.OsuInquireService;
import top.kylinbot.demo.service.OsuService;
import top.kylinbot.demo.util.JsonUtil;
import top.kylinbot.demo.util.MysqlUtil;

@Beans
public class OsuInquireListener extends OsuService {
    OsuInquireService inquireService = new OsuInquireService();


    @OnPrivate
    @Filter(value = "!info", matchType = MatchType.REGEX_MATCHES)
    public void sendPlayerInfo(PrivateMsg privateMsg, MsgSender sender) {
        String qq = privateMsg.getAccountInfo().getAccountCode();
        String osuNickName = MysqlUtil.getOsuIDByQQ(qq);
        if (osuNickName == null) {
            sender.SENDER.sendPrivateMsg(privateMsg.getAccountInfo().getAccountCode(), "尚未绑定, 请发送!oauth以绑定bot");
        }
        String info = JsonUtil.parseOsuInfoJson(getPlayerOsuInfo(osuNickName));
        sender.SENDER.sendPrivateMsg(privateMsg.getAccountInfo().getAccountCode(), info);
    }

    @OnGroup
    @Filter(value = "!info", matchType = MatchType.REGEX_MATCHES)
    public void sendPlayerInfo(GroupMsg groupMsg, MsgSender sender) {
        String qq = groupMsg.getAccountInfo().getAccountCode();
        String osuNickName = MysqlUtil.getOsuIDByQQ(qq);
        if (osuNickName == null) {
            sender.SENDER.sendGroupMsg(groupMsg.getGroupInfo().getGroupCode(), "尚未绑定, 请发送!oauth以绑定bot");
        }
        String info = JsonUtil.parseOsuInfoJson(getPlayerOsuInfo(osuNickName));
        sender.SENDER.sendGroupMsg(groupMsg.getGroupInfo().getGroupCode(), info);
    }

    @OnPrivate
    @Filter(value = "!info", trim = true, matchType = MatchType.STARTS_WITH)
    public void sendOtherPlayerInfo(PrivateMsg privateMsg, MsgSender sender) {
//        String osuNickName = privateMsg.getMsgContent().toString().replace("!info ","");
        if (privateMsg.getMsg().length() == 5) {
            return;
        }
        String osuNickName = privateMsg.getMsg().replace("!info ", "");
        String info = JsonUtil.parseOsuInfoJson(getPlayerOsuInfo(osuNickName));
        sender.SENDER.sendPrivateMsg(privateMsg.getAccountInfo().getAccountCode(), info);
    }

    @OnGroup
    @Filter(value = "!info", trim = true, matchType = MatchType.STARTS_WITH)
    public void sendOtherPlayerInfo(GroupMsg groupMsg, MsgSender sender) {
        if (groupMsg.getMsg().length() == 5) {
//            System.out.println("no name");
            return;
        }
        String osuNickName = groupMsg.getMsg().replace("!info ", "");
        String info = JsonUtil.parseOsuInfoJson(getPlayerOsuInfo(osuNickName));
        sender.SENDER.sendGroupMsg(groupMsg.getGroupInfo().getGroupCode(), info);
    }

    @OnGroup
    @Filter(value = "!kypr", trim = true, matchType = MatchType.STARTS_WITH)
    public void sendPlayerRecentScore(GroupMsg groupMsg, MsgSender sender) {
        String qq = groupMsg.getAccountInfo().getAccountCode();
        String msg = inquireService.parsePlayerRecentScore(qq);
        sender.SENDER.sendGroupMsg(groupMsg.getGroupInfo().getGroupCode(), msg);
    }

    @OnPrivate
    @Filter(value = "!kypr", trim = true, matchType = MatchType.STARTS_WITH)
    public void sendPlayerRecentScore(PrivateMsg privateMsg, MsgSender sender) {
        String qq = privateMsg.getAccountInfo().getAccountCode();
        String msg = inquireService.parsePlayerRecentScore(qq);
        sender.SENDER.sendPrivateMsg(privateMsg.getAccountInfo().getAccountCode(), msg.toString());
    }

    @OnGroup
    @Filter(value = "!kyre", trim = true, matchType = MatchType.STARTS_WITH)
    public void sendPlayerAllRecentScore(GroupMsg groupMsg, MsgSender sender) {
        String qq = groupMsg.getAccountInfo().getAccountCode();
        String msg = inquireService.parsePlayerAllRecentScore(qq);
        sender.SENDER.sendGroupMsg(groupMsg.getGroupInfo().getGroupCode(), msg);
    }

    @OnPrivate
    @Filter(value = "!kyre", trim = true, matchType = MatchType.STARTS_WITH)
    public void sendPlayerAllRecentScore(PrivateMsg privateMsg, MsgSender sender) {
        String qq = privateMsg.getAccountInfo().getAccountCode();
        String msg = inquireService.parsePlayerAllRecentScore(qq);
        sender.SENDER.sendPrivateMsg(privateMsg.getAccountInfo().getAccountCode(), msg);
    }

    @OnGroup
    @Filter(value = "!kypp {{beatmap,\\d+}} {{mods,\\w+}}", matchType = MatchType.REGEX_MATCHES)
    public void sendBeatmapPerformancePoint(GroupMsg groupMsg, MsgSender sender, @FilterValue("beatmap") long beatmap, @FilterValue("mods") String mods) {
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
        JSONObject ppForAcc = beatmapObject.getJSONObject("ppForAcc");
        String diff = beatmapObject.getString("starDiff");
        String acc_93 = ppForAcc.getString("0.93").substring(0, 5);
        String acc_95 = ppForAcc.getString("0.95").substring(0, 5);
        String acc_97 = ppForAcc.getString("0.97").substring(0, 5);
        String acc_98 = ppForAcc.getString("0.98").substring(0, 5);
        String acc_99 = ppForAcc.getString("0.99").substring(0, 5);
        String acc_100 = ppForAcc.getString("1.0").substring(0, 5);
        StringBuilder msg = new StringBuilder();
        msg.append("BID:").append(beatmap).append("\n")
                .append("star:").append(diff).append("\n")
                .append("100%:").append(acc_100).append("\n")
                .append("99%:").append(acc_99).append("\n")
                .append("98%:").append(acc_98).append("\n")
                .append("97%:").append(acc_97).append("\n")
                .append("95%:").append(acc_95).append("\n")
                .append("93%:").append(acc_93).append("\n")
                .append("Mods:").append(mods);
        sender.SENDER.sendGroupMsg(groupMsg.getGroupInfo().getGroupCode(), msg.toString());
    }

    @OnPrivate
    @Filter(value = "!kypp {{beatmap,\\d+}} {{mods,\\w+}}", matchType = MatchType.REGEX_MATCHES)
    public void sendBeatmapPerformancePoint(PrivateMsg privateMsg, MsgSender sender, @FilterValue("beatmap") long beatmap, @FilterValue("mods") String mods) {
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
        JSONObject ppForAcc = beatmapObject.getJSONObject("ppForAcc");
        String diff = beatmapObject.getString("starDiff");
        String acc_93 = ppForAcc.getString("0.93").substring(0, 5);
        String acc_95 = ppForAcc.getString("0.95").substring(0, 5);
        String acc_97 = ppForAcc.getString("0.97").substring(0, 5);
        String acc_98 = ppForAcc.getString("0.98").substring(0, 5);
        String acc_99 = ppForAcc.getString("0.99").substring(0, 5);
        String acc_100 = ppForAcc.getString("1.0").substring(0, 5);
        StringBuilder msg = new StringBuilder();
        msg.append("BID:").append(beatmap).append("\n")
                .append("star:").append(diff).append("\n")
                .append("100%:").append(acc_100).append("\n")
                .append("99%:").append(acc_99).append("\n")
                .append("98%:").append(acc_98).append("\n")
                .append("97%:").append(acc_97).append("\n")
                .append("95%:").append(acc_95).append("\n")
                .append("93%:").append(acc_93).append("\n")
                .append("Mods:").append(mods);
        sender.SENDER.sendPrivateMsg(privateMsg.getAccountInfo().getAccountCode(), msg.toString());

    }

}
                                                                                  