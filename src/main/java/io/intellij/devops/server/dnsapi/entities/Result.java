package io.intellij.devops.server.dnsapi.entities;

import lombok.Getter;

/**
 * Result
 *
 * @author tech@intellij.io
 */
@Getter
public class Result<T> {
    private final int code;
    private final String msg;
    private final T data;

    private Result(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static <T> Result<T> ok(T data) {
        return new Result<>(200, "", data);
    }

    public static <T> Result<T> fail(String msg) {
        return new Result<>(9999, msg, null);
    }

}
