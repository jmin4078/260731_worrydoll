package org.example.worrydoll.controller;

import jakarta.servlet.http.HttpSession;
import org.example.worrydoll.dto.ChatUserSessionDTO;
import org.example.worrydoll.entity.ChatUser;
import org.example.worrydoll.service.ChatService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping
public class MainController {
    private final ChatService chatService;

    public MainController(ChatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping
    public String index(Model model, HttpSession session) {
        ChatUserSessionDTO chatUserSessionDTO = (ChatUserSessionDTO) session.getAttribute("chatUser");
        if (chatUserSessionDTO != null) {
            model.addAttribute("chats",
                    chatService.getChatMessages(
                            Long.toString(chatUserSessionDTO.getUserId())));
        }
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

    @PostMapping("/chat")
    public String chat(@RequestParam String content, HttpSession session) {
        ChatUserSessionDTO chatUserSessionDTO = (ChatUserSessionDTO) session.getAttribute("chatUser");
        String conversationId = Long.toString(chatUserSessionDTO.getUserId());
        chatService.chat(conversationId, content);
        return "redirect:/";
    }

    @PostMapping("/search")
    public String search(@RequestParam String query,
                         HttpSession session,
                         RedirectAttributes redirectAttributes) {
        ChatUserSessionDTO chatUserSessionDTO = (ChatUserSessionDTO) session.getAttribute("chatUser");
        String conversationId = Long.toString(chatUserSessionDTO.getUserId());
        redirectAttributes.addFlashAttribute("search",
                chatService.search(query, conversationId));
        return "redirect:/";
    }
}