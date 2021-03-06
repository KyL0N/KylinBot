package top.kylinbot.demo.listener;

import love.forte.common.ioc.annotation.Beans;
import love.forte.simbot.annotation.OnFriendAddRequest;
import love.forte.simbot.annotation.OnFriendIncrease;
import love.forte.simbot.api.message.events.FriendAddRequest;
import love.forte.simbot.api.message.events.FriendIncrease;
import love.forte.simbot.api.sender.Sender;
import love.forte.simbot.api.sender.Setter;

@Beans
public class FriendAddListener {

    @OnFriendAddRequest
    public void acceptFriendRequest(FriendAddRequest flag, Setter setter) {
        setter.acceptFriendAddRequest(flag.getFlag());
    }

    @OnFriendIncrease
    public void welcomeNewFriend(FriendIncrease friendIncrease, Sender sender) {
        sender.sendPrivateMsg(friendIncrease.getAccountInfo().getAccountCodeNumber(), "hello im Asagodness");
        sender.sendPrivateMsg(1579525246, friendIncrease.getAccountInfo().getAccountCodeNumber() + " add friend");
    }
}
