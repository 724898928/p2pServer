package com.carelee.model;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;

import javax.websocket.Session;

/**
 * Package: com.carelee.model
 * Description： TODO
 * Author: CareLee
 * Date: Created in 2020/12/11 15:16
 * Copyright: Copyright (c) 2040
 */
public class User {
    private String userId;
    private String name;
    @JSONField(serialize = false)
    private Session session;

    public User(Object id, Object name, Session session) {
        this.userId = String.valueOf(id);
        this.name = String.valueOf(name);
        this.session = session;
    }

    public User(JSONObject userMap, Session session) {
        this.userId = (String) userMap.get("userId");
        this.name = (String) userMap.get("name");
        this.session = session;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public Session getSession() {
        return session;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return userId.equals(user.userId) &&
                name.equals(user.name);
    }

    @Override
    public int hashCode() {
        if (userId == null)
            return 0;
        int result = 1;
        result = userId.hashCode()+ 31 * result + (name == null ? 0 : name.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + userId + '\'' +
                ", name='" + name + '\'' +
                ", session=" + session +
                '}';
    }
}
