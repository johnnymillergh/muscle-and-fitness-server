package com.jmsoftware.maf.common.websocket;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * Description: WebSocketMessagePayload, change description here.
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com, date: 7/15/2021 5:35 PM
 **/
@Data
public class WebSocketMessageResponse {
    private final LocalDateTime timestamp = LocalDateTime.now();
    private String sender;
    private String receiver;
    private Object content;
}
