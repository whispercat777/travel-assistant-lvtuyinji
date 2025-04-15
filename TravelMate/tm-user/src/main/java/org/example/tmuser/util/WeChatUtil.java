package org.example.tmuser.util;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

import java.util.HashMap;

public class WeChatUtil {
    public static JSONObject getSessionKeyOrOpenId(String code) {
        // 微信小程序官方接口
        String requestUrl = "https://api.weixin.qq.com/sns/jscode2session";

        // 接口所需参数
        HashMap<String, Object> requestUrlParam = new HashMap<>();
        requestUrlParam.put("appid", "wx898e6fb434083cf9");
        requestUrlParam.put("secret", "28a0d88336884eaea453be993a21c53f");
        requestUrlParam.put("js_code", code);
        requestUrlParam.put("grant_type", "authorization_code");

        // 发送 HTTP 请求
        String result = HttpUtil.get(requestUrl, requestUrlParam);
        JSONObject jsonObject = JSONUtil.parseObj(result);

        // 检查接口返回的错误码
        if (jsonObject.containsKey("errcode")) {
            String errorCode = jsonObject.get("errcode", String.class);
            String errorMsg = jsonObject.get("errmsg", String.class);
            // 处理错误
            System.out.println("Error: " + errorCode + " - " + errorMsg);
            return null; // 返回空或处理错误逻辑
        }

        // 成功获取到 openid
        String openid = jsonObject.get("openid", String.class);
        return jsonObject;
    }

}
