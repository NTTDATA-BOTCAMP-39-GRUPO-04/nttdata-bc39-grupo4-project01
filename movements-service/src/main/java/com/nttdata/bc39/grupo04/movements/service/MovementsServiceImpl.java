package com.nttdata.bc39.grupo04.movements.service;

import com.nttdata.bc39.grupo04.api.utils.CodesEnum;
import com.nttdata.bc39.grupo04.movements.dto.MovementsDTO;
import com.nttdata.bc39.grupo04.movements.persistence.MovementsEntity;
import com.nttdata.bc39.grupo04.movements.persistence.MovementsRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Calendar;

@Service
public class MovementsServiceImpl implements MovementsService {
    private final MovementsRepository repository;
    private final MovementMapper mapper;
    private final Logger logger = Logger.getLogger(MovementsServiceImpl.class);

    @Autowired
    public MovementsServiceImpl(MovementsRepository repository, MovementMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Mono<MovementsDTO> saveCreditMovement(MovementsDTO dto) {
        MovementsEntity entity = mapper.dtoToEntity(dto, CodesEnum.TYPE_CREDIT);
        entity.setDate(Calendar.getInstance().getTime());
        return repository.save(entity).map(x -> mapper.entityToDto(x, CodesEnum.TYPE_CREDIT));
    }

    @Override
    public Mono<MovementsDTO> saveAccountMovement(MovementsDTO dto) {
        MovementsEntity entity = mapper.dtoToEntity(dto, CodesEnum.TYPE_ACCOUNT);
        entity.setDate(Calendar.getInstance().getTime());
        return repository.save(entity).map(x -> mapper.entityToDto(x, CodesEnum.TYPE_ACCOUNT));
    }

    @Override
    public Flux<MovementsDTO> getAllMovementsByAccountNumber(String accountNumber) {
        return repository.findByAccountnumber(accountNumber).map(
                x -> mapper.entityToDto(x, CodesEnum.TYPE_ACCOUNT));
    }

    @Override
    public Flux<MovementsDTO> getAllMovementsByCreditNumber(String creditNumber) {
        return repository.finByCreditnumber(creditNumber).map(
                x -> mapper.entityToDto(x, CodesEnum.TYPE_CREDIT));
    }
}
