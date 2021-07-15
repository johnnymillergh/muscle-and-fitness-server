package com.jmsoftware.maf.springcloudstarter.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jmsoftware.maf.common.websocket.WebSocketMessagePayload;
import com.jmsoftware.maf.common.websocket.WebSocketMessageResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.Map;

/**
 * Description: GreetingController, change description here.
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com, date: 7/15/2021 5:24 PM
 **/
@Slf4j
@Controller
@RequiredArgsConstructor
public class GreetingController {
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final ObjectMapper objectMapper;

    @SneakyThrows
    @MessageMapping("/send-message")
    public void sendMessage(@Payload WebSocketMessagePayload payload, @Headers Map<String, Object> headers) {
        val response = new WebSocketMessageResponse();
        response.setSender(payload.getSender());
        response.setReceiver(payload.getReceiver());
        response.setContent(headers.get("simpSessionId").toString() + "---" + payload.getContent());
        this.simpMessagingTemplate.convertAndSend("/topic/public/broadcast",
                                                  this.objectMapper.writeValueAsString(response));
    }
}
