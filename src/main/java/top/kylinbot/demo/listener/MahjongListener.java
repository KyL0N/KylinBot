package top.kylinbot.demo.listener;

import love.forte.common.ioc.annotation.Beans;
import love.forte.simbot.annotation.Filter;
import love.forte.simbot.annotation.OnGroup;
import love.forte.simbot.annotation.OnPrivate;
import love.forte.simbot.annotation.OnlySession;
import love.forte.simbot.api.message.events.GroupMsg;
import love.forte.simbot.api.message.events.PrivateMsg;
import love.forte.simbot.api.sender.MsgSender;
import love.forte.simbot.api.sender.Sender;
import love.forte.simbot.filter.MatchType;
import love.forte.simbot.listener.ContinuousSessionScopeContext;
import love.forte.simbot.listener.ListenerContext;
import love.forte.simbot.listener.SessionCallback;
import top.kylinbot.demo.util.HtmlUtil;

import java.util.concurrent.TimeoutException;

@Beans
public class MahjongListener {
    private static final String PHONE_GROUP = "==tellMeYourNameAndPhone==PHONE==";
    private static final String NAME_GROUP = "==tellMeYourNameAndPhone==NAME==";
    /**
     * @param groupMsg 手牌信息
     * @param sender   传输sender
     * @throws Exception 获取html的错误信息
     */
    @OnGroup
    @Filter(value = "!t", matchType = MatchType.STARTS_WITH)
    public void sendTenHouResult(GroupMsg groupMsg, MsgSender sender) throws Exception {
        String tiles = groupMsg.getMsg().replace("!t ", "");
        String result = HtmlUtil.getTenHouResult(tiles);
        sender.SENDER.sendGroupMsg(groupMsg.getGroupInfo().getGroupCode(), result);
    }

    /**
     * @param privateMsg 手牌信息
     * @param sender     传输sender
     * @throws Exception 获取html的错误信息
     */
    @OnPrivate
    @Filter(value = "!t", trim = true, matchType = MatchType.STARTS_WITH)
    public void sendTenHouResult(PrivateMsg privateMsg, MsgSender sender) throws Exception {
        String tiles = privateMsg.getMsg().replace("!t ", "");
        String result = HtmlUtil.getTenHouResult(tiles);
        sender.SENDER.sendPrivateMsg(privateMsg.getAccountInfo().getAccountCode(), result);
    }


    @OnGroup
    @Filter(value = "test", trim = true, matchType = MatchType.EQUALS)
    public void test(GroupMsg m, ListenerContext context, Sender sender) {
        // 直接将类型转化为 ContinuousSessionScopeContext
        // 这个 sessionContext 就是会话上下文
        final ContinuousSessionScopeContext sessionContext = (ContinuousSessionScopeContext) context.getContext(ListenerContext.Scope.CONTINUOUS_SESSION);
        assert sessionContext != null;
        final String groupCode = m.getGroupInfo().getGroupCode();
        final String code = m.getAccountInfo().getAccountCode();
        String key = groupCode + ":" + code;
//        if (sessionContext == null) {
//
//            // Do something
//            // 2.3.0后允许 getContext 得到null。虽然目前在由核心实现的情况下不会出现这个现象，但是依旧需要做好处理。
//            return;
//        }
        sender.sendGroupMsg(m,"请输出初始手牌:");
        final SessionCallback<Long> callback = SessionCallback.<Long>builder().onResume(phone -> {
            // 得到手机号，进行下一步操作
            sender.sendGroupMsg(m, "手机号为: " + phone);
            sender.sendGroupMsg(m, "请输入姓名");

            // 这是在回调中继续创建一个会话。
            // 这里通过 sessionContext.waiting(group, key, OnResume) 快速创建一个回调，只处理正确结果的情况，而不处理其他（出现错误、关闭事件等）
            // wait, 这里使用的是 name_group，也就是等待提供姓名的group，但是key还是那个人对应唯一的key
            sessionContext.waiting(NAME_GROUP, key, name -> {
                // 得到了name，结合上一个会话的 phone 发出结果消息
                sender.sendGroupMsg(m, name + "的手机号为" + phone);
            });

        }).onError(e -> {
            // 这里是第一个会话，此处通过 onError 来处理出现了异常的情况，例如超时
            if (e instanceof TimeoutException) {
                // 超时事件是 waiting的第三个参数，可以省略，默认下，超时时间为 1分钟
                // 这个默认的超时时间可以通过配置 simbot.core.continuousSession.defaultTimeout 进行指定。如果小于等于0，则没有超时，但是不推荐不设置超时。
                System.out.println("onError: 超时啦: " + e);
            } else {
                System.out.println("onError: 出错啦: " + e);
            }
        }).onCancel(e -> {
            // 这里是第一个会话，此处通过 onCancel 来处理会话被手动关闭、超时关闭的情况的处理，有些时候会与 orError 同时被触发（例如超时的时候）
            System.out.println("onCancel 关闭啦: " + e);
        }).build(); // build 构建

        // 这里开始等待第一个会话。
        sessionContext.waiting(PHONE_GROUP, key, callback);
        // And then..
    }

    @OnGroup
    @OnlySession(group = PHONE_GROUP)
    @Filter(value = "\\d+", matchType = MatchType.REGEX_MATCHES)
    public void phone(GroupMsg m, ListenerContext context) {
        // 得到session上下文。

        final ContinuousSessionScopeContext session = (ContinuousSessionScopeContext) context.getContext(ListenerContext.Scope.CONTINUOUS_SESSION);
        assert session != null;

        final String groupCode = m.getGroupInfo().getGroupCode();
        final String code = m.getAccountInfo().getAccountCode();

        // 拼接出来这个人对应的唯一key
        String key = groupCode + ":" + code;

        final String text = m.getText();
        final long phone = Long.parseLong(text);

        // 尝试将这个phone推送给对应的会话。
        session.push(PHONE_GROUP, key, phone);

        /*
            注意！如果你的会话结果逻辑比较复杂，那么你应该先判断这个key对应的会话是否存在，然后再进行推送。
            session.push 在没有发现对应的会话的情况下，会返回false。
         */

    }

    /**
     * 针对上述第二个会话的监听。
     * 因为这里是监听 获取姓名 的事件。
     * 通过 @OnlySession来限制，当且仅当由 NAME_GROUP 这个组的会话的时候，此监听函数才会生效。
     */
    @OnGroup
    @OnlySession(group = NAME_GROUP)
    public void onName(GroupMsg m, ListenerContext context) {
        // 得到session上下文。


        final ContinuousSessionScopeContext session = (ContinuousSessionScopeContext) context.getContext(ListenerContext.Scope.CONTINUOUS_SESSION);
        assert session != null;
        final String groupCode = m.getGroupInfo().getGroupCode();
        final String code = m.getAccountInfo().getAccountCode();
        // 拼接出来一个对应的唯一key
        String key = groupCode + ":" + code;

        // 尝试推送结果
        session.push(NAME_GROUP, key, m.getText());
    }
}
