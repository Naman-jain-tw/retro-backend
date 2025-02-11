package com.project.retro_backend.infrastructure.adapter.input.rest;

import com.project.retro_backend.application.port.input.CreateBoardUseCase;
import com.project.retro_backend.application.port.input.CreateCardUseCase;
import com.project.retro_backend.application.port.input.JoinBoardUseCase;
import com.project.retro_backend.domain.exception.BoardNotFoundException;
import com.project.retro_backend.domain.exception.UserNotFoundException;
import com.project.retro_backend.domain.model.Board;
import com.project.retro_backend.domain.model.BoardUser;
import com.project.retro_backend.domain.model.Card;
import com.project.retro_backend.domain.model.UserToken;
import com.project.retro_backend.infrastructure.adapter.input.rest.dto.CreateBoardResponse;
import com.project.retro_backend.infrastructure.adapter.input.rest.dto.JoinBoardResponse;
import com.project.retro_backend.infrastructure.adapter.input.websocket.CardContent;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/board")
@RequiredArgsConstructor
@Tag(name = "Board Management", description = "APIs for managing retrospective boards")
public class BoardController {
    private final CreateBoardUseCase createBoardUseCase;
    private final JoinBoardUseCase joinBoardUseCase;
    private final CreateCardUseCase createCardUseCase;

    @PostMapping
    @Operation(summary = "Create a new board",
            description = "Creates a new retrospective board with the given name and creator")
    @ApiResponse(responseCode = "200", description = "Board created successfully")
    public ResponseEntity<CreateBoardResponse> createBoard(
            @Parameter(description = "Name of the board") @RequestParam("name") String name,
            @Parameter(description = "Name of the board creator") @RequestParam("creatorName") String creatorName) {
        Board board = createBoardUseCase.createBoard(name, creatorName);
        
        CreateBoardResponse response = new CreateBoardResponse();
        response.setPublicId(board.getPublicId());
        response.setName(board.getName());
        
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{boardId}/join")
    @Operation(summary = "Join an existing board",
            description = "Allows a user to join an existing board. If user has already joined, " +
                    "their status will be updated to ACTIVE")
    @ApiResponse(responseCode = "200", description = "Successfully joined the board")
    @ApiResponse(responseCode = "404", description = "Board not found")
    public ResponseEntity<Map<String, Object>> joinBoard(
            @Parameter(description = "ID of the board to join") @PathVariable("boardId") UUID boardId,
            @Parameter(description = "Name of the user joining") @RequestParam("userName") String userName) {
        BoardUser boardUser = joinBoardUseCase.joinBoard(boardId, userName);
        UserToken userToken = joinBoardUseCase.generateUserToken(boardUser.getUser(), boardUser.getBoard());
        
        Map<String, Object> response = new HashMap<>();
        response.put("boardUser", boardUser);
        response.put("token", userToken.getToken());
        response.put("wsEndpoint", "/websocket");
        response.put("boardTopic", "/topic/board/" + boardId);
        response.put("joinEndpoint", "/app/board/" + boardId + "/join");
        
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{boardId}/note")
    @Operation(summary = "Add a card on an existing board",
            description = "Allows a user to add a card on an existing board. If the user has already joined the board, they can add a card.")
    @ApiResponse(responseCode = "200", description = "Successfully added a card to the board")
    @ApiResponse(responseCode = "404", description = "Board or User not found")
    public ResponseEntity<HttpStatus> addCard(
            @Parameter(description = "ID of the board to add a card to") @PathVariable("boardId") UUID boardId,
            @Parameter(description = "Name of the user adding the card") @RequestParam("userName") String userName,
            @Parameter(description = "Card content or message") @RequestBody final CardContent cardContent) {
        try {
            Card card = createCardUseCase.createCard(boardId, userName, cardContent.getCardContent());
            return ResponseEntity.ok().build(); // Return the created card in response
        } catch (BoardNotFoundException | UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Return 404 if board or user is not found
        }
    }

} 