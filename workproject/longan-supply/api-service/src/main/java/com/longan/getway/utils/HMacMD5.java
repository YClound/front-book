package com.longan.getway.utils;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
public class HMacMD5 {
    public static String sign(String auth, String secretKey){
        try {           
            SecretKey sharedKey = new SecretKeySpec(
                    Base64.decodeBase64(secretKey), "HmacMD5");
            Mac messageSigner = Mac.getInstance("HmacMD5");
            messageSigner.init(sharedKey);
            return Base64.encodeBase64String(messageSigner.doFinal(auth.getBytes("utf-8")));
        } catch (Exception ex) {
            return null;
        }
    }
}
