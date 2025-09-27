package com.example.demo.config;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class CloudinaryConfig {


    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "dpdg2zia4",
                "api_key", "941641111126165",
                "api_secret", "0y38ZhsWMeeNHvXRypCNCp3Zlvw"
        ));
    }
}
