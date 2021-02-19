package com.lxc.utils;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Component;


/**
 * @author liuxianchun
 * @date 2021/1/25
 * MD5工具类
 */
@Component
public class MD5Util {

    public static String md5(String src){
        return DigestUtils.md5Hex(src);
    }

    private static final String salt = "1a2b3c4d";

    /*第一次加密,前端进行加密传到后端*/
    public static String inputPassToFormPass(String inputPass){
        String str = salt.charAt(0)+salt.charAt(3)+inputPass+salt.charAt(6)+salt.charAt(5);
        return md5(str);
    }

    /*第二次加密，后端到数据库前*/
    public static String formPassToDBPass(String formPass,String salt){
        String str = salt.charAt(0)+salt.charAt(3)+formPass+salt.charAt(6)+salt.charAt(5);
        return md5(str);
    }

    public static String inputPassToDBPass(String inputPass,String salt){
        String formPass = inputPassToFormPass(inputPass);
        return formPassToDBPass(formPass,salt);
    }

    public static void main(String[] args) {
        System.out.println(inputPassToFormPass("123456"));
        System.out.println(formPassToDBPass("09610badd7b2415fa0ac0b29d3a14dd9","1a2b3c4d"));
        System.out.println(inputPassToDBPass("123456","1a2b3c4d"));
    }

}
