package com.autulin.library.http;

/**
 * Created by yefeng on 20/01/2017.
 * http response
 */

public class HttpRes<T> {
    private int code;
    private String msg;

    public T getT() {
        return t;
    }

    public void setT(T data) {
        this.t = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    private T t;

    @Override
    public String toString() {
        return "HttpRes{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + t +
                '}';
    }
}
