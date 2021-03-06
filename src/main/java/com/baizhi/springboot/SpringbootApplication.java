package com.baizhi.springboot;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import tk.mybatis.spring.annotation.MapperScan;

import java.util.ArrayList;
import java.util.List;
@MapperScan(basePackages = "com.baizhi.springboot.mapper")
@SpringBootApplication
public class SpringbootApplication {
    public static void main(String[] args) {
        System.out.println("haha");
        SpringApplication.run(SpringbootApplication.class, args);
    }

    @Bean
    public HttpMessageConverters fastJsonHttpMessageConverters(){
        // 1.创建一个converter对象
        FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();
        // 2.创建配置对象
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        // 3.添加配置
        fastJsonConfig.setSerializerFeatures(SerializerFeature.PrettyFormat);
        // 4.将配置添加到转换器对象中
        fastConverter.setFastJsonConfig(fastJsonConfig);
        // 5. 解决中文乱码问题
        List<MediaType> mediaTypes = new ArrayList<MediaType>();
        mediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
        fastConverter.setSupportedMediaTypes(mediaTypes);
        // 6.将转换器对象转化为HttpMessageConverter对象
        HttpMessageConverter<?> converter = fastConverter;
        return new HttpMessageConverters(converter);
    }

}

