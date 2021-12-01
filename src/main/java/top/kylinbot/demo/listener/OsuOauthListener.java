package top.kylinbot.demo.listener;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import love.forte.common.ioc.annotation.Beans;
import love.forte.simbot.annotation.Filter;
import love.forte.simbot.annotation.OnPrivate;
import love.forte.simbot.api.message.events.PrivateMsg;
import love.forte.simbot.api.sender.MsgSender;
import love.forte.simbot.filter.MatchType;
import top.kylinbot.demo.modle.osuUser;
import top.kylinbot.demo.service.OsuBindService;
import top.kylinbot.demo.service.OsuService;
import top.kylinbot.demo.util.JsonUtil;
import top.kylinbot.demo.util.MysqlUtil;


@Beans
public class OsuOauthListener extends OsuService {

    @OnPrivate
    @Filter(value = "!oauth", trim = true, matchType = MatchType.EQUALS)
    public void sendOauth(PrivateMsg privateMsg, MsgSender sender) {
        int accountCode = Integer.parseInt(privateMsg.getAccountInfo().getAccountCode());
        String url = getOauthUrl(privateMsg.getAccountInfo().getAccountCode());
        sender.SENDER.sendPrivateMsg(accountCode, url);
        osuUser user = new osuUser(accountCode, null);

        if (OsuBindService.bindServer(8888, user) == 0) {
            //通过bindServer得到的refreshToken来获取更多的token信息
            getToken(user);
            //
            getPlayerInfo(user, "osu");

            MysqlUtil.writeUser(user);
            sender.SENDER.sendPrivateMsg(accountCode, "绑定成功");
//            sender.SENDER.sendPrivateMsg(accountCode, "帐户详细:" + "\nqq:" + user.getQQ() + "\nID:" + user.getOsuID() + "\naccessToken:" + user.getRefreshToken());
        } else {
            sender.SENDER.sendPrivateMsg(accountCode, "绑定失败或超时");
        }

    }

    @OnPrivate
    @Filter(value = "!token", trim = true, matchType = MatchType.EQUALS)
    public void sendToken(PrivateMsg privateMsg, MsgSender sender) {
        int accountCode = Integer.parseInt(privateMsg.getAccountInfo().getAccountCode());
        osuUser user = new osuUser(accountCode, null);
//        JSONObject token = getToken();
        sender.SENDER.sendPrivateMsg(accountCode, "你的token:" + MysqlUtil.getUserCode(user));
//        sender.SENDER.sendPrivateMsg(accountCode, user.toString());
    }

    @OnPrivate
    @Filter(value = "!info", trim = true, matchType = MatchType.EQUALS)
    public void sendPlayerInfo(PrivateMsg privateMsg, MsgSender sender) {
        String qq = privateMsg.getAccountInfo().getAccountCode();
        String osuNickName = MysqlUtil.getOsuIDByQQ(qq);
        JSONObject testInfo = getPlayerOsuInfo(osuNickName);
        String info = JsonUtil.parseOsuInfoJson(testInfo);
        sender.SENDER.sendPrivateMsg(privateMsg.getAccountInfo().getAccountCode(), info);
    }

}
