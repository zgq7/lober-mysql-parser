package com.lober.mysql.parser;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.junit.Test;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author liaonanzhou
 * @date 2022/6/6 15:09
 * @description
 **/
public class TuZhanUserAlgorithm {

    @Test
    public void hash() {
        System.out.println(shardingValToInt("51b1b251-6b00-4ee0-b196-a000e581119b"));
    }


    private int shardingValToInt(String shardingval) {
        return Math.abs(shardingval.hashCode() % 16);
    }


    @Test
    public void password() {
        String encode = getSHA256Str("hxx123456");
        System.out.println(encode);
        System.out.println(decodeSHA256Str(encode));
    }

    public static String getSHA256Str(String str) {
        MessageDigest messageDigest;
        String encdeStr = "";
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
            byte[] hash = messageDigest.digest(str.getBytes(StandardCharsets.UTF_8));
            encdeStr = Hex.encodeHexString(hash);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return encdeStr;
    }

    public static String decodeSHA256Str(String str) {
        MessageDigest messageDigest;
        String encdeStr = "";
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
            byte[] hash = messageDigest.digest(str.getBytes(StandardCharsets.UTF_8));
            Hex.decodeHex(byte2char(hash));
        } catch (NoSuchAlgorithmException | DecoderException e) {
            e.printStackTrace();
        }
        return encdeStr;
    }

    private static char[] byte2char(byte[] bytes) {
        Charset cs = StandardCharsets.UTF_8;
        ByteBuffer byteBuffer = ByteBuffer.allocate(bytes.length);
        byteBuffer.put(bytes);
        byteBuffer.flip();
        CharBuffer cb = cs.decode(byteBuffer);

        return cb.array();
    }
}
