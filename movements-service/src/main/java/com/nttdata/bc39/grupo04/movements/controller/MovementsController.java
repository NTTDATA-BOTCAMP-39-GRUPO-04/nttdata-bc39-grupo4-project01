package com.nttdata.bc39.grupo04.movements.controller;

import com.nttdata.bc39.grupo04.movements.dto.MovementsDTO;
import com.nttdata.bc39.grupo04.movements.service.MovementsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/movements")
public class MovementsController {

    @Autowired
    private MovementsService service;


    @GetMapping("/credit/{number}")
    Flux<MovementsDTO> getAllMovementsByCreditNumber(String number) {
        return service.getAllMovementsByCreditNumber(number);
    }

    @GetMapping("/account/{number}")
    Flux<MovementsDTO> getAllMovementsByAccountNumber(String number) {
        return service.getAllMovementsByAccountNumber(number);
    }

    @PostMapping("/credit")
    Mono<MovementsDTO> getSaveCreditMovement(MovementsDTO body) {
        return service.saveCreditMovement(body);
    }

    @PostMapping("/account")
    Mono<MovementsDTO> getSaveAccountMovement(MovementsDTO body) {
        return service.saveAccountMovement(body);
    }
}
