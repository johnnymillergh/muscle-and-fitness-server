package com.jmsoftware.maf.springcloudstarter.websocket;

import com.jmsoftware.maf.common.websocket.WebSocketMessagePayload;
import org.springframework.validation.annotation.Validated;

import java.util.Map;

/**
 * Description: ChatRoomService, change description here.
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com, date: 7/16/2021 8:14 AM
 **/
@Validated
public interface ChatRoomService {
    /**
     * Send message.
     *
     * @param payload the payload
     * @param headers the headers
     */
    void sendMessage(WebSocketMessagePayload payload, Map<String, Object> headers);
}
