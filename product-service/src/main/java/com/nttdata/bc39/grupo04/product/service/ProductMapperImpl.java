package com.nttdata.bc39.grupo04.product.service;


import org.springframework.stereotype.Component;

import com.nttdata.bc39.grupo04.product.dto.ProductDTO;
import com.nttdata.bc39.grupo04.product.persistence.ProductEntity;



@Component
public class ProductMapperImpl implements ProductMapper {

	@Override
	public ProductDTO entityToDto(ProductEntity entity) {
		// TODO Auto-generated method stub
		if( entity == null) {
			return null;
		}
		
		ProductDTO productDTO = new ProductDTO();
		productDTO.setCode(entity.getCode());
		productDTO.setName(entity.getName());
		productDTO.setTypeProduct(entity.getTypeProduct());
		return productDTO;
	}

	@Override
	public ProductEntity dtoToEntity(ProductDTO dto) {
		// TODO Auto-generated method stub
		if( dto == null) {
			return null;
		}
		
		ProductEntity productEntity = new ProductEntity();
		productEntity.setCode(dto.getCode());
		productEntity.setName(dto.getName());
		productEntity.setTypeProduct(dto.getTypeProduct());
		
		return productEntity;
	}

}
