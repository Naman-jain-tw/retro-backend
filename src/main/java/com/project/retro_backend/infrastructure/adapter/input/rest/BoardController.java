package com.project.retro_backend.infrastructure.adapter.input.rest;

import com.project.retro_backend.application.port.input.CreateBoardUseCase;
import com.project.retro_backend.application.port.input.JoinBoardUseCase;
import com.project.retro_backend.application.service.BoardService;
import com.project.retro_backend.domain.model.Board;
import com.project.retro_backend.domain.model.BoardUser;
import com.project.retro_backend.domain.model.UserToken;
import com.project.retro_backend.infrastructure.adapter.input.rest.dto.CreateBoardResponse;
import com.project.retro_backend.infrastructure.adapter.input.rest.dto.JoinBoardResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/boards")
@RequiredArgsConstructor
@Tag(name = "Board Management", description = "APIs for managing retrospective boards")
public class BoardController {
    private final CreateBoardUseCase createBoardUseCase;
    private final JoinBoardUseCase joinBoardUseCase;
    private final BoardService boardService;

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
        BoardUser boardUser = boardService.joinBoard(boardId, userName);
        UserToken userToken = boardService.generateUserToken(boardUser.getUser(), boardUser.getBoard());
        
        Map<String, Object> response = new HashMap<>();
        response.put("boardUser", boardUser);
        response.put("token", userToken.getToken());
        response.put("wsEndpoint", "/ws");
        response.put("boardTopic", "/topic/board/" + boardId);
        response.put("joinEndpoint", "/app/board/" + boardId + "/join");
        
        return ResponseEntity.ok(response);
    }
} 