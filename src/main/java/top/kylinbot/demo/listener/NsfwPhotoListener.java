package top.kylinbot.demo.listener;

import catcode.CatCodeUtil;
import love.forte.common.ioc.annotation.Beans;
import love.forte.simbot.annotation.Filter;
import love.forte.simbot.annotation.OnGroup;
import love.forte.simbot.annotation.OnPrivate;
import love.forte.simbot.api.message.events.GroupMsg;
import love.forte.simbot.api.message.events.PrivateMsg;
import love.forte.simbot.api.sender.MsgSender;
import love.forte.simbot.filter.MatchType;
import top.kylinbot.demo.service.NsfwService;

import java.io.IOException;

@Beans
public class NsfwPhotoListener extends NsfwService {
    private static final String IMG_PATH = "~/target/photo_2021-11-20_17-19-12.jpg";
    private static int r18 = 0;

    /**
     * 获取R18标志, 0为off 1为on
     * @return 返回r18值
     */
    public static int getR18() {
        return r18;
    }

    @OnGroup
    @Filter(value = "show me the photo", trim = true, matchType = MatchType.STARTS_WITH)
    public void sendImg(GroupMsg msg, MsgSender sender) {
        CatCodeUtil util = CatCodeUtil.INSTANCE;
        String image = util.getStringTemplate().image(IMG_PATH);
        sender.SENDER.sendGroupMsg(msg.getGroupInfo().getGroupCode(), image);
    }


    @OnGroup
    @Filter(value = ".uestc", trim = true, matchType = MatchType.STARTS_WITH)
    public void sendGroupPixivPic(GroupMsg groupMsg, MsgSender sender) throws IOException {
//        sender.SENDER.sendGroupMsg(groupMsg.getGroupInfo().getGroupCode(), "url:" + urls);
        if (getR18() == 1){
            sender.SENDER.sendGroupMsg(groupMsg.getGroupInfo().getGroupCode(), "R18功能现对群聊关闭");
        }
        String catCode1 = getCatCodeFromApi(false);
        sender.SENDER.sendGroupMsg(groupMsg.getGroupInfo().getGroupCode(), catCode1);

    }


    @OnPrivate
    @Filter(value = ".uestc", trim = true, matchType = MatchType.STARTS_WITH)
    public void sendPrivatePixivPic(PrivateMsg privateMsg, MsgSender sender) throws IOException {
//        sender.SENDER.sendPrivateMsg(privateMsg.getAccountInfo().getAccountCode(), "url:" + urls);
        String catCode1 = getCatCodeFromApi(true);
        sender.SENDER.sendPrivateMsg(privateMsg.getAccountInfo().getAccountCode(), catCode1);
    }



    @OnPrivate
    @Filter(value = "!r18 on", trim = true, matchType = MatchType.EQUALS)
    public void setR18(PrivateMsg privateMsg, MsgSender sender) {
        r18 = 1;
        sender.SENDER.sendPrivateMsg(privateMsg.getAccountInfo().getAccountCode(), "now r18 is on:" + getR18());
    }

    @OnPrivate
    @Filter(value = "!r18 off", trim = true, matchType = MatchType.EQUALS)
    public void offR18(PrivateMsg privateMsg, MsgSender sender) {
        r18 = 0;
        sender.SENDER.sendPrivateMsg(privateMsg.getAccountInfo().getAccountCode(), "now r18 is off:" + getR18());
    }
}
