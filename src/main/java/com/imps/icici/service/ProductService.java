package com.imps.icici.service;

import java.util.List;

import com.imps.icici.dto.ProductDTO;
import com.imps.icici.dto.ProductResponseDTO;
import com.imps.icici.exception.ProductException;

public interface ProductService {

	List<ProductResponseDTO<ProductDTO>> getProductAll() throws ProductException;

	

}
