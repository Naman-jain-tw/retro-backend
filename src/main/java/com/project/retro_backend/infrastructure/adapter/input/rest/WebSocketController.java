package com.project.retro_backend.infrastructure.adapter.input.rest;

import com.project.retro_backend.application.service.BoardService;
import com.project.retro_backend.application.service.TimerService;
import com.project.retro_backend.domain.exception.BoardNotFoundException;
import com.project.retro_backend.infrastructure.adapter.input.rest.dto.AddCardResponse;
import com.project.retro_backend.infrastructure.adapter.input.websocket.CardContent;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

import java.security.Principal;
import java.util.UUID;

@Controller
public class WebSocketController {

    private BoardService boardService;

    private TimerService timerService;

    @MessageMapping("/board/{boardId}/message")
    @SendTo("/topic/board/{boardId}/messages")
    public AddCardResponse getMessage(final CardContent content, @DestinationVariable String boardId)
            throws InterruptedException {
        if (!boardService.boardExists(UUID.fromString(boardId))) {
            throw new BoardNotFoundException("Board not found");
        }
        return new AddCardResponse(HtmlUtils.htmlEscape(content.getCardContent()));
    }

    @MessageMapping("/private-message")
    @SendToUser("/topic/private-messages")
    public AddCardResponse getPrivateMessage(final CardContent content, final Principal principal)
            throws InterruptedException {
        return new AddCardResponse(HtmlUtils
                .htmlEscape("Sending private message to: " + principal.getName() + " " + content.getCardContent()));
    }

    @MessageMapping("/board/{boardId}/timer/start")
    @SendTo("/topic/board/{boardId}/timer")
    public void startTimer(@DestinationVariable String boardId, @Payload int duration) {
        timerService.startTimer(UUID.fromString(boardId), duration);
    }

    @MessageMapping("/board/{boardId}/timer/stop")
    @SendTo("/topic/board/{boardId}/timer")
    public void stopTimer(@DestinationVariable String boardId) {
        timerService.stopTimer(UUID.fromString(boardId));
    }

    @MessageMapping("/board/{boardId}/timer/cancel")
    @SendTo("/topic/board/{boardId}/timer")
    public void cancelTimer(@DestinationVariable String boardId) {
        timerService.cancelTimer(UUID.fromString(boardId));
    }
}
