package com.nttdata.bc39.grupo04.movements.service;

import com.nttdata.bc39.grupo04.api.utils.CodesEnum;
import com.nttdata.bc39.grupo04.movements.dto.MovementsDTO;
import com.nttdata.bc39.grupo04.movements.persistence.MovementsEntity;

public class MovementsMapperImpl implements MovementMapper {
    @Override
    public MovementsDTO entityToDto(MovementsEntity entity, CodesEnum productType) {
        MovementsDTO dto = new MovementsDTO();
        dto.setAmount(entity.getAmount());
        dto.setDate(entity.getDate());
        if (productType == CodesEnum.TYPE_CREDIT) {
            dto.setNumber(entity.getCreditnumber());
        } else if (productType == CodesEnum.TYPE_ACCOUNT) {
            dto.setNumber(entity.getAccountnumber());
        }
        return dto;
    }

    @Override
    public MovementsEntity dtoToEntity(MovementsDTO dto, CodesEnum productType) {
        MovementsEntity entity = new MovementsEntity();
        entity.setType(dto.getType());
        entity.setAmount(dto.getAmount());
        entity.setDate(dto.getDate());
        if (productType == CodesEnum.TYPE_CREDIT) {
            entity.setCreditnumber(dto.getNumber());
        } else if (productType == CodesEnum.TYPE_ACCOUNT) {
            entity.setAccountnumber(dto.getNumber());
        }
        return entity;
    }
}
