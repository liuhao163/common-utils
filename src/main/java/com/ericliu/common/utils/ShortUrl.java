package com.ericliu.common.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: liuhaoeric
 * Create time: 2019/08/15
 * Description:
 */
public class ShortUrl {

    private static String  url="https://github.com/wangzheng0822/ratelimiter4j";


    public static void main(String[] args) {

        int code=MurmurHash.murmurhash3_x86_32(url,0,url.length(),0);

        System.out.println(code);
        List<Integer> list = new ArrayList<>();

        int i = 181338494;
        while (i > 0) {
            list.add(i % 62);
            i = i / 62;
        }
        System.out.println(list);
    }
}
