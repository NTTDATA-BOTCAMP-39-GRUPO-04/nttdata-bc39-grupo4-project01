package com.nttdata.bc39.grupo04.credit.service;

import org.springframework.stereotype.Component;

import com.nttdata.bc39.grupo04.credit.dto.CreditDTO;
import com.nttdata.bc39.grupo04.credit.persistence.CreditEntity;

@Component
public class CreditMapperImpl implements CreditMapper {

	@Override
	public CreditDTO entityToDto(CreditEntity entity) {
		// TODO Auto-generated method stub
		if (entity == null) {
			return null;
		}

		CreditDTO creditDTO = new CreditDTO();
		creditDTO.setCreditNumber(entity.getCreditNumber());
		creditDTO.setProductId(entity.getProductId());
		creditDTO.setCustomerId(entity.getCustomerId());
		creditDTO.setAvailableBalance(entity.getAvailableBalance());
		creditDTO.setCreditAmount(entity.getCreditAmount());
		creditDTO.setCardNumber(entity.getCardNumber());
		creditDTO.setCreateDate(entity.getCreateDate());
		creditDTO.setModifyDate(entity.getModifyDate());

		return creditDTO;
	}

	@Override
	public CreditEntity dtoToEntity(CreditDTO dto) {
		// TODO Auto-generated method stub
		if (dto == null) {
			return null;
		}

		CreditEntity creditEntity = new CreditEntity();
		creditEntity.setCreditNumber(dto.getCreditNumber());
		creditEntity.setProductId(dto.getProductId());
		creditEntity.setCustomerId(dto.getCustomerId());
		creditEntity.setAvailableBalance(dto.getAvailableBalance());
		creditEntity.setCreditAmount(dto.getCreditAmount());
		creditEntity.setCardNumber(dto.getCardNumber());
		creditEntity.setCreateDate(dto.getCreateDate());
		creditEntity.setModifyDate(dto.getModifyDate());

		return creditEntity;
	}
}
