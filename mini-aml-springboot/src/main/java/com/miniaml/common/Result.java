package com.miniaml.common;

public class Result<T> {
    private Integer code;
    private String msg;
    private T data;

    // 构造器
    public Result(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    // 成功时的快捷方法
    public static <T> Result<T> success(T data) {
        return new Result<>(200, "操作成功", data);
    }

    // 失败时的快捷方法
    public static <T> Result<T> error(String msg) {
        return new Result<>(500, msg, null);
    }

    // Getter 和 Setter（方便 Jackson 转 JSON）
    public Integer getCode() { return code; }
    public void setCode(Integer code) { this.code = code; }
    public String getMsg() { return msg; }
    public void setMsg(String msg) { this.msg = msg; }
    public T getData() { return data; }
    public void setData(T data) { this.data = data; }
}