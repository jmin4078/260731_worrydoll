package org.example.worrydoll.service;

import lombok.RequiredArgsConstructor;
import org.example.worrydoll.entity.ChatUser;
import org.example.worrydoll.repository.ChatUserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final ChatUserRepository chatUserRepository;

    @Transactional
    public ChatUser getChatUser(String username) {
        // username을 기준으로 있으면 가져오고 없으면 만들어줌
        try {
            return chatUserRepository.findByUsername(username);
        } catch (NoSuchElementException e) {
            return chatUserRepository.save(
                    ChatUser.builder().username(username).build());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}