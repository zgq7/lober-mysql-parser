package com.lober.mysql.parser;

import org.junit.Test;

/**
 * @author liaonanzhou
 * @date 2022/6/6 15:09
 * @description
 **/
public class TuZhanChannleAlgorithm {

    @Test
    public void algorithm() {
        String orgID = "a34d311a-1fab-4068-86ae-5fdf6d061216";

        System.out.println(getHash(orgID) % 16);
        System.out.println(getHash(orgID) / 16 % 64);
    }

    public static int getHash(String str) {
        final int p = 16777619;
        int hash = (int) 2166136261L;
        for (int i = 0; i < str.length(); i++)
            hash = (hash ^ str.charAt(i)) * p;
        hash += hash << 13;
        hash ^= hash >> 7;
        hash += hash << 3;
        hash ^= hash >> 17;
        hash += hash << 5;
        // 如果算出来的值为负数则取其绝对值
        if (hash < 0)
            hash = Math.abs(hash);
        return hash;
    }
}
