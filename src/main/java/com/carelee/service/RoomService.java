package com.carelee.service;

import com.carelee.model.Room;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Package: com.carelee.service
 * Description： TODO
 * Author: CareLee
 * Date: Created in 2020/12/11 15:24
 * Copyright: Copyright (c) 2040
 */
public class RoomService {
    private ConcurrentHashMap<String, Room> roomManager = new ConcurrentHashMap<String, Room>();
    private AtomicInteger roomKey = new AtomicInteger();

    public Room getRoom(String id) {
        return roomManager.get(id);
    }

    public Room createRoom(String id) {
        Room room = new Room();
        roomKey.incrementAndGet();
        roomManager.put(String.valueOf(roomKey.get()), room);
        return room;
    }

    public void deleteRoom(String id) {
        roomManager.remove(id);
    }

}
