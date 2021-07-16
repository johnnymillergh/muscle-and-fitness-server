package com.jmsoftware.maf.springcloudstarter.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jmsoftware.maf.common.websocket.WebSocketMessagePayload;
import com.jmsoftware.maf.common.websocket.WebSocketMessageResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Description: ChatRoomServiceImpl, change description here.
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com, date: 7/16/2021 8:14 AM
 **/
@Slf4j
@Service
@RequiredArgsConstructor
public class ChatRoomServiceImpl implements ChatRoomService {
    public static final String BROADCAST_CHANNEL = "/topic/public/broadcast";
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final ObjectMapper objectMapper;

    @Override
    @SneakyThrows
    public void sendMessage(WebSocketMessagePayload payload, Map<String, Object> headers) {
        val response = new WebSocketMessageResponse();
        response.setSender(payload.getSender());
        response.setReceiver(payload.getReceiver());
        response.setContent(payload.getContent());
        this.simpMessagingTemplate.convertAndSend(BROADCAST_CHANNEL, this.objectMapper.writeValueAsString(response));
        log.info("Got message from client. simpSessionId: {}, payload: {}", headers.get("simpSessionId"), payload);
    }
}
