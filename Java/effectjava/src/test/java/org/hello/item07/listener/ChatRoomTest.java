package org.hello.item07.listener;

import org.hello.chapter01.item07.listener.ChatRoom;
import org.hello.chapter01.item07.listener.User;
import org.junit.jupiter.api.Test;

import java.lang.ref.WeakReference;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ChatRoomTest {


    @Test
    void chatRoom() throws InterruptedException {
        ChatRoom chatRoom = new ChatRoom();
        User user1 = new User();
        User user2 = new User();
        chatRoom.addUser(user1);
        chatRoom.addUser(user2);

        chatRoom.sendMessage("hello");

        user1 = null;
        System.gc();
        Thread.sleep(5000L);

        List<WeakReference<User>> users = chatRoom.getUsers();
        assertThat(users.size()).isEqualTo(1);
    }
}