package com.imps.icici.controller;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.imps.icici.apiresponse.ApiResponse;
import com.imps.icici.cipher.PerfiosEncryDecry;
import com.imps.icici.dto.AdminRegDTO;
import com.imps.icici.dto.ProductDTO;
import com.imps.icici.dto.ProductResponseDTO;
import com.imps.icici.dto.UserDto;
import com.imps.icici.exception.ProductException;
import com.imps.icici.security.SingnatureService;
import com.imps.icici.service.AdminService;
import com.imps.icici.service.EncryptedDecryptService;
import com.imps.icici.service.ProductService;
import com.imps.icici.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/public")
public class PublicController {
	
	
	@Autowired
	private UserService oUserService;
	
	@Autowired
	private ProductService oProductService;
	
	@Autowired
	private EncryptedDecryptService oEncryptedDecryptService;
	
	@Autowired
	private AdminService oAdminService;
	
	@Autowired
	private PerfiosEncryDecry perfiosEncryDecry;
	
	@Autowired
	private SingnatureService oSingnatureService;
	

	@PostMapping("/user/register")
	public ResponseEntity<ApiResponse<UserDto>> userRegister(@RequestBody UserDto oUserDto){
		
		ApiResponse<UserDto> data = oUserService.userRegister(oUserDto);
		
		return new ResponseEntity<ApiResponse<UserDto>>(data,HttpStatus.CREATED);
	}
	
	@PostMapping("/user/login")
	public ResponseEntity<UserDto> userLogin(@RequestBody UserDto oUserDto){
		UserDto data = oUserService.userLogin(oUserDto);
		return new ResponseEntity<UserDto>(data,HttpStatus.CREATED);
	}
	
	@PostMapping("/encrypt")
	public String encryptData(@RequestBody String data) throws Exception {
		return oEncryptedDecryptService.encrypt(data);
	}

	@PostMapping("/decrypt")
	public String decryptData(@RequestBody String data) throws JsonMappingException, JsonProcessingException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
		return oEncryptedDecryptService.decryptData(data);
	}
	
	@GetMapping("/product")
	public ResponseEntity<List<ProductResponseDTO<ProductDTO>>> getProductAll() throws ProductException{
		
		List<ProductResponseDTO<ProductDTO>> data = oProductService.getProductAll();
		
		return new ResponseEntity<List<ProductResponseDTO<ProductDTO>>>(data,HttpStatus.OK);
	}
	
	@PostMapping("/admin/register")
	public ResponseEntity<ApiResponse<UserDto>> adminRegister(@RequestBody UserDto oUserDto){
		
		ApiResponse<UserDto> data = oUserService.adminRegister(oUserDto);
		
		return new ResponseEntity<ApiResponse<UserDto>>(data,HttpStatus.CREATED);
	}
	
	
	@PutMapping("/verify/otp")
	public ResponseEntity<ApiResponse<?>> verifyOtp(@RequestParam String otp, @RequestParam long userId){
		
		ApiResponse<?> response = oUserService.verifyOtp(otp,userId);
		
		return new ResponseEntity<ApiResponse<?>>(response,HttpStatus.CREATED);
		
	}
	
	@GetMapping("/apikey")
	public ResponseEntity<Map<String,Object>> generateApiKey(){
		
		
		String apiKey = oUserService.generateApiKey();
		
		System.out.println("API key length: " + apiKey.length());
		
		Map<String,Object> map = new HashMap<>();
		map.put("APIKEY", apiKey);
		
		return new ResponseEntity<Map<String,Object>>(map , HttpStatus.CREATED);
	}
	
	
	@PostMapping("/icici/encrypt")
	public ResponseEntity<String> generateEncrypt(@RequestBody String requestData) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException{
		
		String encryptData = perfiosEncryDecry.encryptService(requestData);
		
		return new ResponseEntity<String>(encryptData,HttpStatus.OK);
	}
	
	@GetMapping("/sign")
	public ResponseEntity<String> getSign(){
		String data = null;
		try {
			data = oSingnatureService.generateSingature("testing_data");
		} catch (NoSuchAlgorithmException | SignatureException e) {
			e.printStackTrace();
		}
		
		return new ResponseEntity<String>(data , HttpStatus.CREATED);
	}
	
	@PostMapping("/verify/sign")
	public ResponseEntity<String> verifySign(@RequestBody String data){
		String response = null;
		boolean isValid = oSingnatureService.verifySignature(data);
		if (isValid) {
			response = "Signature is valid " + isValid;
		}else {
			response = "Signature is valid " + isValid;
		}
		return new ResponseEntity<String>(response , HttpStatus.ACCEPTED);
	}
}
