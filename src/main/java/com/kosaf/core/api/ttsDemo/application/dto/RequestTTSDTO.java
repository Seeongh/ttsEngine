package com.kosaf.core.api.ttsDemo.application.dto;

import lombok.*;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RequestTTSDTO {
    private String _TEXT;
    private String output_type;
    private String _DEFAULT_MODEL;
    private String _DEFAULT_SPEAKER;
    private String _DEFAULT_TEMPO;
    private String _DEFAULT_PITCH;
    private String _DEFAULT_GAIN;
    private String _CACHE = "read"; //남즈의 의견을 따라 저장하지 않는 read를 default로 전달
    private String _CONV_RATE;
}
