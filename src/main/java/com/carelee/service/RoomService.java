package com.carelee.service;


import com.alibaba.fastjson.JSONObject;
import com.carelee.model.Room;
import com.carelee.model.User;
import com.carelee.util.MsgContant;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.websocket.Session;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Package: com.carelee.service
 * Description： TODO
 * Author: CareLee
 * Date: Created in 2020/12/11 15:24
 * Copyright: Copyright (c) 2040
 */
@Service
public class RoomService {
    private static final Logger log = LogManager.getLogger(RoomService.class);
    private static ConcurrentHashMap<String, Room> roomManager = new ConcurrentHashMap<String, Room>();

    public Room getRoom(String id) {
        return roomManager.get(id);
    }

    public Room createRoom(String id) {
        Room room = new Room(id);
        roomManager.put(id, room);
        return room;
    }

    public User onJoinRoom(JSONObject data, Room room, Session session) {
        User user = new User(data.get("id"), data.get("name"), session);
        room.addUser(user);
        return user;
    }

    public void deleteRoom(String id) {
        roomManager.remove(id);
    }

    public static ConcurrentHashMap<String, Room> roomManager() {
        return roomManager;
    }

    public void onCandidate(JSONObject data, Room room, JSONObject msg) {
        // 读取目标to属性值
        String to = String.valueOf(data.get("to"));
        User toUser = room.getUser(to);
        if (null != toUser) {
            try {
                toUser.getSession().getBasicRemote().sendText(msg.toJSONString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            log.error("没有发现用户 [" + to + "]");
        }
    }

    public void onHangUp(JSONObject data, Room room) {
        String sessionID = (String) data.get("sessionId");
        String[] ids = sessionID.split("-");
        User user = room.getUser(ids[0]);  // 自己
        JSONObject json = new JSONObject();
        Map<String, Object> map = new HashMap();
        if (null == user) {
            log.error("用户 [" + ids[0] + "] 没有找到");
            return;
        } else {
            try {
                map.put("to", ids[0]);
                map.put("sessionId", sessionID);
                json.put("data", map);
                json.put("type", MsgContant.HANGUP);
                user.getSession().getBasicRemote().sendText(json.toJSONString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        User user1 = room.getUser(ids[1]);   // 对方
        if (null != user1) {
            map.put("to", ids[1]);
            map.put("sessionId", sessionID);
            json.put("data", map);
            json.put("type", MsgContant.HANGUP);
            try {
                user1.getSession().getBasicRemote().sendText(json.toJSONString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            log.error("用户 [" + ids[1] + "] 没有找到");
            return;
        }

    }

    /**
     * 通知所有的用户更新
     *
     * @param users
     */
    public void notifyUsersUpdate(Map<String, User> users) {

        List<User> userArray = new ArrayList<>();
        if (null != users && users.size() > 0) {
            users.forEach((key, user) -> {
                userArray.add(user);
            });
            log.info("notifyUsersUpdate users = " + users.toString() + " userArray = " + userArray.toString());
            JSONObject json = new JSONObject();
            json.put("type", MsgContant.UPDATE_USER_LIST);
            json.put("data", userArray);
            //Map 转成  JSONObject 字符串
            users.forEach((key, user) -> {
                try {
                    log.info("notifyUsersUpdate jsonObject.toString() = " +json.toJSONString());
                    if (user.getSession().isOpen()) {
                        user.getSession().getBasicRemote().sendText(json.toJSONString());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }
}
