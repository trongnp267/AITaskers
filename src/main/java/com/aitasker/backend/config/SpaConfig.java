package com.aitasker.backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

import java.io.IOException;

/**
 * Phuc vu frontend Next.js (trang tinh trong resources/static) cho moi duong
 * dan khong phai /api. Vi Next xuat moi route thanh {route}/index.html, resolver
 * nay se: uu tien file that -> thu {path}/index.html -> cuoi cung fallback ve
 * index.html goc de client-router tu render dung trang.
 */
@Configuration
public class SpaConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/")
                .resourceChain(true)
                .addResolver(new PathResourceResolver() {
                    @Override
                    protected Resource getResource(String resourcePath, Resource location) throws IOException {
                        Resource requested = location.createRelative(resourcePath);
                        if (requested.exists() && requested.isReadable()) {
                            return requested;
                        }
                        Resource indexed = location.createRelative(
                                resourcePath.isEmpty() ? "index.html" : resourcePath + "/index.html");
                        if (indexed.exists() && indexed.isReadable()) {
                            return indexed;
                        }
                        Resource root = location.createRelative("index.html");
                        return (root.exists() && root.isReadable()) ? root : null;
                    }
                });
    }
}
