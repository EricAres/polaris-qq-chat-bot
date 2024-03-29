package yuki.mirai.demo.utils;


import yuki.mirai.demo.Set;

import java.util.HashMap;
import java.util.Map;

public class TransApi {
    private static final String TRANS_API_HOST = "http://api.fanyi.baidu.com/api/trans/vip/translate";

    private String appid;
    private String securityKey;

    public TransApi(String appid, String securityKey) {
        this.appid = appid;
        this.securityKey = securityKey;
    }

    public static String getTransResult(String query, String from, String to) {
        Map<String, String> params = buildParams(query, from, to);
        return HttpGet.get(TRANS_API_HOST, params);
    }

    private static Map<String, String> buildParams(String query, String from, String to) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("q", query);
        params.put("from", from);
        params.put("to", to);

        params.put("appid", Set.CONFIG.FunctionSet.BaiduTranslate.BaiduTranslateAPPID);

        // 随机数
        String salt = String.valueOf(System.currentTimeMillis());
        params.put("salt", salt);

        // 签名
        String src = Set.CONFIG.FunctionSet.BaiduTranslate.BaiduTranslateAPPID + query + salt + Set.CONFIG.FunctionSet.BaiduTranslate.BaiduTranslateKey; // 加密前的原文
        params.put("sign", MD5.md5(src));

        return params;
    }

}
