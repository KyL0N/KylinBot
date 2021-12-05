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
import top.kylinbot.demo.service.MahjongService.MahjongTestService;
import top.kylinbot.demo.service.NsfwService;
import top.kylinbot.demo.util.HtmlUtil;
import top.kylinbot.demo.util.JsonUtil;

import java.net.*;
import java.util.Enumeration;


@Beans
public class TestListener {

    @OnGroup
    @Filter(value = "!repeat", trim = true, matchType = MatchType.CONTAINS)
    public void repeat(GroupMsg msg, MsgSender sender) {
        sender.SENDER.sendGroupMsg(msg.getGroupInfo().getGroupCode(), msg.getMsgContent());
    }

    @OnGroup
    @Filter(value = "where are you", trim = true, matchType = MatchType.EQUALS)
    public void hello(GroupMsg msg, MsgSender sender) throws UnknownHostException {
        String localIP = null;
        try {
            Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface.getNetworkInterfaces();
            while (allNetInterfaces.hasMoreElements()) {
                NetworkInterface netInterface = (NetworkInterface) allNetInterfaces.nextElement();
                Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress ip = (InetAddress) addresses.nextElement();
                    if (ip instanceof Inet4Address
                            && !ip.isLoopbackAddress()
                            && !ip.getHostAddress().contains(":")) {
//                        System.out.println("本机的IP = " + ip.getHostAddress());
                        localIP = ip.getHostAddress();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        sender.SENDER.sendGroupMsg(msg.getGroupInfo().getGroupCode(),
                "I'm here Kylin" +
                        "\nHostAddress:" +
                        localIP
        );
    }


}
