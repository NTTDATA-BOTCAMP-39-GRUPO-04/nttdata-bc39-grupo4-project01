package com.nttdata.bc39.grupo04.movements.service;

import com.nttdata.bc39.grupo04.api.utils.CodesEnum;
import com.nttdata.bc39.grupo04.movements.dto.MovementsDTO;
import com.nttdata.bc39.grupo04.movements.persistence.MovementsEntity;

public interface MovementMapper {

    MovementsEntity dtoToEntity(MovementsDTO dto, CodesEnum productType);

    MovementsDTO entityToDto(MovementsEntity entity, CodesEnum productType);

}
