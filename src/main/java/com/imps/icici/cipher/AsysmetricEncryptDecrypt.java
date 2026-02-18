package com.imps.icici.cipher;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.imps.icici.exception.EncryptionDecryptionException;

@Service
public class AsysmetricEncryptDecrypt {

	@Autowired
	private KeystoreUtil oKeystoreUtil;
	
	private static final String TRANSAFORMATION = "RSA/ECB/PKCS1Padding";
	
	
	public String encryption(String plainText) {
		
		byte[] encryptedByte = null;
		try {
			Cipher cipher = Cipher.getInstance(TRANSAFORMATION);
			cipher.init(Cipher.ENCRYPT_MODE, oKeystoreUtil.getPublicKey());
			encryptedByte = cipher.doFinal(plainText.getBytes());
			
		} catch (Exception e) {
			throw new EncryptionDecryptionException(e.getMessage());
		}
		return Base64.getEncoder().encodeToString(encryptedByte);
		
	}
	
	public String decryption(String encryptedText) throws InvalidKeyException, UnrecoverableKeyException, KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
		
		byte[] decryptedByte = null;
		try {
			Cipher cipher = Cipher.getInstance(TRANSAFORMATION);
			cipher.init(Cipher.DECRYPT_MODE, oKeystoreUtil.getPrivateKey());
			byte[] decode = Base64.getDecoder().decode(encryptedText.getBytes());
			decryptedByte = cipher.doFinal(decode); 
		} catch (Exception e) {
			throw new EncryptionDecryptionException(e.getMessage());
		}
		return new String(decryptedByte,StandardCharsets.UTF_8);
	}
}
