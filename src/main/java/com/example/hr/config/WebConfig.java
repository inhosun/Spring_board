package com.example.hr.config;

import com.example.hr.service.PhotoStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final PhotoStorageService photoStorage;

    /** http://localhost:8080/photos/xxx.jpg 요청을 업로드 폴더의 파일로 연결 */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/photos/**")
                .addResourceLocations(photoStorage.getRoot().toUri().toString());
    }
}
