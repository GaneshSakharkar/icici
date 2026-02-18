package com.imps.icici.cipher;

import java.io.IOException;
import java.io.InputStream;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import com.imps.icici.exception.CertificateNotFoundException;

@Component
public class KeystoreUtil {

	@Value("${keystore.path}")
	private Resource resource;
	
	@Value("${keystore.password}")
	private String keystorePass;
	
	@Value("${keystore.alias}")
	private String aliesName;
	
	@Value("${key.password}")
	private String aliesPass;
	
	private KeyStore getKeyStore() throws KeyStoreException, IOException, NoSuchAlgorithmException, CertificateException {
		KeyStore key = KeyStore.getInstance("JKS");
		try(InputStream input = resource.getInputStream()){
			key.load(input, keystorePass.toCharArray());
		}
		return key;
	}
	
	public PublicKey getPublicKey() throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {
		KeyStore key = getKeyStore();
		Certificate cert = key.getCertificate(aliesName);
		if(cert==null) {throw new CertificateNotFoundException ("Public key Certificate Not Found.");}
		return cert.getPublicKey();
	}
	
	public PrivateKey getPrivateKey() throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException, UnrecoverableKeyException {
		KeyStore ks = getKeyStore();
		Key key = ks.getKey(aliesName, aliesPass.toCharArray());
		if(key==null) {throw new CertificateNotFoundException ("Private key Certificate Not Found.");}
		return (PrivateKey) key;
	}
	
	
	
}
