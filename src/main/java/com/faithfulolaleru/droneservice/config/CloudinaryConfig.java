package com.faithfulolaleru.droneservice.config;

import com.cloudinary.Cloudinary;
import io.github.cdimascio.dotenv.Dotenv;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AllArgsConstructor
public class CloudinaryConfig {

//    @Value("${cloud.name}")
//    private String cloudName;
//
//    @Value("${api.key}")
//    private String apiKey;
//
//    @Value("${api.secret}")
//    private String apiSecret;

    /*@Bean
    public Cloudinary cloudinary() {

        Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", cloudName,
                "api_key", apiKey,
                "api_secret", apiSecret));

        return cloudinary;
    }*/
    @Bean public Cloudinary cloudinary() {

        Dotenv dotenv = Dotenv.load();

        Cloudinary cloudinary = new Cloudinary(dotenv.get("CLOUDINARY_URL"));
        cloudinary.config.secure = true;
        // System.out.println(cloudinary.config.cloudName);

        return cloudinary;
    }

}
