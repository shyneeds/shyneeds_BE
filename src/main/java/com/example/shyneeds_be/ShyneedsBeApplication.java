package com.example.shyneeds_be;

import com.example.shyneeds_be.domain.travel_package.model.dto.request.TravelPackageRegisterRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class ShyneedsBeApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShyneedsBeApplication.class, args);
    }


    @Component
    public static class StringToTravelPackageRegisterInfo implements Converter<String, TravelPackageRegisterRequestDto> {

        @Autowired
        private ObjectMapper objectMapper;

        @Override
        @SneakyThrows
        public TravelPackageRegisterRequestDto convert(String source) {
            return objectMapper.readValue(source, TravelPackageRegisterRequestDto.class);
        }
    }
}
