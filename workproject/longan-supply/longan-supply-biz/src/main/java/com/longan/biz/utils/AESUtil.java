package com.longan.biz.utils;

import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.engines.AESFastEngine;
import org.bouncycastle.crypto.modes.CBCBlockCipher;
import org.bouncycastle.crypto.paddings.PaddedBufferedBlockCipher;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;
import org.bouncycastle.util.encoders.Hex;

public final class AESUtil {

	private static final byte[] INIT_VECTOR = { 0x31, 0x37, 0x36, 0x35, 0x34, 0x33, 0x32, 0x31,
			0x38, 0x27, 0x36, 0x35, 0x33, 0x23, 0x32, 0x33 };

	public static String encrypt(String content, String apiKey){
		if (apiKey == null) {
			throw new IllegalArgumentException("Key cannot be null!");
		}
		String encrypted = null;
		byte[] keyBytes = apiKey.getBytes();
		if (keyBytes.length != 32 && keyBytes.length != 24 && keyBytes.length != 16) {
			throw new IllegalArgumentException("Key length must be 128/192/256 bits!");
		}
		byte[] encryptedBytes = null;
		try {
			encryptedBytes = encrypt(content.getBytes(), keyBytes, INIT_VECTOR);
		} catch (Exception e) {
		}
		encrypted = new String(Hex.encode(encryptedBytes));
		return encrypted;
	}

	public static String decrypt(String content, String apiKey) throws Exception {
		if (apiKey == null) {
			throw new IllegalArgumentException("Key cannot be null!");
		}
		String decrypted = null;
		byte[] encryptedContent = Hex.decode(content);
		byte[] keyBytes = apiKey.getBytes();
		byte[] decryptedBytes = null;
		if (keyBytes.length != 32 && keyBytes.length != 24 && keyBytes.length != 16) {
			throw new IllegalArgumentException("Key length must be 128/192/256 bits!");
		}
		decryptedBytes = decrypt(encryptedContent, keyBytes, INIT_VECTOR);
		decrypted = new String(decryptedBytes);
		return decrypted;
	}

	private static byte[] encrypt(byte[] plain, byte[] key, byte[] iv) throws Exception {
		PaddedBufferedBlockCipher aes = new PaddedBufferedBlockCipher(new CBCBlockCipher(
				new AESFastEngine()));
		CipherParameters ivAndKey = new ParametersWithIV(new KeyParameter(key), iv);
		aes.init(true, ivAndKey);
		return cipherData(aes, plain);
	}

	private static byte[] decrypt(byte[] cipher, byte[] key, byte[] iv) throws Exception {
		PaddedBufferedBlockCipher aes = new PaddedBufferedBlockCipher(new CBCBlockCipher(
				new AESFastEngine()));
		CipherParameters ivAndKey = new ParametersWithIV(new KeyParameter(key), iv);
		aes.init(false, ivAndKey);
		return cipherData(aes, cipher);
	}

	private static byte[] cipherData(PaddedBufferedBlockCipher cipher, byte[] data)
			throws Exception {
		int minSize = cipher.getOutputSize(data.length);
		byte[] outBuf = new byte[minSize];
		int length1 = cipher.processBytes(data, 0, data.length, outBuf, 0);
		int length2 = cipher.doFinal(outBuf, length1);
		int actualLength = length1 + length2;
		byte[] result = new byte[actualLength];
		System.arraycopy(outBuf, 0, result, 0, result.length);
		return result;
	}
}