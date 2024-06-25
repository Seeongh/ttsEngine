package com.kosaf.core.api.monitoring.application.dto;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class ResponseBodyDto {
        private int code;
        private String message;
        private Map<String, Object> result = new HashMap<>();

        // Getters and Setters

        @JsonAnySetter
        public void setResult(String key, Object value) {
                this.result.put(key, value);
        }

}
