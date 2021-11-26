package top.kylinbot.demo.listener;

import catcode.CatCodeUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import love.forte.common.ioc.annotation.Beans;
import love.forte.simbot.annotation.Filter;
import love.forte.simbot.annotation.OnGroup;
import love.forte.simbot.annotation.OnPrivate;
import love.forte.simbot.api.message.events.GroupMsg;
import love.forte.simbot.api.message.events.PrivateMsg;
import love.forte.simbot.api.sender.MsgSender;
import love.forte.simbot.filter.MatchType;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import top.kylinbot.demo.service.nsfwService;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;


@Beans
public class listenerdemo extends nsfwService {
    private static final String IMG_PATH = "~/target/photo_2021-11-20_17-19-12.jpg";
    private static int r18 = 0;

    public static int getR18() {
        return r18;
    }

    @OnGroup
    @Filter(value = "!r18 on", trim = true, matchType = MatchType.EQUALS)
    public void setR18(GroupMsg groupMsg, MsgSender sender) {
        r18 = 1;
//        sender.SENDER.sendPrivateMsg(msg.getAccountInfo().getAccountCode(), "now r18 is on:" + getR18());
        sender.SENDER.sendGroupMsg(groupMsg.getGroupInfo().getGroupCode(), "now r18 is on:" + getR18());

    }

    @OnGroup
    @Filter(value = "!r18 off", trim = true, matchType = MatchType.EQUALS)
    public void offR18(GroupMsg groupMsg, MsgSender sender) {
        r18 = 0;
//        sender.SENDER.sendPrivateMsg(msg.getAccountInfo().getAccountCode(), "now r18 is off:" + getR18());
        sender.SENDER.sendGroupMsg(groupMsg.getGroupInfo().getGroupCode(), "now r18 is off:" + getR18());

    }


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
        String catCode1 = getCodeFromApi();
        sender.SENDER.sendGroupMsg(groupMsg.getGroupInfo().getGroupCode(), catCode1);

    }


    @OnPrivate
    @Filter(value = ".uestc", trim = true, matchType = MatchType.STARTS_WITH)
    public void sendPrivatePixivPic(PrivateMsg privateMsg, MsgSender sender) throws IOException {
//        sender.SENDER.sendPrivateMsg(privateMsg.getAccountInfo().getAccountCode(), "url:" + urls);
        String catCode1 = getCodeFromApi();
        sender.SENDER.sendPrivateMsg(privateMsg.getAccountInfo().getAccountCode(), catCode1);
    }


}
