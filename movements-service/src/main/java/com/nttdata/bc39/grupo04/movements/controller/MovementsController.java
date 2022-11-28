package com.nttdata.bc39.grupo04.movements.controller;

import com.nttdata.bc39.grupo04.movements.dto.MovementsDTO;
import com.nttdata.bc39.grupo04.movements.dto.MovementsExplainDTO;
import com.nttdata.bc39.grupo04.movements.service.MovementsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/movements")
public class MovementsController {

    @Autowired
    private MovementsService service;


    @GetMapping("/{number}")
    Flux<MovementsExplainDTO> getAllMovementsByNumber(@PathVariable("number") String number) {
        return service.getAllMovementsByNumber(number);
    }

    @PostMapping("/deposit")
    Mono<MovementsDTO> saveDepositMovement(@RequestBody MovementsDTO body) {
        return service.saveDepositMovement(body);
    }
    @PostMapping("/withdrawl")
    Mono<MovementsDTO> saveWithdrawlMovement(@RequestBody MovementsDTO body) {
        return service.saveWithdrawlMovement(body);
    }
}
