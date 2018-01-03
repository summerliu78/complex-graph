package com.yinker.tinyv.utils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
/**
 * 哈希工具类
 * @author Zhu Xiuwei
 */
public class HashUtil {

	/**
	 * Get MD5 of given content
	 * 
	 * @param content
	 * @return
	 */
	public static String md5(String content) {
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
		}
		byte[] bytes = md.digest(content.getBytes());
		return new BigInteger(1, bytes).toString(16);
	}

	/**
	 * Long representation for eXcusive or (XOR) value of the two halves of the
	 * given content's MD5 bytes
	 * 
	 * @param content
	 * @return
	 */
	public static long getLongHashCode(String content) {
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
		}
		byte[] bytes = md.digest(content.getBytes());
		for (int i = 0; i < 8; i++) {
			bytes[i] ^= bytes[i + 8];
		}
		return java.nio.ByteBuffer.wrap(bytes, 0, 8).getLong();
	}

	/**
	 * http://isthe.com/chongo/tech/comp/fnv/
	 */
	public static int fnvHash(String input) {
		if (input == null)
			return 0;

		int h = (int)2166136261L;
		for (int val : input.toCharArray()) {
			h *= 16777619;
			h ^= val;
		}
		return h;
	}

	// test
	public static void main(String[] args) {
		System.out.println(Math.abs(Long.MIN_VALUE));
		System.out.println(HashUtil.md5("170504"));
		System.out.println(HashUtil.md5("hello").hashCode());
		System.out.println(HashUtil.getLongHashCode("hello"));
		System.out.println(HashUtil.getLongHashCode("world"));
		System.out.println(HashUtil.getLongHashCode("hello world!"));
		System.out.println(HashUtil.fnvHash(null));
		System.out.println(HashUtil.fnvHash(""));
		System.out.println(HashUtil.fnvHash("x"));
		System.out.println("x".hashCode());
	}
}
