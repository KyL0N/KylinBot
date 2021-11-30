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
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;


@Beans
public class listenerdemo extends NsfwService {



    @OnGroup
    @Filter(value = "!repeat", trim = true, matchType = MatchType.CONTAINS)
    public void repeat(GroupMsg msg, MsgSender sender) {
        sender.SENDER.sendGroupMsg(msg.getGroupInfo().getGroupCode(), msg.getMsgContent());
    }

    @OnGroup
    @Filter(value = "!kylin", trim = true, matchType = MatchType.EQUALS)
    public void hello(GroupMsg msg, MsgSender sender) throws UnknownHostException {
        InetAddress localAddress = InetAddress.getLocalHost();
        try {
            InetAddress candidateAddress = null;
            Enumeration<NetworkInterface> networkInterfaceEnumeration = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaceEnumeration.hasMoreElements()) {
                NetworkInterface iFace = networkInterfaceEnumeration.nextElement();
                for (Enumeration<InetAddress> inetAddress = iFace.getInetAddresses(); inetAddress.hasMoreElements(); ) {
                    InetAddress address = InetAddress.getLocalHost();
                    if (!address.isLoopbackAddress()) {
                        if (address.isSiteLocalAddress()) {
                            localAddress = address;
                        }

                        if (candidateAddress == null) {
                            candidateAddress = address;
                        }
                    }
                }
            }
            localAddress = candidateAddress == null ? InetAddress.getLocalHost() : candidateAddress;
        } catch (SocketException e) {
            e.printStackTrace();
        }

        sender.SENDER.sendGroupMsg(msg.getAccountInfo().getAccountCode(),
                "hello kylin is here" +
                        "\nHostAddress:" +
                        localAddress.getHostAddress() +
                        "\nHostName:" +
                        localAddress.getHostName()
        );
    }




}
