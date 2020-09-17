package com.ericliu.common.util.collections;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href=mailto:ericliu@fivewh.com>ericliu</a>,Date:2020/6/14
 */
public class MapUtils {

    /**
     * 将src和dst相同的key进行合并，支持递归
     *
     * @param src      src
     * @param dst      dst
     * @param maxDepth 深度
     * @return
     */
    public static Map mergeMap(Map src, Map dst, Integer maxDepth) {
        if (maxDepth < 0) {
            return src;
        }
        mergeMap(src, dst, 0, maxDepth);
        return src;
    }

    private static void mergeMap(Map src, Map dst, Integer depth, Integer max) {
        if (depth > max) {
            return;
        }

        for (Object dstK : dst.keySet()) {
            Object dstV = dst.get(dstK);
            Object srcV = src.get(dstK);
            if (dstV == null) {
                continue;
            }

            if (srcV == null) {
                src.put(dstK, dstV);
                continue;
            }

            if (!srcV.getClass().equals(dstV.getClass())) {
                continue;
            }

            if (srcV instanceof Map) {
                mergeMap((Map) srcV, (Map) dstV, depth + 1, max);
            } else if (srcV instanceof Collection) {
                ((Collection) srcV).addAll((Collection) dstV);
            } else {
                src.put(dstK, dstV);
            }
        }
    }
}
