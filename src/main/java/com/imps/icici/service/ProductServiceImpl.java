package com.imps.icici.service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.imps.icici.dto.ProductDTO;
import com.imps.icici.dto.ProductResponseDTO;
import com.imps.icici.exception.ProductException;

@Service
public class ProductServiceImpl implements ProductService{
	
	@Autowired
	private RestTemplate oRestTemplate;
	
	@Value("${product.api.url}")
	private String API_URL;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	private static Logger logger = LogManager.getLogger(ProductServiceImpl.class);
	
	//private static final String API_URL = "https://api.restful-api.dev/objects";

	@Override
	public List<ProductResponseDTO<ProductDTO>> getProductAll() throws ProductException {
		
		try {
			ResponseEntity<List<ProductResponseDTO<ProductDTO>>> response = oRestTemplate.exchange(API_URL,
					HttpMethod.GET,
					null,
					new ParameterizedTypeReference<List<ProductResponseDTO<ProductDTO>>>() {});
			
			if(response.getStatusCode().is2xxSuccessful()) {
				
				List<ProductResponseDTO<ProductDTO>> productData = response.getBody();
				
				String productObject = objectMapper.writeValueAsString(productData);
				
				logger.debug("Product Info: {}", productObject.toString());
				
				
				return productData;
			}else {
				throw new RuntimeException("Exception occured while processing request...!");
			}
		} catch (Exception e) {
			throw new ProductException("Exception Message:" , e.getMessage());
		}
	}

	
}
