package com.kosaf.core.config.websocket;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import com.kosaf.core.config.websocket.value.SendNm;
import com.kosaf.core.config.websocket.value.SocketStatus;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CustomWebSocketHandler extends TextWebSocketHandler {

  // 세션 관리 Map
  public static Map<String, WebSocketSession> sessionMap = new ConcurrentHashMap<>();
  // 사용자 Map (테스트를 위한)
  public static Map<String, String> userMap = new ConcurrentHashMap<>();

  /**
   * 웹 소켓 connection 연결
   */
  @Override
  public void afterConnectionEstablished(WebSocketSession session) {

    try {
      // 소켓 연결
      super.afterConnectionEstablished(session);
      // 메시지 길이제한 변경 (8192(8KB) -> 52428800(50MB))
      session.setTextMessageSizeLimit(50 * 1024 * 1024);

      // 세션 Map에 추가
      sessionMap.put(session.getId(), session);
      log.info("소켓 연결 세션 ID: " + session.getId());

      // 테스트를 위한 로직
      userMap.put(session.getId(), "사용자" + userMap.size());
      if (userMap.size() > 1) {
        JSONObject resObj = new JSONObject();
        resObj.put("status", SocketStatus.ON);
        resObj.put("sendNm", "SYSTEM");
        resObj.put("msg", "상대방이 입장했습니다.");
        sendMessageToSession(session, resObj.toString());
      }
    } catch (Exception e) {
      // 연결 오류 처리
      log.error("WebSocket Connection Error: " + e.getMessage());
    }
  }

  /**
   * 웹 소켓 connection 종료
   */
  @Override
  public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {

    try {
      // 세션 Map에서 제거
      sessionMap.remove(session.getId());
      // 소켓 종료
      super.afterConnectionClosed(session, status);
      log.info("소켓 종료(세션 삭제) 세션 ID: " + session.getId() + " 상태: " + status);

      // 테스트를 위한 로직
      userMap.remove(session.getId());
      if (userMap.size() <= 1) {
        JSONObject resObj = new JSONObject();
        resObj.put("status", SocketStatus.OFF);
        resObj.put("sendNm", SendNm.SYSTEM);
        resObj.put("msg", "상대방이 퇴장했습니다.");
        sendMessageToSession(session, resObj.toString());
      }
    } catch (Exception e) {
      // 종료 오류 처리
      log.error("WebSocket Close Error: " + e.getMessage());
    }
  }

  /**
   * 메시지 도착시
   */
  @Override
  public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) {

    try {
      // 테스트를 위한 로직
      String userNm = "(" + userMap.get(session.getId()) + ")";

      // 세션 Map의 모든 세션에 메시지 전송
      sessionMap.forEach((sessionId, sessionInMap) -> {
        try {
          JSONObject resObj = new JSONObject();
          resObj.put("status", SocketStatus.RUN);
          resObj
              .put("sendNm", (sessionId.equals(session.getId()) ? SendNm.SELF.getDescription()
                  : SendNm.OTHER.getDescription()) + userNm);
          resObj.put("msg", message.getPayload());
          sessionInMap.sendMessage(new TextMessage(resObj.toString()));

          // sessionInMap.sendMessage(message);
        } catch (IOException e) {
          // 메시지 전송 오류 처리
          log.error("WebSocket Message Send Error: " + e.getMessage());
        }
      });
    } catch (Exception e) {
      // 메시지 처리 오류 처리
      log.error("WebSocket Message Handling Error: " + e.getMessage());
    }
  }

  /**
   * 웹 소켓 통신 에러시
   */
  @Override
  public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
    try {
      super.handleTransportError(session, exception);
      // 통신 에러 처리
      log.error("WebSocket Transport Error: " + exception.getMessage());
    } catch (Exception e) {
      // 에러 처리 중 오류 발생 시 처리
      log.error("WebSocket Error Handling Error: " + e.getMessage());
    }
  }

  /**
   * 세션에 메시지 전송(테스트를 위한)
   */
  private void sendMessageToSession(WebSocketSession session, String message) {
    try {
      sessionMap.forEach((sessionId, sessionInMap) -> {
        try {
          sessionInMap.sendMessage(new TextMessage(message));
        } catch (IOException e) {
          // 메시지 전송 오류 처리
          log.error("WebSocket Message Send Error: " + e.getMessage());
        }
      });
    } catch (Exception e) {
      // 메시지 전송 중 오류 발생 시 처리
      log.error("WebSocket Message Sending Error: " + e.getMessage());
    }
  }
}
