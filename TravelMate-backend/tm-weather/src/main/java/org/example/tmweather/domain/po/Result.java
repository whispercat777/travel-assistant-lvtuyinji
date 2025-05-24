package org.example.tmweather.domain.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 通用接口响应封装类。
 * 用于统一 REST 接口的返回结构，包含状态码、消息提示和返回数据。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result {

    /**
     * 响应状态码：
     * - 1 表示成功
     * - 0 表示失败
     */
    private Integer code;

    /**
     * 响应提示信息，通常用于描述操作结果或错误原因。
     */
    private String msg;

    /**
     * 响应返回的数据对象，可为任意类型，如实体、集合或 null。
     */
    private Object data;

    /**
     * 增删改类操作成功的返回结果（无数据）。
     *
     * @return 只包含成功标志的 Result 对象
     */
    public static Result success() {
        return new Result(1, "success", null);
    }

    /**
     * 查询类操作成功的返回结果（包含数据）。
     *
     * @param data 返回的数据对象
     * @return 包含数据的 Result 对象
     */
    public static Result success(Object data) {
        return new Result(1, "success", data);
    }

    /**
     * 操作失败的返回结果。
     *
     * @param msg 错误提示信息
     * @return 错误的 Result 对象
     */
    public static Result error(String msg) {
        return new Result(0, msg, null);
    }
}
