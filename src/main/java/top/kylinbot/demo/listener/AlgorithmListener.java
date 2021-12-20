package top.kylinbot.demo.listener;

import io.ktor.utils.io.bits.PrimiteArraysKt;
import love.forte.common.ioc.annotation.Beans;
import love.forte.simbot.annotation.Filter;
import love.forte.simbot.annotation.FilterValue;
import love.forte.simbot.annotation.OnGroup;
import love.forte.simbot.annotation.OnPrivate;
import love.forte.simbot.api.message.events.GroupMsg;
import love.forte.simbot.api.message.events.PrivateMsg;
import love.forte.simbot.api.sender.MsgSender;
import love.forte.simbot.filter.MatchType;
import top.kylinbot.demo.service.AlgorithmService;

@Beans
public class AlgorithmListener {
    AlgorithmService algorithmService = new AlgorithmService();

    @OnPrivate
    @Filter(value = "!alg", matchType = MatchType.STARTS_WITH)
    public void bracket_calculate(PrivateMsg privateMsg, MsgSender sender) {
        String content = privateMsg.getMsgContent().getMsg();
        content = content.replace("!alg","");
//        System.out.println(content);
        int ans = algorithmService.bracketCalculate(content);
        sender.SENDER.sendPrivateMsg(privateMsg.getAccountInfo().getAccountCode(), "答案是" + ans);
    }


    @OnGroup
    @Filter(value = "!alg", matchType = MatchType.STARTS_WITH)
    public void bracket_calculate(GroupMsg groupMsg, MsgSender sender) {
        String content = groupMsg.getMsgContent().getMsg();
        content = content.replace("!alg","");
//        System.out.println(content);
        int ans = algorithmService.bracketCalculate(content);
        sender.SENDER.sendGroupMsg(groupMsg.getGroupInfo().getGroupCode(), "答案是" + ans);
    }
}
