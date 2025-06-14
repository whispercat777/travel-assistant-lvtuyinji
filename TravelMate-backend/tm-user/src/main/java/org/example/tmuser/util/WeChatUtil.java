package org.example.tmuser.util;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

import java.util.HashMap;

/**
 * 微信小程序登录工具类。
 * 封装调用微信 openid 获取接口的请求逻辑，用于小程序登录认证流程。
 */
public class WeChatUtil {

    /**
     * 调用微信 API，根据前端传入的 code 获取 openID 和 session_key。
     *
     * @param code 小程序端调用 wx.login 获取的临时登录凭证
     * @return 包含 openid 和 session_key 的 JSON 对象，失败返回 null
     */
    public static JSONObject getSessionKeyOrOpenId(String code) {
        // 微信官方提供的 openid 获取接口地址
        String requestUrl = "https://api.weixin.qq.com/sns/jscode2session";

        // 设置请求参数
        HashMap<String, Object> requestUrlParam = new HashMap<>();
        requestUrlParam.put("appid", "wx898e6fb434083cf9"); // 小程序的 AppID
        requestUrlParam.put("secret", "28a0d88336884eaea453be993a21c53f"); // 小程序的 AppSecret
        requestUrlParam.put("js_code", code); // 前端传入的 code
        requestUrlParam.put("grant_type", "authorization_code"); // 授权类型固定值

        // 发送 GET 请求
        String result = HttpUtil.get(requestUrl, requestUrlParam);

        // 解析响应 JSON
        JSONObject jsonObject = JSONUtil.parseObj(result);

        // 错误处理：如果响应包含错误码
        if (jsonObject.containsKey("errcode")) {
            String errorCode = jsonObject.get("errcode", String.class);
            String errorMsg = jsonObject.get("errmsg", String.class);
            System.out.println("Error: " + errorCode + " - " + errorMsg);
            return null;
        }

        // 成功返回：包含 openid 和 session_key
        return jsonObject;
    }
}
