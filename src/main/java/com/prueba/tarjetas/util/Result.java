package com.prueba.tarjetas.util;

import java.util.List;

public class Result {
    
    public boolean correct;
    public String errorMessage;
    public Exception ex;
    public Object object;
    public List<Object> objects;
    
    public Result() {
        this.correct = true;
    }
    
    public Result(boolean correct) {
        this.correct = correct;
    }
    
    public Result(boolean correct, String errorMessage) {
        this.correct = correct;
        this.errorMessage = errorMessage;
    }
    
    public Result(Object object) {
        this.correct = true;
        this.object = object;
    }
    
    public Result(List<Object> objects) {
        this.correct = true;
        this.objects = objects;
    }
    
    public static Result success() {
        return new Result(true);
    }
    
    public static Result success(Object object) {
        return new Result(object);
    }
    
    public static Result success(List<Object> objects) {
        return new Result(objects);
    }
    
    public static Result error(String errorMessage) {
        return new Result(false, errorMessage);
    }
    
    public static Result error(String errorMessage, Exception ex) {
        Result result = new Result(false, errorMessage);
        result.ex = ex;
        return result;
    }
}
