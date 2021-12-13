package top.kylinbot.demo.listener;

import love.forte.common.ioc.annotation.Beans;
import love.forte.common.ioc.annotation.Depend;
import love.forte.simbot.annotation.Filter;
import love.forte.simbot.annotation.OnGroup;
import love.forte.simbot.annotation.OnPrivate;
import love.forte.simbot.api.message.events.GroupMsg;
import love.forte.simbot.api.message.events.PrivateMsg;
import love.forte.simbot.api.message.results.GroupList;
import love.forte.simbot.api.message.results.SimpleGroupInfo;
import love.forte.simbot.api.sender.Getter;
import love.forte.simbot.api.sender.MsgSender;
import love.forte.simbot.bot.Bot;
import love.forte.simbot.bot.BotDestroyer;
import love.forte.simbot.bot.BotManager;
import love.forte.simbot.core.SimbotContext;
import love.forte.simbot.core.SimbotProcess;
import love.forte.simbot.filter.MatchType;
import org.jetbrains.annotations.NotNull;
import top.kylinbot.demo.KylinBotApplication;

import java.util.HashMap;
import java.util.List;

@Beans
public class DebugListener {

    @Depend
    private BotManager botManager;

    @OnGroup
    @Filter(value = "!debug", matchType = MatchType.EQUALS)
    public void debugSend(GroupMsg groupMsg, MsgSender msgSender) {
        String helpMsg = new StringBuilder()
                .append("所有命令:\n")
                .append("NSFW图片: ").append(".uestc ").append(".zju ").append(".thu").append("\n")
                .append("牌理:").append("!t ").append("\n  示例:!t 123s1p9p").append("\n")
                .append("osu:\n")
                .append("  绑定:").append("!oauth\n")
                .append("  查询最近打图:").append("!kypr").append("\n")
                .append("  个人信息:").append("!info")
                .toString();
        msgSender.SENDER.sendGroupMsg(groupMsg.getGroupInfo().getGroupCode(), helpMsg);
    }


    @OnPrivate
    @Filter(value = "!shut", matchType = MatchType.EQUALS)
    public void shutdown(PrivateMsg privateMsg, MsgSender msgSender, Getter getter) {
        long count = getter.getGroupList().stream().count();
        GroupList list = getter.getGroupList();
        List<SimpleGroupInfo> results = list.getResults();
        int i = results.size() - 1;
        HashMap<String, Integer> body = new HashMap<>();
//        for (; i >= 0; i--) {
//            String groupCode = results.get(i).getGroupCode();
//            if (groupCode.equals("186263214)")){
//                break;
//            }
//            body.put(groupCode, i);
//            msgSender.SENDER.sendGroupMsg(groupCode, "bot进入调试阶段，稍后将中止进程");
//        }
        msgSender.SENDER.sendGroupMsg(334990945, "bot进入调试阶段，将中止进程");
        msgSender.SENDER.sendGroupMsg(278134001, "bot进入调试阶段，将中止进程");
        msgSender.SENDER.sendPrivateMsg(privateMsg.getAccountInfo().getAccountCode(), "发送完成" + getter.getBotInfo().getBotCode());
        botManager.destroyBot(getter.getBotInfo().getBotCode());
        System.exit(0);
    }
}
