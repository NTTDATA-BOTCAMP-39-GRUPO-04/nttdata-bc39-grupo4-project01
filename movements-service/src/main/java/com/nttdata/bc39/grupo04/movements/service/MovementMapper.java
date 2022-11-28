package com.nttdata.bc39.grupo04.movements.service;

import com.nttdata.bc39.grupo04.movements.dto.MovementsDTO;
import com.nttdata.bc39.grupo04.movements.dto.MovementsExplainDTO;
import com.nttdata.bc39.grupo04.movements.persistence.MovementsEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface MovementMapper {

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "depositAmount", ignore = true),
            @Mapping(target = "withdrawlAmount", ignore = true),
    })
    MovementsEntity dtoToEntity(MovementsDTO dto);

    @Mappings({
            @Mapping(target = "amount", ignore = true),
    })
    MovementsDTO entityToDto(MovementsEntity entity);

    MovementsExplainDTO entityDtoExplain(MovementsEntity entity);

}
