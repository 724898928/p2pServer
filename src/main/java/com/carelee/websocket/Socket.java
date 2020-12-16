package com.carelee.websocket;

import com.alibaba.fastjson.JSONObject;
import com.carelee.model.Room;
import com.carelee.model.User;
import com.carelee.service.RoomService;
import com.carelee.util.MsgContant;
import com.carelee.util.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Package: com.carelee.websocket
 * Description： TODO
 * Author: CareLee
 * Date: Created in 2020/12/11 11:10
 * Copyright: Copyright (c) 2040
 */
@ServerEndpoint(value = "/socket")
@Component
public class Socket {
    private static final Logger log = LogManager.getLogger(RoomService.class);
    // 静态变量，用来记录当前在线的连接数，应该把它设计成线程安全的
    private static AtomicInteger onlineCount = new AtomicInteger(0);
    // concurrent，用来存放每个客户端对应的socket对象
    private static CopyOnWriteArraySet<Socket> webSocketSet = new CopyOnWriteArraySet<Socket>();
    // 与某个客户端的连接会话，需要通过它来给客户发送数据
    private Session session;

    private static RoomService roomService = new RoomService();

    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        webSocketSet.add(this);  // 加入set中
        addOnlineCount();         //在线数加一
        log.info("有新连接加入！当前在线人数为" + getOnlineCount());

    }

    @OnMessage
    public void onMessage(String message, Session session) {
        log.info("来自客户端的消息:" + message);
        if (StringUtils.notEmpty(message)) {
            JSONObject jsonObject = JSONObject.parseObject(message);
            String type = StringUtils.getStr(jsonObject.get("type"));
            JSONObject data = (JSONObject) jsonObject.get("data");
            if (null != data) {
                String roomId = (String) data.get("roomId");
                Room room = RoomService.roomManager().get(roomId);
                if (null == room) {
                    room = roomService.createRoom(roomId);
                }
                switch (type) {
                    case MsgContant.JOIN_ROOM:
                        User user = roomService.onJoinRoom(data, room, session);
                        log.info("Room = " + room.toString());
                        roomService.notifyUsersUpdate(room.users());
                        break;
                    case MsgContant.OFFER:
                    case MsgContant.ANSWER:
                    case MsgContant.CANDIDATE:
                        roomService.onCandidate(data, room, jsonObject);
                        break;
                    case MsgContant.HANGUP:
                        roomService.onHangUp(data, room);
                        break;
                    default:
                        log.info("未知的请求 %v");
                        break;
                }

            }

        }
    }

    @OnClose
    public void onClose() {
        webSocketSet.remove(this); //从set中删除
        subOnlineCount(); // 在线数-1
        AtomicReference<String> userId = new AtomicReference<>("");
        AtomicReference<String> roomId = new AtomicReference<>("");
        RoomService.roomManager().forEach((roId, room) -> {
            for (Map.Entry<String, User> entry : room.users().entrySet()) {
                User user = entry.getValue();
                Session session = user.getSession();
                if (this.session.equals(session)) {
                    userId.set(user.getId());
                    roomId.set(room.getId());
                    break;
                }
            }
        });

        if ("".equals(roomId)) {
            log.info("没有查找到退出的房间及用户");
            return;
        }
        log.info("退出的用户roomId =" + roomId + "  userId =" + userId);
        Room room = roomService.getRoom(roomId.get());
        if (null != room) {
            room.users().forEach((key, user) -> {
                Session userSession = user.getSession();
                if (this.session != userSession) {
                    JSONObject map = new JSONObject();
                    map.put("type", MsgContant.LEAVE_ROOM);
                    try {
                        if (userSession.isOpen()) {
                            userSession.getBasicRemote().sendText(map.toJSONString());
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            room.delUser(userId.get());
            roomService.notifyUsersUpdate(room.users());
        }
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        log.info("发生错误");
        throwable.printStackTrace();
    }

    private void subOnlineCount() {
        onlineCount.decrementAndGet();
    }

    private String getOnlineCount() {
        return String.valueOf(onlineCount.get());
    }

    private void addOnlineCount() {
        onlineCount.incrementAndGet();
    }
}
