package com.site.blog.my.core.controller.common;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

/**
 * @ProjectName: my-blog
 * @ClassName: UploadConfig
 * @Auther: wczy
 * @Date: 2020-11-18 14:25
 * @Version 设置上传图片的最大尺寸
 **/
@Component
public class UploadConfig {
    @Bean
    public CommonsMultipartResolver getC(){
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
        multipartResolver.setMaxUploadSize(999999999L);
        return multipartResolver;
    }
}
