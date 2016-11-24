package com.lljqiu.generator.utils;

public class DMException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    public DMException(String errorMessage) {
        this(errorMessage, null);
    }
    

    public DMException(String errorMessage, Throwable cause) {
        super(errorMessage, cause);
    }
    
    public static void checkCondition(boolean expression, String errorMessage) {
        if (expression) {
            throw new DMException(errorMessage);
        }
    }

}
