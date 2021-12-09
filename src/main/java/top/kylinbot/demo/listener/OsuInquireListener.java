package top.kylinbot.demo.listener;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import love.forte.common.ioc.annotation.Beans;
import love.forte.simbot.annotation.Filter;
import love.forte.simbot.annotation.OnGroup;
import love.forte.simbot.annotation.OnPrivate;
import love.forte.simbot.api.message.events.GroupMsg;
import love.forte.simbot.api.message.events.PrivateMsg;
import love.forte.simbot.api.sender.MsgSender;
import love.forte.simbot.filter.MatchType;
import top.kylinbot.demo.modle.osuScore.Beatmap;
import top.kylinbot.demo.modle.osuScore.BeatmapImpl;
import top.kylinbot.demo.modle.osuScore.OsuApiBeatmapForDiff;
import top.kylinbot.demo.modle.osuScore.OsuScore;
import top.kylinbot.demo.service.OsuService;
import top.kylinbot.demo.util.JsonUtil;
import top.kylinbot.demo.util.MysqlUtil;

@Beans
public class OsuInquireListener extends OsuService {

    @OnPrivate
    @Filter(value = "!info", trim = true, matchType = MatchType.EQUALS)
    public void sendPlayerInfo(PrivateMsg privateMsg, MsgSender sender) {
        String qq = privateMsg.getAccountInfo().getAccountCode();
        String osuNickName = MysqlUtil.getOsuIDByQQ(qq);
        if (osuNickName == null) {
            sender.SENDER.sendPrivateMsg(privateMsg.getAccountInfo().getAccountCode(), "尚未绑定, 请发送!oauth以绑定bot");
        }
        JSONObject testInfo = getPlayerOsuInfo(osuNickName);
        String info = JsonUtil.parseOsuInfoJson(testInfo);
        sender.SENDER.sendPrivateMsg(privateMsg.getAccountInfo().getAccountCode(), info);
    }

    @OnGroup
    @Filter(value = "!info", trim = true, matchType = MatchType.EQUALS)
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
        String osuNickName = privateMsg.getMsg().replace("!info ", "");
        String info = JsonUtil.parseOsuInfoJson(getPlayerOsuInfo(osuNickName));
        sender.SENDER.sendPrivateMsg(privateMsg.getAccountInfo().getAccountCode(), info);
    }

    @OnGroup
    @Filter(value = "!info", trim = true, matchType = MatchType.STARTS_WITH)
    public void sendOtherPlayerInfo(GroupMsg groupMsg, MsgSender sender) {
        String osuNickName = groupMsg.getMsg().replace("!info ", "");
        String info = JsonUtil.parseOsuInfoJson(getPlayerOsuInfo(osuNickName));
        sender.SENDER.sendGroupMsg(groupMsg.getGroupInfo().getGroupCode(), info);
    }

    @OnGroup
    @Filter(value = "!kyre", trim = true, matchType = MatchType.STARTS_WITH)
    public void sendPlayerRecentScore(GroupMsg groupMsg, MsgSender sender) {
        String qq = groupMsg.getAccountInfo().getAccountCode();
        int id = MysqlUtil.getIDByQQ(qq);//向数据库查询用户id,
        JSONArray js = getOsuRecent(id, 0, 1);//通过用户id获取最近打图
        JSONObject object = js.getJSONObject(0);
        JSONObject statistics = object.getJSONObject("statistics");
        int count_miss = statistics.getIntValue("count_miss");
        int count_50 = statistics.getIntValue("count_50");
        int count_100 = statistics.getIntValue("count_100");
        int count_300 = statistics.getIntValue("count_300");
        int max_combo = object.getIntValue("max_combo");
        JSONArray mods = object.getJSONArray("mods");
        OsuScore score = new OsuScore(max_combo,count_300,count_100,count_50,count_miss,0);
        OsuApiBeatmapForDiff beatmap = new OsuApiBeatmapForDiff();

        score.getPP((Beatmap) beatmap);
        System.out.println(count_miss + "\n" + count_50 + "\n" + count_100
                + "\n" + count_300 + "\n" + max_combo + "\n" + mods.toString());
        sender.SENDER.sendGroupMsg(groupMsg.getGroupInfo().getGroupCode(), "1");
    }
}
                                                                                  