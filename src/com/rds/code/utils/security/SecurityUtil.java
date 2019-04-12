package com.rds.code.utils.security;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import org.apache.log4j.Logger;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * descrition 安全加解密
 * 
 * @author 杨惠
 * @version 1.0
 * @date 2009-5-21
 */
public class SecurityUtil {

	private final static Logger logger = Logger.getLogger(SecurityUtil.class);

	/**
	 * 加密
	 * 
	 * @param strInput
	 *            输入字符串
	 * @return String
	 * @throws Exception
	 */
	public final static String encrypt(String strInput) {
		String strResult = "";
		SecureRandom secureRandom = new SecureRandom();
		byte byteRawKeyData[] = "&d!t(k)j".getBytes();
		DESKeySpec dKeySpec = null;
		try {
			dKeySpec = new DESKeySpec(byteRawKeyData);
			SecretKeyFactory secretKeyFactory = SecretKeyFactory
					.getInstance("DES");
			SecretKey key = secretKeyFactory.generateSecret(dKeySpec);
			Cipher cipher = Cipher.getInstance("DES");
			cipher.init(Cipher.ENCRYPT_MODE, key, secureRandom);
			byte[] cipherByte = cipher.doFinal(strInput.getBytes());
			strResult = new BASE64Encoder().encode(cipherByte);
		} catch (Exception e) {
			logger.error(e);
		}
		return strResult;

	}

	/**
	 * 解密
	 * 
	 * @param strInput
	 *            输入字符串
	 * @return String 如果返回是"",则解密失败
	 * @throws Exception
	 */
	public final static String decrypt(String strInput) {
		String strResult = "";
		byte[] byteArray = null;
		try {
			byteArray = new BASE64Decoder().decodeBuffer(strInput);
			SecureRandom secureRandom = new SecureRandom();
			byte byteRawKeyData[] = "&d!t(k)j".getBytes();
			DESKeySpec dKeySpec = new DESKeySpec(byteRawKeyData);
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			SecretKey key = keyFactory.generateSecret(dKeySpec);
			Cipher cipher = Cipher.getInstance("DES");
			cipher.init(Cipher.DECRYPT_MODE, key, secureRandom);
			byte[] clearByte = cipher.doFinal(byteArray);
			strResult = new String(clearByte);
		} catch (Exception e) {
			logger.error(e);
		}
		return strResult;
	}

}
