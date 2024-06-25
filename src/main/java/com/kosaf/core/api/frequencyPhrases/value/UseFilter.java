package com.kosaf.core.api.frequencyPhrases.value;

import com.kosaf.core.common.CodeType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UseFilter implements CodeType {

    Y("사용"), N("미사용");

    private final String description;
    
    @Override
    public String getCode() {
        return name();
    }

}
