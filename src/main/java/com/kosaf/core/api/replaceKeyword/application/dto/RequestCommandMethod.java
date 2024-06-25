package com.kosaf.core.api.replaceKeyword.application.dto;

public enum RequestCommandMethod {
    insert, delete;
    
    public static String getRollbackMethod(String failedMethod) {
        //실패한 메소드가 등록하기 이면 삭제를, 삭제면 등록을 리턴
        return (failedMethod.equals(insert.name()))? delete.name() : insert.name();
    }
}
