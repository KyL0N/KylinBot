package top.kylinbot.demo.listener;

import catcode.CatCodeUtil;
import love.forte.common.ioc.annotation.Beans;
import love.forte.simbot.annotation.Filter;
import love.forte.simbot.annotation.OnGroup;
import love.forte.simbot.annotation.OnPrivate;
import love.forte.simbot.api.message.events.GroupMsg;
import love.forte.simbot.api.message.events.PrivateMsg;
import love.forte.simbot.api.sender.MsgSender;
import love.forte.simbot.filter.FilterTargets;
import love.forte.simbot.filter.MatchType;

@Beans
public class CommonListener {

    @OnGroup
    @Filter(value = "!help", trim = true, matchType = MatchType.EQUALS)
    @Filter(value = "!帮助", trim = true, matchType = MatchType.EQUALS)
    @Filter(value = "帮助", trim = true, matchType = MatchType.EQUALS)
    @Filter(value = "help", trim = true, matchType = MatchType.EQUALS)
    public void sendHelpMsg(GroupMsg groupMsg, MsgSender msgSender) {
        String helpMsg = new StringBuilder()
                .append("所有命令:\n")
                .append("NSFW图片: ").append(".uestc ").append(".zju ").append(".thu").append("\n")
                .append("牌理:").append("!t ").append("\n  示例:!t 123s1p9p").append("\n")
                .append("osu:\n")
                .append("  绑定:").append("!oauth\n")
                .append("  查询最近打图:").append("!kypr").append("\n")
                .append("  个人信息:").append("!info")
                .toString();
        msgSender.SENDER.sendGroupMsg(groupMsg.getGroupInfo().getGroupCode(), helpMsg);
    }


    @OnPrivate
    @Filter(value = "!help", trim = true, matchType = MatchType.EQUALS)
    @Filter(value = "!帮助", trim = true, matchType = MatchType.EQUALS)
    @Filter(value = "帮助", trim = true, matchType = MatchType.EQUALS)
    @Filter(value = "help", trim = true, matchType = MatchType.EQUALS)
    public void sendHelpMsg(PrivateMsg privateMsg, MsgSender msgSender) {
        String helpMsg = new StringBuilder()
                .append("所有命令:\n")
                .append("NSFW图片:\t").append(".uestc ").append(".zju ").append(".thu").append("\n")
                .append("牌理:").append("!t ").append("\n  示例:!t 123s1p9p").append("\n")
                .append("osu:\n")
                .append("  绑定:").append("!oauth\n")
                .append("  查询最近打图:").append("!kypr").append("\n")
                .append("  个人信息:").append("!info")
                .toString();
        msgSender.SENDER.sendPrivateMsg(privateMsg.getAccountInfo().getAccountCode(), helpMsg);
    }

    @OnPrivate
    @Filter(value = "!poke", trim = true, matchType = MatchType.EQUALS)
    public void sendPoke(PrivateMsg privateMsg, MsgSender msgSender) {
        CatCodeUtil util = CatCodeUtil.INSTANCE;
        String cat1 = util.getStringCodeBuilder("poke", false).key("id").value(9)
                .key("code").value(privateMsg.getAccountInfo().getAccountCode())
                .build();
        msgSender.SENDER.sendPrivateMsg(privateMsg.getAccountInfo().getAccountCode(), cat1);
    }

    @OnGroup
    @Filter(target = FilterTargets.MSG, value = "CAT:nudge", matchType = MatchType.REGEX_FIND)
    public void sendNudge(GroupMsg groupMsg, MsgSender msgSender) {
        CatCodeUtil util = CatCodeUtil.INSTANCE;
        String cat1 = util.getStringCodeBuilder("nudge", false)
                .key("target").value(groupMsg.getAccountInfo().getAccountCode())
                .build();
        msgSender.SENDER.sendGroupMsg(groupMsg.getGroupInfo().getGroupCode(), cat1);
    }

    @OnPrivate
    @Filter(target = FilterTargets.MSG, value = "CAT:nudge", matchType = MatchType.REGEX_FIND)
    public void sendNudge(PrivateMsg privateMsg, MsgSender msgSender) {
        CatCodeUtil util = CatCodeUtil.INSTANCE;
        String cat1 = util.getStringCodeBuilder("nudge", false)
                .key("target").value(privateMsg.getAccountInfo().getAccountCode())
                .build();
        msgSender.SENDER.sendPrivateMsg(privateMsg.getAccountInfo().getAccountCode(), cat1);
    }

}
