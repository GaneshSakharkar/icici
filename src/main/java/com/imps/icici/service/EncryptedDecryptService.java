package com.imps.icici.service;

import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.stereotype.Service;


@Service
public class EncryptedDecryptService {
	
	
 

	// This is Symetric System for encryption and decryption.

	private static final String SECRET_KEY="ec905149bb3211225f59024e21a48a48";
	private static final String AES = "AES";
	
	//private static final String ALGORITHM = "AES";
	
    private static final String TRANSFORMATION = "AES/CBC/PKCS5Padding";
	
    
    // step 1 generate key 
	private SecretKeySpec generateKey() {
		return new SecretKeySpec(SECRET_KEY.getBytes(StandardCharsets.UTF_8), AES);
	}
	
	// Step 2 encrypt the data
	public String encrypt(String data) throws Exception {
		
		byte[] iv = new byte[16];
		
		new SecureRandom().nextBytes(iv);
		
		IvParameterSpec ivSpec = new IvParameterSpec(iv);
		
		Cipher cipher = Cipher.getInstance(TRANSFORMATION);
		cipher.init(Cipher.ENCRYPT_MODE, generateKey(),ivSpec);
		
		byte[] encryptByte = cipher.doFinal(data.getBytes());
		
		byte[] combine = new byte[iv.length + encryptByte.length];
		
		System.arraycopy(iv, 0, combine, 0, iv.length);
		
		System.arraycopy(encryptByte, 0, combine, iv.length, encryptByte.length);
		
		return Base64.getEncoder().encodeToString(combine);
	}
	
	
	// This for Normal Data encryption (i.e Reapting same eccryption data in response).
	
//	public String encrypt(String data) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
//		
//		Cipher cipher = Cipher.getInstance(AES);
//		cipher.init(Cipher.ENCRYPT_MODE, generateKey());
//	
//		byte[] encryptValue = cipher.doFinal(data.getBytes());
//		
//		return Base64.getEncoder().encodeToString(encryptValue);
//	}
	

	public String decryptData(String data) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
		
		byte[] combined = Base64.getDecoder().decode(data);

        byte[] iv = new byte[16];
        
        System.arraycopy(combined, 0, iv, 0, iv.length);
        
        IvParameterSpec ivSpec = new IvParameterSpec(iv);

        byte[] encryptedBytes = new byte[combined.length - iv.length];
        System.arraycopy(combined, iv.length, encryptedBytes, 0, encryptedBytes.length);

        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.DECRYPT_MODE, generateKey(), ivSpec);
        
        
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);

        return new String(decryptedBytes);
	}
	
	public KeyGenerator keyGenerate() {
		
		SecretKeySpec key = generateKey();
		
		return null;
	}
	
}
