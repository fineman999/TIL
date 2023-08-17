package org.hello.chapter05.item26.genericdao;

import java.util.Optional;
import java.util.Set;

public class MessageRepository {
    private Set<Message> messages;

    public MessageRepository(Set<Message> messages) {
        this.messages = messages;
    }

    public Optional<Message> findById(Long id) {
        return messages.stream().filter(m -> m.getId().equals(id)).findFirst();
    }

    public void add(Message message) {
        messages.add(message);
    }
}
