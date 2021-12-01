package top.kylinbot.demo;

import love.forte.common.configuration.Configuration;
import love.forte.simbot.annotation.SimbotApplication;
import love.forte.simbot.api.sender.BotSender;
import love.forte.simbot.bot.Bot;
import love.forte.simbot.core.SimbotApp;
import love.forte.simbot.core.SimbotContext;
import love.forte.simbot.core.SimbotProcess;
import org.jetbrains.annotations.NotNull;
import top.kylinbot.demo.service.OsuService;

import java.time.LocalDateTime;

import static top.kylinbot.demo.listener.NsfwPhotoListener.getR18;


@SimbotApplication
public class KylinBotApplication implements SimbotProcess {

    public static void main(String[] args) {
        SimbotApp.run(KylinBotApplication.class, args);
//        mysqlUtil.testConnect();
//        OsuService osuService = new OsuService();
//        osuService.getToken();
    }

    @Override
    public void post(@NotNull SimbotContext context) {
        Bot bot = context.getBotManager().getDefaultBot();
        BotSender sender = bot.getSender();
        sender.SENDER.sendPrivateMsg(1579525246, "Bot is started\n" + LocalDateTime.now());
//        sender.SENDER.sendGroupMsg(278134001, "Bot is started\n r18:" + getR18());
    }

    @Override
    public void pre(@NotNull Configuration config) {

    }
}
