package com.meuorcamento.data;

public class DataAccessException extends RuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    String msg = "";

    public DataAccessException(String message, Throwable cause) {
        super(message, cause);
    }

    public DataAccessException(Throwable cause) {
        super(cause);
    }

    public DataAccessException(String message) {
        super(message);
    }

    public DataAccessException() {
        super();
    }

}
