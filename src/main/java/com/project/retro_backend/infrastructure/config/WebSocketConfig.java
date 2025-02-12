package com.project.retro_backend.infrastructure.config;

import com.project.retro_backend.infrastructure.adapter.input.websocket.UserHandshakeHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(final MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic"); // Server-to-client broadcasts
        registry.setApplicationDestinationPrefixes("/app"); // Client-to-server messages
    }

    @Override
    public void registerStompEndpoints(final StompEndpointRegistry registry) {
        registry.addEndpoint("/websocket")
                .setAllowedOrigins("https://retro-backend-service-887081604066.asia-south1.run.app", "http://localhost:8081", "http://localhost:5173")
                .setHandshakeHandler(new UserHandshakeHandler())
                .withSockJS();
    }

}