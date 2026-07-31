package org.example.worrydoll.service;

import lombok.RequiredArgsConstructor;
import org.example.worrydoll.entity.ChatMessage;
import org.example.worrydoll.entity.ChatUser;
import org.example.worrydoll.repository.ChatMessageJpaRepository;
import org.example.worrydoll.repository.ChatUserRepository;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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

    private final ChatClient chatClient;

    @Transactional
    public void chat(String conversationId, String content) {
        // conversationId -> session (userId)
        chatClient.prompt()
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, conversationId))
                .user(content)
                .call()
                .content();
    }

    private final ChatMessageJpaRepository jpaRepository;

    public List<ChatMessage> getChatMessages(String conversationId) {
        return jpaRepository.findAllByConversationId(conversationId);
    }
}