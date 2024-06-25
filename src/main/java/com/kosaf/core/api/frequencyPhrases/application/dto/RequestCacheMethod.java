package com.kosaf.core.api.frequencyPhrases.application.dto;

public enum RequestCacheMethod {
    OFF, WRITE;
    
    public static String getRollbackMethod(String failedMethod) {
        //실패한 메소드가 등록하기 이면 삭제를, 삭제면 등록을 리턴
        return (failedMethod.equals(WRITE.name()))? OFF.name() : WRITE.name();
    }
}
