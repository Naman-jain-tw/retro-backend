package com.project.retro_backend.controller;

import com.project.retro_backend.domain.Board;
import com.project.retro_backend.domain.BoardUser;
import com.project.retro_backend.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;

@RestController
@RequestMapping("/api/boards")
@Tag(name = "Board Management", description = "APIs for managing retrospective boards")
public class BoardController {

    @Autowired
    private BoardService boardService;

    @Operation(
        summary = "Create a new board",
        description = "Creates a new retrospective board with the specified name and creator"
    )
    @ApiResponse(
        responseCode = "200",
        description = "Board created successfully",
        content = @Content(schema = @Schema(implementation = Board.class))
    )
    @PostMapping
    public ResponseEntity<Board> createBoard(
            @Parameter(description = "Name of the board") @RequestParam("name") String name,
            @Parameter(description = "Name of the board creator") @RequestParam("creatorName") String creatorName) {
        Board board = boardService.createBoard(name, creatorName);
        return ResponseEntity.ok(board);
    }
    
    @Operation(
        summary = "Join an existing board",
        description = "Allows a user to join an existing board using the board's UUID"
    )
    @ApiResponse(
        responseCode = "200",
        description = "Successfully joined the board",
        content = @Content(schema = @Schema(implementation = BoardUser.class))
    )
    @ApiResponse(
        responseCode = "404",
        description = "Board not found"
    )
    @PostMapping("/{boardId}/join")
    public ResponseEntity<BoardUser> joinBoard(
            @Parameter(description = "UUID of the board to join") @PathVariable("boardId") UUID boardId,
            @Parameter(description = "Name of the user joining") @RequestParam("userName") String userName) {
        BoardUser boardUser = boardService.joinBoard(boardId, userName);
        return ResponseEntity.ok(boardUser);
    }
} 