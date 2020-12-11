package com.carelee.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * Package: com.carelee.config
 * Description： TODO
 * Author: CareLee
 * Date: Created in 2020/12/11 11:14
 * Copyright: Copyright (c) 2040
 */
@Configuration
public class WebSocketConfig {
    @Bean
    public ServerEndpointExporter serverEndpointExporter(){
        return new ServerEndpointExporter();
    }
}
