package com.nttdata.bc39.grupo04.movements.service;

import com.nttdata.bc39.grupo04.movements.dto.MovementsDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface MovementsService {

    Mono<MovementsDTO> saveCreditMovement(MovementsDTO dto);

    Mono<MovementsDTO> saveAccountMovement(MovementsDTO dto);

    Flux<MovementsDTO> getAllMovementsByAccountNumber(String accountNumber);

    Flux<MovementsDTO> getAllMovementsByCreditNumber(String creditNumber);
}
