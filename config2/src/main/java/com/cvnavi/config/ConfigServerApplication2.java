package com.cvnavi.config;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

/**
 * @description: spring cloud config
 * @author: zjbing
 * @create: 2019-03-28 11:30
 **/

@EnableConfigServer
@SpringCloudApplication
public class ConfigServerApplication2 {
	public static void main(String[] args) {
		SpringApplication.run(ConfigServerApplication2.class, args);
	}

}

