package com.project.retro_backend.service;

import com.project.retro_backend.domain.*;
import com.project.retro_backend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;

@Service
public class BoardService {
    @Autowired
    private BoardRepository boardRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private BoardUserRepository boardUserRepository;

    @Transactional
    public Board createBoard(String name, String creatorName) {
        // Create new user (admin)
        User admin = new User();
        admin.setName(creatorName);
        admin = userRepository.save(admin);
        
        // Create board
        Board board = new Board();
        board.setName(name);
        board = boardRepository.save(board);
        
        // Create board-user relationship
        BoardUser boardUser = new BoardUser();
        boardUser.setBoard(board);
        boardUser.setUser(admin);
        boardUser.setRole(UserRole.ADMIN);
        boardUserRepository.save(boardUser);
        
        return board;
    }
    
    @Transactional
    public BoardUser joinBoard(UUID boardPublicId, String userName) {
        Board board = boardRepository.findByPublicId(boardPublicId)
            .orElseThrow(() -> new RuntimeException("Board not found"));
            
        User user = new User();
        user.setName(userName);
        user = userRepository.save(user);
        
        BoardUser boardUser = new BoardUser();
        boardUser.setBoard(board);
        boardUser.setUser(user);
        boardUser.setRole(UserRole.USER);
        
        return boardUserRepository.save(boardUser);
    }
} 