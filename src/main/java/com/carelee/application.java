package com.carelee;

import org.apache.catalina.Context;
import org.apache.tomcat.websocket.server.WsSci;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatContextCustomizer;
import org.springframework.context.annotation.Bean;

/**
 * Package: PACKAGE_NAME
 * Description： TODO
 * Author: CareLee
 * Date: Created in 2020/12/11 10:50
 * Copyright: Copyright (c) 2040
 */
@SpringBootApplication
public class application {
    public static void main(String[] args) {
        SpringApplication.run(application.class,args);
    }

    /**
     * 创建wss协议接口
     *
     * @return
     */
    @Bean
    public TomcatContextCustomizer tomcatContextCustomizer(){
        System.out.println("tomcatContextCustomizer");
        return new TomcatContextCustomizer() {
            public void customize(Context context) {
                context.addServletContainerInitializer(new WsSci(),null);
            }
        };

    }
}
