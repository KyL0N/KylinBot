package top.kylinbot.demo.listener;

import love.forte.common.ioc.annotation.Beans;
import love.forte.simbot.annotation.Filter;
import love.forte.simbot.annotation.OnGroup;
import love.forte.simbot.annotation.OnPrivate;
import love.forte.simbot.api.message.events.GroupMsg;
import love.forte.simbot.api.message.events.PrivateMsg;
import love.forte.simbot.api.sender.MsgSender;
import love.forte.simbot.filter.MatchType;
import top.kylinbot.demo.modle.osuUser;
import top.kylinbot.demo.service.OsuBindService;
import top.kylinbot.demo.service.OsuService;
import top.kylinbot.demo.util.MysqlUtil;


@Beans
public class OsuOauthListener extends OsuService {

    @OnPrivate
    @Filter(value = "!oauth", trim = true, matchType = MatchType.EQUALS)
    public void sendOauth(PrivateMsg privateMsg, MsgSender sender) {
        String accountCode = privateMsg.getAccountInfo().getAccountCode();
        long qq = Long.parseLong(accountCode);
        String url = getOauthUrl(privateMsg.getAccountInfo().getAccountCode());
        sender.SENDER.sendPrivateMsg(accountCode, url);
        sender.SENDER.sendPrivateMsg(accountCode, "请在2分钟内完成绑定操作");
        osuUser user = new osuUser(qq, null);
        int status = OsuBindService.bindServer(8888, user);
        if (status == 0) {
            //通过bindServer得到的refreshToken来获取更多的token信息
            getToken(user);
            getPlayerInfo(user, "osu");
            MysqlUtil.writeUser(user);
            sender.SENDER.sendPrivateMsg(accountCode, "绑定成功");
        } else if (status == 1) {
            sender.SENDER.sendPrivateMsg(accountCode, "绑定超时");
        } else if (status == 2) {
            sender.SENDER.sendPrivateMsg(accountCode, "绑定失败");
        }
    }

    @OnGroup
    @Filter(value = "!oauth", trim = true, matchType = MatchType.EQUALS)
    public void sendOauth(GroupMsg groupMsg, MsgSender sender) {
        String accountCode = groupMsg.getAccountInfo().getAccountCode();
        long qq = Long.parseLong(accountCode);
        String url = getOauthUrl(groupMsg.getAccountInfo().getAccountCode());
        sender.SENDER.sendGroupMsg(groupMsg.getGroupInfo().getGroupCode(), url);
        sender.SENDER.sendGroupMsg(groupMsg.getGroupInfo().getGroupCode(), "请在2分钟内完成绑定操作");
        osuUser user = new osuUser(qq, null);

        int status = OsuBindService.bindServer(8888, user);
        if (status == 0) {
            //通过bindServer得到的refreshToken来获取更多的token信息
            getToken(user);
            getPlayerInfo(user, "osu");
            MysqlUtil.writeUser(user);
            sender.SENDER.sendGroupMsg(groupMsg.getGroupInfo().getGroupCode(), "绑定成功");
        } else if (status == 1) {
            sender.SENDER.sendGroupMsg(groupMsg.getGroupInfo().getGroupCode(), "绑定超时");
        } else if (status == 2) {
            sender.SENDER.sendGroupMsg(groupMsg.getGroupInfo().getGroupCode(), "绑定失败");
        }
    }


}
