package com.carelee.service;


import com.alibaba.fastjson.JSONObject;
import com.carelee.model.Room;
import com.carelee.model.User;
import com.carelee.util.MsgContant;
import com.carelee.util.StringUtils;
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
        log.info("createRoom roomId:" + id);
        Room room = new Room(id);
        roomManager.put(id, room);
        return room;
    }

    public User onJoinRoom(JSONObject data, Room room, Session session) {
        //  User user = new User(data.get("id"), data.get("name"), session);
        log.info("onJoinRoom data:" + data + " room:" + room);
        String fromId = String.valueOf(data.get("from"));
        String userName = String.valueOf(data.get("fromName"));
        String toUserId = String.valueOf(data.get("to"));
        User user = new User(fromId, userName, session);
        room.addUser(user);
        // tell the user that I am connecting is online;
        JSONObject msg = new JSONObject();
        msg.put("type", MsgContant.JOIN_ROOM);
        data.put("peer",room.users().values().toArray());
        msg.put("data", data);
        sendMsg2Users(null, room, msg);
        return user;
    }

    public void deleteRoom(String id) {
        roomManager.remove(id);
    }

    public static ConcurrentHashMap<String, Room> roomManager() {
        return roomManager;
    }

    public void onCandidate(JSONObject data, Room room, Session session) {
        log.info("onCandidate data:" + data + " room:" + room);
        JSONObject candidate = (JSONObject) data.clone();
        String from = data.getString("from");
        JSONObject candidateMsg = new JSONObject();
        candidateMsg.put("type", MsgContant.CANDIDATE);
        candidateMsg.put("data", candidate);
        log.info("onCandidate candidateMsg:" + candidateMsg);
        sendMsg2Users(from, room, candidateMsg);
//        try {
//            candidateMsg.put("data", data);
//            log.info("onCandidate candidateMsg:" + candidateMsg);
//            session.getBasicRemote().sendText(candidateMsg.toJSONString());
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
    }

    private void sendMsg2Users(String fromId, Room room, JSONObject msg) {
        // 读取目标to属性值
        log.info("sendMsg2Users room: " + room);
        if (null != room) {
            Map<String, User> users = room.users();
            users.forEach((userId,user) ->{
                if (null != user && !userId.equals(fromId)) {
                    try {
                        log.info("sendMsg2Users room.user:" + user);
                        Session session = user.getSession();
                        if (session.isOpen()) {
                            session.getBasicRemote().sendText(msg.toJSONString());
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    log.error("用户 [" + userId + "]");
                }
            });
        }
    }

    private void sendMsg2User(String userId, Room room, JSONObject msg) {
        // 读取目标to属性值
        log.info("sendMsg2User data: to :" + userId);
        if (StringUtils.notEmpty(userId)) {
            User toUser = room.getUser(userId);
            log.info("sendMsg2User room.getUser toUser:" + toUser);
            if (null != toUser) {
                try {
                    Session session = toUser.getSession();
                    if (session.isOpen()) {
                        session.getBasicRemote().sendText(msg.toJSONString());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                log.error("没有发现用户 [" + userId + "]");
            }
        }
    }


    public void onHangUp(JSONObject data, Room room) {
        //  String sessionID = data.getString("sessionId");
        //  String[] ids = sessionID.split("-");
        String from = data.getString("from");// 自己
        // User user = room.getUser(from);
        String to = data.getString("to"); // 对方
        String sessionId = data.getString("sessionId"); // 对方
        JSONObject json = new JSONObject();
        Map<String, Object> map = new HashMap();
        map.put("to", to);
        map.put("sessionId", sessionId);
        json.put("data", map);
        json.put("type", MsgContant.HANGUP);
        sendMsg2Users(from, room, json);
//        map.put("to", from);
//        //  map.put("sessionId", sessionID);
//        json.put("data", map);
//        json.put("type", MsgContant.HANGUP);
//        sendMsg2User(to, room, json);
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
                    log.info("notifyUsersUpdate jsonObject.toString() = " + json.toJSONString());
                    Session userSession = user.getSession();
                    if (null != userSession && userSession.isOpen()) {
                        userSession.getBasicRemote().sendText(json.toJSONString());
                    } else {
                        log.info("user = " + user + " userSession =null");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    // from create offer for to
    public void onOffer(JSONObject data, Room room, Session session) {
        JSONObject offer = (JSONObject) data.clone();
        // 读取目标to属性值
        String from = data.getString("from"); // 对方
        JSONObject offerMsg = new JSONObject();
        offerMsg.put("type", MsgContant.OFFER);
        offerMsg.put("data", data);
        log.info("onOffer offerMsg ->" + offer.toString());
        sendMsg2Users(from, room, offerMsg);
        // My session
//        try {
//            offerMsg.put("data", data);
//            log.info("onOffer data ->" + offerMsg.toString());
//            session.getBasicRemote().sendText(offerMsg.toJSONString());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

    }

    // to create answer for from
    public void onAnswer(JSONObject data, Room room, Session session) {
        // 读取目标to属性值
        JSONObject answer = (JSONObject) data.clone();
        String to = data.getString("to"); // 对方
        JSONObject answerMsg = new JSONObject();
        answerMsg.put("type", MsgContant.ANSWER);
        answerMsg.put("data", answer);
        log.info("onAnswer answerMsg1 ->" + answerMsg.toString());
        sendMsg2Users(to, room, answerMsg);
        // My session
//        try {
//            answerMsg.put("data", data);
//            log.info("onAnswer data ->" + answerMsg.toString());
//            session.getBasicRemote().sendText(answerMsg.toJSONString());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    private JSONObject swap2From(JSONObject data){
        String to = data.getString("to");
        String toName = data.getString("toName");
        String from = data.getString("from");
        String fromName = data.getString("fromName");
        data.put("to", from);
        data.put("toName", fromName);
        data.put("from", to);
        data.put("fromName", toName);
       return data;
    }
}
