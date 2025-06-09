package com.zzt.blog;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author 227
 */ // 启用SpringDoc自动配置
@MapperScan("com.zzt.blog.mapper")
@SpringBootApplication
//@EnableDiscoveryClient
public class BlogApplication {

	public static void main(String[] args) {
		SpringApplication.run(BlogApplication.class, args);
	}

}
