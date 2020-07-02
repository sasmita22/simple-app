package com.example.simpleapp.model;

public class ResponseResult<T> {
    private Throwable error;
    private T result;

    public ResponseResult(Throwable error) {
        this.error = error;
    }

    public ResponseResult(T result) {
        this.result = result;
    }

    public Throwable getError() {
        return error;
    }

    public void setError(Exception error) {
        this.error = error;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }
}
