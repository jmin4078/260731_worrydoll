package org.example.worrydoll.service;

import lombok.RequiredArgsConstructor;
import org.example.worrydoll.entity.ChatMessage;
import org.example.worrydoll.entity.ChatUser;
import org.example.worrydoll.repository.ChatMessageJpaRepository;
import org.example.worrydoll.repository.ChatMessageRepository;
import org.example.worrydoll.repository.ChatUserRepository;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
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
    // import org.springframework.ai.vectorstore.VectorStore;
    private final VectorStore vectorStore;

    @Transactional
    public void chat(String conversationId, String content) {
        // conversationId -> session (userId)
        chatClient.prompt()
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, conversationId))
                .user(content)
                .call() // 여기까지만 있으면 호출 X
                .content();
        // import org.springframework.ai.document.Document;
        vectorStore.add(List.of(
                Document.builder()
                        .text(content)
                        .metadata(Map.of("conversationId", conversationId))
                        .build()
        ));
    }

    private final ChatMessageJpaRepository jpaRepository;

    public List<ChatMessage> getChatMessages(String conversationId) {
        return jpaRepository.findAllByConversationId(conversationId);
    }

    @Qualifier("ragChatClient")
    private final ChatClient ragChatClient;

    public String search(String query, String conversationId) {
        return ragChatClient.prompt()
                .advisors(a -> a.param(
                        QuestionAnswerAdvisor.FILTER_EXPRESSION,
                        "conversationId == '%s'".formatted(conversationId)))
                .user(query)
                .call()
                .content();
    }
}