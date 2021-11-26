package top.kylinbot.demo.listener;


import com.alibaba.fastjson.JSONObject;
import love.forte.common.ioc.annotation.Beans;
import love.forte.simbot.annotation.Filter;
import love.forte.simbot.annotation.OnPrivate;
import love.forte.simbot.api.message.events.PrivateMsg;
import love.forte.simbot.api.sender.MsgSender;
import love.forte.simbot.filter.MatchType;
import top.kylinbot.demo.service.osuService;

@Beans
public class osuOauthListener extends osuService {

    @OnPrivate
    @Filter(value = "!oauth",trim = true,matchType = MatchType.EQUALS)
    public void sendOauth(PrivateMsg privateMsg, MsgSender sender){
        String accountCode = privateMsg.getAccountInfo().getAccountCode();
        String url = getOauthUrl(accountCode);
        sender.SENDER.sendPrivateMsg(accountCode, url);
    }

    @OnPrivate
    @Filter(value = "!token",trim = true,matchType = MatchType.EQUALS)
    public void sendToken(PrivateMsg privateMsg, MsgSender sender){
        String accountCode = privateMsg.getAccountInfo().getAccountCode();
        JSONObject token = getToken();
        sender.SENDER.sendPrivateMsg(accountCode, token.toString());
    }

}
