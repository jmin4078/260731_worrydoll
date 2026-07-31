package org.example.worrydoll.controller;

import jakarta.servlet.http.HttpSession;
import org.example.worrydoll.dto.ChatUserSessionDTO;
import org.example.worrydoll.entity.ChatUser;
import org.example.worrydoll.service.ChatService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping
public class MainController {
    private final ChatService chatService;

    public MainController(ChatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping
    public String index() {
        return "index";
    }

    @PostMapping("/user")
    public String user(@RequestParam String username, HttpSession session) {
        ChatUser chatUser = chatService.getChatUser(username);
        session.setAttribute("chatUser",
                ChatUserSessionDTO.builder()
                        .userId(chatUser.getId())
                        .username(username).build());
        return "redirect:/";
    }
}