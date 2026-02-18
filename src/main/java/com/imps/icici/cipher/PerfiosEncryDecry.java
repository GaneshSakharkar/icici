package com.imps.icici.cipher;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PerfiosEncryDecry {

	
	@Autowired
	private KeystoreUtil keystoreUtil;
	
	private static final String TRANSAFORMATION="AES/CBC/PKCS5Padding";
	
	private static final String RSA_TRANSFORMATION = "RSA/ECB/PKCS1Padding";
	
	public String encryptService(String data) throws NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		
		byte[] iv = new byte[16];
		
		IvParameterSpec ivSpec = new IvParameterSpec(iv);
		Cipher cipher = Cipher.getInstance(TRANSAFORMATION);
		cipher.init(Cipher.ENCRYPT_MODE, generateSessionKey(), ivSpec);
		byte[] encryptedByte = cipher.doFinal(data.getBytes());
		return Base64.getEncoder().encodeToString(encryptedByte);
	}
	
	private SecretKey generateSessionKey() {
		SecureRandom secureRandom = new SecureRandom();
		byte[] keyByte = new byte[16];
		secureRandom.nextBytes(keyByte);
		SecretKey sessionKey = new SecretKeySpec(keyByte, "AES");
		return sessionKey;
	}
	
	private String encryptSessionKey(String sessionKey) throws NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		
		byte[] encrypteByte = null;
		try {
			PublicKey publicKey = keystoreUtil.getPublicKey();
			
			byte[] keyByte = new byte[16];
			IvParameterSpec ivSpec = new IvParameterSpec(keyByte);
			Cipher cipher = Cipher.getInstance(RSA_TRANSFORMATION);
			cipher.init(Cipher.ENCRYPT_MODE, publicKey, ivSpec);
			
			encrypteByte = cipher.doFinal(sessionKey.getBytes());
			
		} catch (KeyStoreException | NoSuchAlgorithmException | CertificateException | IOException e) {
			e.printStackTrace();
		}	
		return Base64.getEncoder().encodeToString(encrypteByte);
	}
	
}
