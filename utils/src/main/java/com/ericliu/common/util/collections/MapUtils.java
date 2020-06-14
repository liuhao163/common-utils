package com.ericliu.common.util.collections;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href=mailto:ericliu@fivewh.com>ericliu</a>,Date:2020/6/14
 */
public class MapUtils {

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

    public static void main(String[] args) {
        Map a = new HashMap();
        a.put("a", "a");
        a.put("aint", 1);
        Map aChildMap = new HashMap();
        a.put("aMap", aChildMap);
        Map aaChildMap = new HashMap();
        aChildMap.put("a-child-3", aaChildMap);
        aChildMap.put("a-child-1", 1);
        aChildMap.put("a-child-2", "a-child");

        aaChildMap.put("a-child-child-2", "a-child-child");


        Map b = new HashMap();
        b.put("a", "b");
        b.put("aint", 2);
        b.put("bint", 3);
        Map bChildMap = new HashMap();
        b.put("aMap", bChildMap);
        Map bbChildMap = new HashMap();
        bChildMap.put("a-child-1", 11);
        bChildMap.put("a-child-2", "a-child-b");
        bChildMap.put("a-child-3", bbChildMap);
        bChildMap.put("b-child-4", "b-child-new");

        bbChildMap.put("a-child-child-2", "b-child-child-2");
        bbChildMap.put("bb-child-child-3", "bb-child-child-new");

        mergeMap(a, b,0);
        System.out.println(a);

    }
}
