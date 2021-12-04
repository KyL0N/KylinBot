package top.kylinbot.demo.listener;

import com.alibaba.fastjson.JSONObject;
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
            getPlayerInfo(user, "osu");
            MysqlUtil.writeUser(user);
            sender.SENDER.sendPrivateMsg(accountCode, "绑定成功");
        } else {
            sender.SENDER.sendPrivateMsg(accountCode, "绑定失败或超时");
        }
    }

    @OnGroup
    @Filter(value = "!oauth", trim = true, matchType = MatchType.EQUALS)
    public void sendOauth(GroupMsg groupMsg, MsgSender sender) {
        int accountCode = Integer.parseInt(groupMsg.getAccountInfo().getAccountCode());
        String url = getOauthUrl(groupMsg.getAccountInfo().getAccountCode());
        sender.SENDER.sendPrivateMsg(accountCode, url);
        osuUser user = new osuUser(accountCode, null);

        if (OsuBindService.bindServer(8888, user) == 0) {
            //通过bindServer得到的refreshToken来获取更多的token信息
            getToken(user);
            getPlayerInfo(user, "osu");
            MysqlUtil.writeUser(user);
            sender.SENDER.sendPrivateMsg(groupMsg.getGroupInfo().getGroupCode(), "绑定成功");
        } else {
            sender.SENDER.sendPrivateMsg(groupMsg.getGroupInfo().getGroupCode(), "绑定失败或超时");
        }
    }


    @OnPrivate
    @Filter(value = "!info", trim = true, matchType = MatchType.EQUALS)
    public void sendPlayerInfo(PrivateMsg privateMsg, MsgSender sender) {
        String qq = privateMsg.getAccountInfo().getAccountCode();
        String osuNickName = MysqlUtil.getOsuIDByQQ(qq);
        if (osuNickName == null) {
            sender.SENDER.sendPrivateMsg(privateMsg.getAccountInfo().getAccountCode(), "尚未绑定, 请发送!oauth以绑定bot");
        }
        JSONObject testInfo = getPlayerOsuInfo(osuNickName);
        String info = JsonUtil.parseOsuInfoJson(testInfo);
        sender.SENDER.sendPrivateMsg(privateMsg.getAccountInfo().getAccountCode(), info);
    }

    @OnGroup
    @Filter(value = "!info", trim = true, matchType = MatchType.EQUALS)
    public void sendPlayerInfo(GroupMsg groupMsg, MsgSender sender) {
        String qq = groupMsg.getAccountInfo().getAccountCode();
        String osuNickName = MysqlUtil.getOsuIDByQQ(qq);
        if (osuNickName == null) {
            sender.SENDER.sendGroupMsg(groupMsg.getGroupInfo().getGroupCode(), "尚未绑定, 请发送!oauth以绑定bot");
        }
        String info = JsonUtil.parseOsuInfoJson(getPlayerOsuInfo(osuNickName));
        sender.SENDER.sendGroupMsg(groupMsg.getGroupInfo().getGroupCode(), info);
    }


    @OnPrivate
    @Filter(value = "!info", trim = true, matchType = MatchType.STARTS_WITH)
    public void sendOtherPlayerInfo(PrivateMsg privateMsg, MsgSender sender) {
//        String osuNickName = privateMsg.getMsgContent().toString().replace("!info ","");
        String osuNickName = privateMsg.getMsg().replace("!info ", "");
        String info = JsonUtil.parseOsuInfoJson(getPlayerOsuInfo(osuNickName));
        sender.SENDER.sendPrivateMsg(privateMsg.getAccountInfo().getAccountCode(), info);
    }

    @OnGroup
    @Filter(value = "!info", trim = true, matchType = MatchType.STARTS_WITH)
    public void sendOtherPlayerInfo(GroupMsg groupMsg, MsgSender sender) {
        String osuNickName = groupMsg.getMsg().replace("!info ","");
        String info = JsonUtil.parseOsuInfoJson(getPlayerOsuInfo(osuNickName));
        sender.SENDER.sendGroupMsg(groupMsg.getGroupInfo().getGroupCode(), info);
    }



}
