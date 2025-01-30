package com.project.retro_backend.infrastructure.adapter.input.rest;

import com.project.retro_backend.infrastructure.adapter.input.rest.dto.AddCardResponse;
import com.project.retro_backend.infrastructure.adapter.input.websocket.CardContent;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

import java.security.Principal;

@Controller
public class WebSocketController {
    @MessageMapping("/message")
    @SendTo("/topic/messages")
    public AddCardResponse getMessage(final CardContent content) throws InterruptedException {
        Thread.sleep(1000);
        return new AddCardResponse(HtmlUtils.htmlEscape(content.getCardContent()));
    }

    @MessageMapping("/private-message")
    @SendToUser("/topic/private-messages")
    public AddCardResponse getPrivateMessage(final CardContent content, final Principal principal)
            throws InterruptedException {
        Thread.sleep(1000);
        return new AddCardResponse(HtmlUtils
                .htmlEscape("Sending private message to: " + principal.getName() + " " + content.getCardContent()));
    }
}
