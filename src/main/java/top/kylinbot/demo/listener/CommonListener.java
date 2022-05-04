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
import org.apache.logging.log4j.util.ProcessIdUtil;
import top.kylinbot.demo.service.CommonService;


@Beans
public class CommonListener {

    @OnGroup
    @Filter(value = "!help", trim = true, matchType = MatchType.EQUALS)
    @Filter(value = "!帮助", trim = true, matchType = MatchType.EQUALS)
    @Filter(value = "帮助", trim = true, matchType = MatchType.EQUALS)
    @Filter(value = "help", trim = true, matchType = MatchType.EQUALS)
    public void sendHelpMsg(GroupMsg groupMsg, MsgSender msgSender) {
        String helpMsg = CommonService.getHelpString();
        msgSender.SENDER.sendGroupMsg(groupMsg.getGroupInfo().getGroupCode(), helpMsg);
    }


    @OnPrivate
    @Filter(value = "!help", trim = true, matchType = MatchType.EQUALS)
    @Filter(value = "!帮助", trim = true, matchType = MatchType.EQUALS)
    @Filter(value = "帮助", trim = true, matchType = MatchType.EQUALS)
    @Filter(value = "help", trim = true, matchType = MatchType.EQUALS)
    public void sendHelpMsg(PrivateMsg privateMsg, MsgSender msgSender) {
        String helpMsg = CommonService.getHelpString();
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
        String cat1 = CommonService.getNudge(groupMsg.getAccountInfo().getAccountCode());
        msgSender.SENDER.sendGroupMsg(groupMsg.getGroupInfo().getGroupCode(), cat1);
    }

    @OnPrivate
    @Filter(target = FilterTargets.MSG, value = "CAT:nudge", matchType = MatchType.REGEX_FIND)
    public void sendNudge(PrivateMsg privateMsg, MsgSender msgSender) {
        String cat1 = CommonService.getNudge(privateMsg.getAccountInfo().getAccountCode());
        msgSender.SENDER.sendPrivateMsg(privateMsg.getAccountInfo().getAccountCode(), cat1);
    }

    @OnPrivate
    @Filter(value = "!pid", matchType = MatchType.EQUALS)
    public void sendPID(PrivateMsg privateMsg, MsgSender msgSender) {
        msgSender.SENDER.sendPrivateMsg(privateMsg.getAccountInfo().getAccountCode(), ProcessIdUtil.getProcessId());
    }

    @OnGroup
    @Filter(value = "!pid", matchType = MatchType.EQUALS)
    public void sendPID(GroupMsg groupMsg, MsgSender msgSender) {
        msgSender.SENDER.sendPrivateMsg(groupMsg.getGroupInfo().getGroupCode(), ProcessIdUtil.getProcessId());
    }

}
