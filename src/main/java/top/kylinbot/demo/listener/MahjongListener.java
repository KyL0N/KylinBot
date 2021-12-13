package top.kylinbot.demo.listener;

import love.forte.common.ioc.annotation.Beans;
import love.forte.simbot.annotation.Filter;
import love.forte.simbot.annotation.OnGroup;
import love.forte.simbot.annotation.OnPrivate;
import love.forte.simbot.api.message.events.GroupMsg;
import love.forte.simbot.api.message.events.PrivateMsg;
import love.forte.simbot.api.sender.MsgSender;
import love.forte.simbot.filter.MatchType;
import top.kylinbot.demo.util.HtmlUtil;

@Beans
public class MahjongListener {
    private static final String PHONE_GROUP = "==tellMeYourNameAndPhone==PHONE==";
    private static final String NAME_GROUP = "==tellMeYourNameAndPhone==NAME==";

    /**
     * @param groupMsg 手牌信息
     * @param sender   传输sender
     * @throws Exception 获取html的错误信息
     */
    @OnGroup
    @Filter(value = "!t", matchType = MatchType.STARTS_WITH)
    public void sendTenHouResult(GroupMsg groupMsg, MsgSender sender) {
        String tiles = groupMsg.getMsg().replace("!t ", "");
        String result = null;
        try {
            result = HtmlUtil.getTenHouResult(tiles);
        } catch (Exception e) {
            return;
        }
        sender.SENDER.sendGroupMsg(groupMsg.getGroupInfo().getGroupCode(), result);
    }

    /**
     * @param privateMsg 手牌信息
     * @param sender     传输sender
     */
    @OnPrivate
    @Filter(value = "!t", trim = true, matchType = MatchType.STARTS_WITH)
    public void sendTenHouResult(PrivateMsg privateMsg, MsgSender sender) {
        String tiles = privateMsg.getMsg().replace("!t ", "");
        String result = null;
        try {
            result = HtmlUtil.getTenHouResult(tiles);
        } catch (Exception e) {
            return;
        }
        sender.SENDER.sendPrivateMsg(privateMsg.getAccountInfo().getAccountCode(), result);
    }


}
