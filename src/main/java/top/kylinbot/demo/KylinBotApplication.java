package top.kylinbot.demo;

import love.forte.common.configuration.Configuration;
import love.forte.simbot.annotation.SimbotApplication;
import love.forte.simbot.api.sender.BotSender;
import love.forte.simbot.bot.Bot;
import love.forte.simbot.core.SimbotApp;
import love.forte.simbot.core.SimbotContext;
import love.forte.simbot.core.SimbotProcess;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;


@SimbotApplication
public class KylinBotApplication implements SimbotProcess {

    public static void main(String[] args) {
        SimbotApp.run(KylinBotApplication.class, args);
//        HtmlUtil.getTenHouResult("1112345678999s1s");
    }


    @Override
    public void post(@NotNull SimbotContext context) {
        Bot bot = context.getBotManager().getDefaultBot();
        BotSender sender = bot.getSender();
        sender.SENDER.sendPrivateMsg(1579525246, "Bot is started\n" + LocalDateTime.now());
    }

    @Override
    public void pre(@NotNull Configuration config) {

    }
}
