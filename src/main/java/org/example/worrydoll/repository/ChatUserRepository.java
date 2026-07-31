package org.example.worrydoll.repository;

import lombok.RequiredArgsConstructor;
import org.example.worrydoll.entity.ChatUser;
import org.springframework.stereotype.Repository;

import java.util.NoSuchElementException;

@Repository
@RequiredArgsConstructor
public class ChatUserRepository {
    private final ChatUserJpaRepository chatUserJpaRepository;

    public ChatUser save(ChatUser chatUser) {
        return chatUserJpaRepository.save(chatUser);
    }

    public ChatUser findByUsername(String username) throws NoSuchElementException {
        return chatUserJpaRepository.findByUsername(username).orElseThrow();
        // NoSuchElementException
    }
}