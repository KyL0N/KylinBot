package top.kylinbot.demo.listener;

import love.forte.common.ioc.annotation.Beans;
import love.forte.simbot.annotation.Filter;
import love.forte.simbot.annotation.FilterValue;
import love.forte.simbot.annotation.OnGroup;
import love.forte.simbot.annotation.OnPrivate;
import love.forte.simbot.api.message.events.GroupMsg;
import love.forte.simbot.api.message.events.PrivateMsg;
import love.forte.simbot.api.sender.MsgSender;
import love.forte.simbot.filter.MatchType;
import top.kylinbot.demo.modle.PPPlusObject;
import top.kylinbot.demo.service.OsuInquireService;
import top.kylinbot.demo.service.OsuService;
import top.kylinbot.demo.util.JsonUtil;
import top.kylinbot.demo.util.MysqlUtil;

@Beans
public class OsuInquireListener extends OsuService {
    OsuInquireService inquireService = new OsuInquireService();

    @OnPrivate
//    @Priority(PriorityConstant.FIRST)
    @Filter(value = "!info", matchType = MatchType.REGEX_MATCHES)
    public void sendPlayerInfo(PrivateMsg privateMsg, MsgSender sender) {
        String qq = privateMsg.getAccountInfo().getAccountCode();
        String osuNickName = MysqlUtil.getOsuIDByQQ(qq);
        if (osuNickName == null) {
            sender.SENDER.sendPrivateMsg(privateMsg.getAccountInfo().getAccountCode(), "尚未绑定, 请发送!oauth以绑定bot");
            return;
        }
        String info = JsonUtil.parseOsuInfoJson(getPlayerOsuInfo(osuNickName));
        sender.SENDER.sendPrivateMsg(privateMsg.getAccountInfo().getAccountCode(), info);
    }

    @OnGroup
//    @Priority(PriorityConstant.FIRST)
    @Filter(value = "!info", matchType = MatchType.REGEX_MATCHES)
    public void sendPlayerInfo(GroupMsg groupMsg, MsgSender sender) {
        String qq = groupMsg.getAccountInfo().getAccountCode();
        String osuNickName = MysqlUtil.getOsuIDByQQ(qq);
        if (osuNickName == null) {
            sender.SENDER.sendGroupMsg(groupMsg.getGroupInfo().getGroupCode(), "尚未绑定, 请发送!oauth以绑定bot");
            return;
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
        sender.SENDER.sendPrivateMsg(privateMsg.getAccountInfo().getAccountCode(), msg);
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
        String msg = inquireService.parsePerformancePoint(beatmap, mods);
        sender.SENDER.sendGroupMsg(groupMsg.getGroupInfo().getGroupCode(), msg);
    }

    @OnPrivate
    @Filter(value = "!kypp {{beatmap,\\d+}} {{mods,\\w+}}", matchType = MatchType.REGEX_MATCHES)
    public void sendBeatmapPerformancePoint(PrivateMsg privateMsg, MsgSender sender, @FilterValue("beatmap") long beatmap, @FilterValue("mods") String mods) {
        String msg = inquireService.parsePerformancePoint(beatmap, mods);
        sender.SENDER.sendPrivateMsg(privateMsg.getAccountInfo().getAccountCode(), msg);

    }

//    @OnGroup
//    @Filter(value = "!kybp", matchType = MatchType.REGEX_MATCHES)
//    public void sendBestBeatmapList(GroupMsg groupMsg, MsgSender senders) {
//        String qq = groupMsg.getAccountInfo().getAccountCode();
//        int id = MysqlUtil.getIDByQQ(qq);
//        String msg = inquireService.parseBestBeatmapList(id);
//        senders.SENDER.sendGroupMsg(groupMsg.getGroupInfo().getGroupCode(), msg);
//    }

//    @OnPrivate
//    @Filter(value = "!kybp", matchType = MatchType.REGEX_MATCHES)
//    public void sendBestBeatmapList(PrivateMsg privateMsg, MsgSender senders) {
//        String qq = privateMsg.getAccountInfo().getAccountCode();
//        int id = MysqlUtil.getIDByQQ(qq);
//        String msg = inquireService.parseBestBeatmapList(id);
//        senders.SENDER.sendPrivateMsg(qq, msg);
//    }

    @OnGroup
    @Filter(value = "!kybp {{best,\\d+}}", matchType = MatchType.REGEX_FIND)
    public void sendBestBeatmap(GroupMsg groupMsg, MsgSender senders, @FilterValue("best") int bp) {
        String qq = groupMsg.getAccountInfo().getAccountCode();
        int id = MysqlUtil.getIDByQQ(qq);
        String msg = null;
        if (groupMsg.getMsgContent().length() == 5) {
            msg = inquireService.parseBestBeatmapList(id);
        } else {
            msg = inquireService.parseBestBeatmapList(id, bp);
        }
        senders.SENDER.sendGroupMsg(groupMsg.getGroupInfo().getGroupCode(), msg);
    }

    @OnPrivate
    @Filter(value = "!kybp {{best,\\d+}}?", trim = true, matchType = MatchType.REGEX_FIND)
    public void sendBestBeatmap(PrivateMsg privateMsg, MsgSender senders, @FilterValue("best") int bp) {
        String qq = privateMsg.getAccountInfo().getAccountCode();
        int id = MysqlUtil.getIDByQQ(qq);
        String msg;
        if (privateMsg.getMsgContent().length() == 5) {
            msg = inquireService.parseBestBeatmapList(id);
        } else {
            msg = inquireService.parseBestBeatmapList(id, bp);
        }
        senders.SENDER.sendPrivateMsg(qq, msg);
    }

    @OnGroup
    @Filter(value = "!kyscore {{bid,\\d+}}", matchType = MatchType.REGEX_MATCHES)
    public void sendBeatmapScore(GroupMsg groupMsg, MsgSender sender, @FilterValue("bid") int bid) {
        String qq = groupMsg.getAccountInfo().getAccountCode();
        int id = MysqlUtil.getIDByQQ(qq);
        String msg = inquireService.parseBeatmapScore(bid, id);
//        System.out.println(msg);
        sender.SENDER.sendGroupMsg(groupMsg.getGroupInfo().getGroupCode(), msg);
    }

    @OnPrivate
    @Filter(value = "!kyscore {{bid,\\d+}}", matchType = MatchType.REGEX_MATCHES)
    public void sendBeatmapScore(PrivateMsg privateMsg, MsgSender sender, @FilterValue("bid") int bid) {
        String qq = privateMsg.getAccountInfo().getAccountCode();
        int id = MysqlUtil.getIDByQQ(qq);
        String msg = inquireService.parseBeatmapScore(bid, id);
//        System.out.println(msg);
        sender.SENDER.sendPrivateMsg(qq, msg);
    }

    @OnGroup
    @Filter(value = ("!kyppv2"), matchType = MatchType.EQUALS)
    public void sendPlayerPPv2(GroupMsg groupMsg, MsgSender sender) {
        String qq = groupMsg.getAccountInfo().getAccountCode();
        String osuID = MysqlUtil.getOsuIDByQQ(qq);
        PPPlusObject ppv2 = new PPPlusObject();
        try {
            ppv2 = getppPlus(osuID);
            sender.SENDER.sendGroupMsg(groupMsg.getGroupInfo().getGroupCode(), ppv2.getAcc().toString());
        } catch (NullPointerException e) {
            e.printStackTrace();
            sender.SENDER.sendGroupMsg(groupMsg.getGroupInfo().getGroupCode(), "获取pp+数据失败");
        }
    }

    @OnPrivate
    @Filter(value = ("!kyppv2"), matchType = MatchType.EQUALS)
    public void sendPlayerPPv2(PrivateMsg privateMsg, MsgSender sender) {
        String qq = privateMsg.getAccountInfo().getAccountCode();
        String osuID = MysqlUtil.getOsuIDByQQ(qq);
        PPPlusObject ppv2 = getppPlus(osuID);
        if (ppv2 == null) {
            sender.SENDER.sendPrivateMsg(qq, "获取pp+数据失败");
        } else {
            sender.SENDER.sendPrivateMsg(qq, "jmp:" + ppv2.getJump().floatValue());
        }
    }


}