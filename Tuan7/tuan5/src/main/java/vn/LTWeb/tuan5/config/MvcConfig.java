package vn.LTWeb.tuan5.config;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Autowired
    private StorageProperties properties;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String uploadDir = properties.getLocation();
        Path uploadPath = Paths.get(uploadDir);

        registry.addResourceHandler("/images/**")
                .addResourceLocations(uploadPath.toUri().toString());
    }
}