package com.ericliu.common.util;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: liuhaoeric
 * Create time: 2019/08/15
 * Description:
 */
public class ShortUrlGenerator {

    private static char[] charArray = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();

    public static String genaratorCode(String input) {

        int code = Math.abs(MurmurHash.murmurhash3_x86_32(input, 0, input.length(), 0));

        List<Integer> list = new ArrayList<>();

        StringBuilder sb = new StringBuilder();
        while (code > 0) {
            //取模
            sb.append(charArray[code % charArray.length]);
            //整除
            code = code / charArray.length;
        }

        return sb.toString();
    }

}
