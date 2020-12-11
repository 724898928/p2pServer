package com.carelee.model;

/**
 * Package: com.carelee.model
 * Description： TODO
 * Author: CareLee
 * Date: Created in 2020/12/11 15:16
 * Copyright: Copyright (c) 2040
 */
public class User {
    private String id;
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "user{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
