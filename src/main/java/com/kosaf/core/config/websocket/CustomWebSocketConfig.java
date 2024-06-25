package com.kosaf.core.config.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class CustomWebSocketConfig implements WebSocketConfigurer {

  @Autowired
  CustomWebSocketHandler customWebSocketHandler;

  @Override
  public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
    registry
        .addHandler(customWebSocketHandler, "/websocket") // 사용할 WebSocketHandler 및 엔드포인트 URL을 등록
        .setAllowedOriginPatterns("*") // 모든 Origin으로부터의 요청을 허용
        .withSockJS()
        .setClientLibraryUrl(
            "https://cdnjs.cloudflare.com/ajax/libs/sockjs-client/1.1.5/sockjs.min.js");
    // SockJS 클라이언트 라이브러리의 URL을 설정
  }
}
