package com.carelee.websocket;

import com.alibaba.fastjson.JSONObject;
import com.carelee.util.StringUtils;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Package: com.carelee.websocket
 * Description： TODO
 * Author: CareLee
 * Date: Created in 2020/12/11 11:10
 * Copyright: Copyright (c) 2040
 */
@ServerEndpoint(value = "/socket")
@Component
public class socket {
    // 静态变量，用来记录当前在线的连接数，应该把它设计成线程安全的
    private static AtomicInteger onlineCount = new AtomicInteger(0);
    // oncurrent包的线程安全set，用来存放每个客户端对应的socket对象
    private static CopyOnWriteArraySet<socket> webSocketSet = new CopyOnWriteArraySet<socket>();
    // 与某个客户端的连接会话，需要通过它来给客户发送数据
    private Session session;

    @OnOpen
    public void onOpen(Session session){
        this.session = session;
        webSocketSet.add(this);  // 加入set中
        addOnlineCount();         //在线数加一
        System.out.println("有新连接加入！当前在线人数为"+getOnlineCount());

    }

    @OnMessage
    public void onMessage(String message,Session session){
        System.out.println("来自客户端的消息:"+message);
        if (StringUtils.notEmtpy(message)){
            JSONObject jsonObject = JSONObject.parseObject(message);
           String type = StringUtils.getStr(jsonObject.get("type"));
           Map<String,String> data = (Map<String, String>) jsonObject.get("data");

        }
    }

    @OnClose
    public void onClose(){
        webSocketSet.remove(this); //从set中删除
        subOnlineCount(); // 在线数-1

    }

    @OnError
    public void onError(Session session,Throwable throwable){
        System.out.println("发生错误");
        throwable.printStackTrace();
    }

    private void subOnlineCount() {
        onlineCount.decrementAndGet();
    }

    private void sendMessage(String msg) {
    }

    private String getOnlineCount() {
        return  String.valueOf(onlineCount.get());
    }

    private void addOnlineCount() {
        onlineCount.incrementAndGet();
    }
}
