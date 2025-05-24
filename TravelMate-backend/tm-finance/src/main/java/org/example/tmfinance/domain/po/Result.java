package org.example.tmfinance.domain.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 通用响应结果封装类。
 * 用于统一返回接口调用的状态码、提示信息和数据内容。
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
     * 响应信息，描述本次请求处理的结果。
     */
    private String msg;

    /**
     * 响应数据载体，可为任何对象（如实体、列表、分页等）。
     */
    private Object data;

    /**
     * 用于新增、删除、修改等不返回数据的成功响应。
     *
     * @return 成功的 Result 对象，data 为 null
     */
    public static Result success() {
        return new Result(1, "success", null);
    }

    /**
     * 用于查询操作的成功响应。
     *
     * @param data 返回的数据对象
     * @return 成功的 Result 对象，包含 data 内容
     */
    public static Result success(Object data) {
        return new Result(1, "success", data);
    }

    /**
     * 用于任何操作失败时的错误响应。
     *
     * @param msg 错误信息描述
     * @return 失败的 Result 对象，data 为 null
     */
    public static Result error(String msg) {
        return new Result(0, msg, null);
    }
}
