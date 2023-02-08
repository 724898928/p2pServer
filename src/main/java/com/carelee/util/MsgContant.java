package com.carelee.util;

/**
 * Package: com.carelee.util
 * Description： TODO
 * Author: CareLee
 * Date: Created in 2020/12/11 16:42
 * Copyright: Copyright (c) 2040
 */
public interface MsgContant {
    String JOIN_ROOM = "joinRoom";
    String OFFER = "offer";         //Offer消息
    String ANSWER = "answer";        //Answer消息
    String CANDIDATE = "candidate";     //Candidate消息
    String HANGUP = "hangUp";         //挂断
    String LEAVE_ROOM = "leaveRoom";        //离开房间
    String UPDATE_USER_LIST = "updateUserList";  //更新房间用户列表

    String REMOVE_STREAM = "removestream";    //流移除事件
    String ADD_TREAM = "addstream";    //流添加事件
    String NEW_CALL = "newCall";    //监听新的呼叫事件
    String LOCAL_STREAM = "localstream";    //监听新本地流事件
}
