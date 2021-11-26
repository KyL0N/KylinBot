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

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;


@Beans
public class listenerdemo {
    private static final String IMG_PATH = "~/target/photo_2021-11-20_17-19-12.jpg";
    private static int r18 = 0;

    public static int getR18() {
        return r18;
    }

    @OnPrivate
    @Filter(value = "!r18 on", trim = true, matchType = MatchType.EQUALS)
    public void setR18(PrivateMsg msg, MsgSender sender) {
        r18 = 1;
        sender.SENDER.sendPrivateMsg(msg.getAccountInfo().getAccountCode(), "r18 is on:" + getR18());
    }

    @OnPrivate
    @Filter(value = "!r18 off", trim = true, matchType = MatchType.EQUALS)
    public void offR18(PrivateMsg msg, MsgSender sender) {
        r18 = 0;
        sender.SENDER.sendPrivateMsg(msg.getAccountInfo().getAccountCode(), "r18 is off:" + getR18());
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
    @Filter(value = "show me the photo", trim = true, matchType = MatchType.EQUALS)
    public void sendImg(GroupMsg msg, MsgSender sender) {
        CatCodeUtil util = CatCodeUtil.INSTANCE;
        String image = util.getStringTemplate().image(IMG_PATH);
        sender.SENDER.sendGroupMsg(msg.getGroupInfo().getGroupCode(), image);
    }


    @OnGroup
    @Filter(value = ".catGro", trim = true, matchType = MatchType.EQUALS)
    public void sendGroupPixivPic(GroupMsg groupMsg, MsgSender sender) throws IOException {
        String catCode1 = getCodeFromApi();
//        sender.SENDER.sendGroupMsg(groupMsg.getGroupInfo().getGroupCode(), "url:" + urls);
        sender.SENDER.sendGroupMsg(groupMsg.getGroupInfo().getGroupCode(), catCode1);

    }


    @OnPrivate
    @Filter(value = ".catPri", trim = true, matchType = MatchType.EQUALS)
    public void sendPrivatePixivPic(PrivateMsg privateMsg, MsgSender sender) throws IOException {
        String catCode1 = getCodeFromApi();
//        sender.SENDER.sendPrivateMsg(privateMsg.getAccountInfo().getAccountCode(), "url:" + urls);
        sender.SENDER.sendPrivateMsg(privateMsg.getAccountInfo().getAccountCode(), catCode1);
    }

    public String getCodeFromApi() throws IOException {
        HttpClient client = HttpClients.createDefault();
        String url = "https://api.lolicon.app/setu/v2?size=regular";
        if (getR18() == 1) {
            url = url + "&r18=1";
        }
        url = url + "&r18=0";
        HttpGet get = new HttpGet(url);
        HttpResponse response = client.execute(get);
        HttpEntity entity = response.getEntity();
        String string = EntityUtils.toString(entity);
        System.out.println(string);
        String urls = parseJson(string);
//        String urlEncoded = CatEncoder.getInstance().encodeParams(urls);
        CatCodeUtil util = CatCodeUtil.INSTANCE;
        String image = util.getStringTemplate().image(urls);
        return image;
    }

    /**
     * 传入get到的Json信息
     *
     * @param string
     * @return 返回解析到的图片url信息
     */
    public String parseJson(String string) {
        JSONObject jsonObject = JSON.parseObject(string);
        JSONArray data = jsonObject.getJSONArray("data");

//        String pid = data.getJSONObject(0).getString("pid");
        String uid = data.getJSONObject(0).getString("uid");
//        String title = data.getJSONObject(0).getString("title");
//        String author = data.getJSONObject(0).getString("author");
        String r18 = data.getJSONObject(0).getString("r18");
//        String width = data.getJSONObject(0).getString("width");
//        String height = data.getJSONObject(0).getString("height");
        //TODO:TAGS
//        String ext = data.getJSONObject(0).getString("ext");
//        String uploadDate = data.getJSONObject(0).getString("uploadDate");

        String url = data.getJSONObject(0).getJSONObject("urls").getString("regular");
//        String url = urls.getString("original");


//        System.out.println("pid:" + pid);
        System.out.println("uid:" + uid);
//        System.out.println("title:" + title);
//        System.out.println("author:" + author);
        System.out.println("url:" + url);
        return url.replace(".cat", ".re");
    }


}
