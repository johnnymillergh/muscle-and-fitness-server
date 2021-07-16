package com.jmsoftware.maf.springcloudstarter.websocket;

import com.jmsoftware.maf.common.websocket.WebSocketMessagePayload;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

import java.util.Map;

/**
 * Description: ChatRoomController, change description here.
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com, date: 7/15/2021 5:24 PM
 **/
@Slf4j
@Controller
@RequiredArgsConstructor
public class ChatRoomController {
    private final ChatRoomService chatRoomService;

    @SneakyThrows
    @MessageMapping("/send-message")
    public void sendMessage(@Payload WebSocketMessagePayload payload, @Headers Map<String, Object> headers) {
        this.chatRoomService.sendMessage(payload, headers);
    }
}
