package com.goggles.user_service.user.infrastructure.persistence.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Converter(autoApply = true)
@RequiredArgsConstructor
public class ProfileConverter implements AttributeConverter<List<? extends Enum<?>>, String> {

    private final ObjectMapper objectMapper;

    @Override
    public String convertToDatabaseColumn(List<? extends Enum<?>> attribute) {

        if (attribute != null && !attribute.isEmpty()) {
            try {
                return objectMapper.writeValueAsString(attribute); // JSON 문자열 변환
            } catch (JsonProcessingException e) {
                log.error("Converting Error: {}", e.getMessage(), e);
            }
        }

        return null;
    }

    @Override
    public List<? extends Enum<?>> convertToEntityAttribute(String dbData) {
        if (StringUtils.hasText(dbData)) {
            try {
                return objectMapper.readValue(dbData, new TypeReference<>() {});
            } catch (JsonProcessingException e) {
                log.error("Converting Error: {}", e.getMessage(), e);
            }
        }

        return new ArrayList<>();
    }
}
