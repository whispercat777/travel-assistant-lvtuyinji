package org.example.tmplan.domain.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 通用响应封装类。
 * 用于统一 REST API 接口的响应结构，包括状态码、提示信息和响应数据。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result {

    /**
     * 响应状态码：1 表示成功，0 表示失败。
     */
    private Integer code;

    /**
     * 响应提示信息，例如 "success" 或失败原因。
     */
    private String msg;

    /**
     * 实际响应数据内容，可为任意对象。
     */
    private Object data;

    /**
     * 增删改操作的成功响应（无返回数据）。
     *
     * @return 成功的 Result 对象，data 为空
     */
    public static Result success() {
        return new Result(1, "success", null);
    }

    /**
     * 查询操作的成功响应（包含返回数据）。
     *
     * @param data 查询得到的数据
     * @return 成功的 Result 对象，含数据
     */
    public static Result success(Object data) {
        return new Result(1, "success", data);
    }

    /**
     * 失败响应，带错误提示信息。
     *
     * @param msg 错误信息
     * @return 失败的 Result 对象，data 为 null
     */
    public static Result error(String msg) {
        return new Result(0, msg, null);
    }
}
