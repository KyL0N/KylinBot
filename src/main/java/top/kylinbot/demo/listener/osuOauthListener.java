package top.kylinbot.demo.listener;


import love.forte.common.ioc.annotation.Beans;
import love.forte.simbot.annotation.Filter;
import love.forte.simbot.annotation.OnPrivate;
import love.forte.simbot.api.message.events.PrivateMsg;
import love.forte.simbot.api.sender.MsgSender;
import love.forte.simbot.filter.MatchType;
import top.kylinbot.demo.controller.MysqlServer;
import top.kylinbot.demo.modle.osuUser;
import top.kylinbot.demo.service.OsuBindService;
import top.kylinbot.demo.service.OsuService;
import top.kylinbot.demo.util.MysqlUtil;

import java.io.IOException;


@Beans
public class osuOauthListener extends OsuService {

    @OnPrivate
    @Filter(value = "!oauth", trim = true, matchType = MatchType.EQUALS)
    public void sendOauth(PrivateMsg privateMsg, MsgSender sender) throws IOException {
        int accountCode = Integer.parseInt(privateMsg.getAccountInfo().getAccountCode());
        String url = getOauthUrl(privateMsg.getAccountInfo().getAccountCode());
        sender.SENDER.sendPrivateMsg(accountCode, url);
        osuUser user = new osuUser(accountCode, null, null, null, null, 0);

        if (OsuBindService.bindServer(8888, user) == 0) {
            MysqlUtil.writeUser(getToken(user));
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
        MysqlServer sql = new MysqlServer();
        osuUser user = new osuUser(accountCode, null, null, null, null, 0);

//        JSONObject token = getToken();
        sender.SENDER.sendPrivateMsg(accountCode, "你的token:" + sql.getUserCode(user));
//        sender.SENDER.sendPrivateMsg(accountCode, user.toString());
    }

}
