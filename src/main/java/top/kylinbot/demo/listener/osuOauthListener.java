package top.kylinbot.demo.listener;


import love.forte.common.ioc.annotation.Beans;
import love.forte.simbot.annotation.Filter;
import love.forte.simbot.annotation.OnPrivate;
import love.forte.simbot.api.message.events.PrivateMsg;
import love.forte.simbot.api.sender.MsgSender;
import love.forte.simbot.filter.MatchType;
import top.kylinbot.demo.controller.mysqlServer;
import top.kylinbot.demo.modle.osuUser;
import top.kylinbot.demo.service.osuService;
import top.kylinbot.demo.util.mysqlUtil;

import static top.kylinbot.demo.KylinBotApplication.bindServer;

@Beans
public class osuOauthListener extends osuService {

    @OnPrivate
    @Filter(value = "!oauth", trim = true, matchType = MatchType.EQUALS)
    public void sendOauth(PrivateMsg privateMsg, MsgSender sender) {
        int accountCode = Integer.parseInt(privateMsg.getAccountInfo().getAccountCode());
        String url = getOauthUrl(privateMsg.getAccountInfo().getAccountCode());
        sender.SENDER.sendPrivateMsg(accountCode, url);
        osuUser user = new osuUser(accountCode, null, null, null, null, 0);
        if (bindServer(8888, user) == 0) {
            mysqlUtil.writeUser(getToken(user));
            sender.SENDER.sendPrivateMsg(accountCode, "绑定成功");
            sender.SENDER.sendPrivateMsg(accountCode, "帐户详细:" + "\nqq:" + user.getQQ() + "\nID:" + user.getOsuID() + "\naccessToken:" + user.getRefreshToken());
        } else {
            sender.SENDER.sendPrivateMsg(accountCode, "绑定失败或超时");
        }

    }

    @OnPrivate
    @Filter(value = "!token", trim = true, matchType = MatchType.EQUALS)
    public void sendToken(PrivateMsg privateMsg, MsgSender sender) {
        int accountCode = Integer.parseInt(privateMsg.getAccountInfo().getAccountCode());
        mysqlServer sql = new mysqlServer();
        osuUser user = new osuUser(accountCode, null, null, null, null, 0);

//        JSONObject token = getToken();
        sender.SENDER.sendPrivateMsg(accountCode, "你的token:" + sql.getUserCode(user));
        sender.SENDER.sendPrivateMsg(accountCode, user.toString());
    }

}
