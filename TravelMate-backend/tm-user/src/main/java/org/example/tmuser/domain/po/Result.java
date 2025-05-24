package org.example.tmuser.domain.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 通用接口响应封装类。
 * 用于统一 REST 接口的返回结构，包括状态码、消息提示和响应数据。
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
     * 响应信息，用于描述返回状态或错误提示。
     */
    private String msg;

    /**
     * 实际返回的数据内容，可以是对象、集合或基本类型。
     */
    private Object data;

    /**
     * 增删改类操作的成功响应（无返回数据）。
     *
     * @return 成功的 Result 对象，data 为 null
     */
    public static Result success() {
        return new Result(1, "success", null);
    }

    /**
     * 查询类操作的成功响应（包含返回数据）。
     *
     * @param data 返回的数据对象
     * @return 成功的 Result 对象，含 data 内容
     */
    public static Result success(Object data) {
        return new Result(1, "success", data);
    }

    /**
     * 操作失败时的响应结果。
     *
     * @param msg 错误提示信息
     * @return 失败的 Result 对象，data 为 null
     */
    public static Result error(String msg) {
        return new Result(0, msg, null);
    }
}
