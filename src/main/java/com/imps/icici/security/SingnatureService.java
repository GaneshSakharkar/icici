package com.imps.icici.security;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyStoreException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.imps.icici.cipher.KeystoreUtil;


@Component
public class SingnatureService {

	
	@Autowired
	private KeystoreUtil oKeystoreUtil;
	
	public String generateSingature(String data) throws NoSuchAlgorithmException, SignatureException {
		
		String digitalSignature = null;
		try {
			PrivateKey privateKey = oKeystoreUtil.getPrivateKey();
			// create singnature
			Signature signature = Signature.getInstance("SHA256withRSA");
			signature.initSign(privateKey);
			signature.update(data.getBytes("UTF-8"));
			
			byte[] digiatalSign = signature.sign();
			digitalSignature = Base64.getEncoder().encodeToString(digiatalSign);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return digitalSignature;
	} 
	
	
	public boolean verifySignature(String singnatureBase64) {
		
		String originalData = "testing_data";
		
		boolean isValid = false;
		try {
			PublicKey publicKey = oKeystoreUtil.getPublicKey();
			Signature signature = Signature.getInstance("SHA256withRSA");
			signature.initVerify(publicKey);
			signature.update(originalData.getBytes("UTF-8"));
			
			byte[] decodeSign = Base64.getDecoder().decode(singnatureBase64);
			isValid = signature.verify(decodeSign);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return isValid;
	}
	
	
	
}
