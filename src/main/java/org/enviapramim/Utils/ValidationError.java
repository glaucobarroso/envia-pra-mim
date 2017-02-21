package org.enviapramim.Utils;

/**
 * Created by glauco on 21/02/17.
 */
public class ValidationError {
    public static final int SUCCESS = 0;
    public static final int FAIL = 100;

    private String message;
    private int code;

    public ValidationError(String message, int code) {
        this.message = message;
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
