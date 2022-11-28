package com.nttdata.bc39.grupo04.movements.service;

import com.nttdata.bc39.grupo04.api.exceptions.InvaliteInputException;
import com.nttdata.bc39.grupo04.api.exceptions.NotFoundException;
import com.nttdata.bc39.grupo04.api.utils.CodesEnum;
import com.nttdata.bc39.grupo04.movements.dto.MovementsDTO;
import com.nttdata.bc39.grupo04.movements.dto.MovementsExplainDTO;
import com.nttdata.bc39.grupo04.movements.persistence.MovementsEntity;
import com.nttdata.bc39.grupo04.movements.persistence.MovementsRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

import static com.nttdata.bc39.grupo04.api.utils.Constants.*;
import static com.nttdata.bc39.grupo04.api.utils.Constants.MAX_WITHDRAWAL_AMOUNT;

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
    public Mono<MovementsDTO> saveDepositMovement(MovementsDTO dto) {
        validationMovement(dto, CodesEnum.TYPE_DEPOSIT);
        MovementsEntity entity = mapper.dtoToEntity(dto);
        entity.setDepositAmount(dto.getAmount());
        entity.setWithdrawlAmount(0);
        entity.setDate(Calendar.getInstance().getTime());
        return repository.save(entity)
                .map(x -> {
                    MovementsDTO mDto = mapper.entityToDto(x);
                    mDto.setAmount(x.getDepositAmount());
                    return mDto;
                });
    }

    @Override
    public Mono<MovementsDTO> saveWithdrawlMovement(MovementsDTO dto) {
        validationMovement(dto, CodesEnum.TYPE_WITHDRAWL);
        MovementsEntity entity = mapper.dtoToEntity(dto);
        entity.setWithdrawlAmount(dto.getAmount());
        entity.setDepositAmount(0);
        entity.setDate(Calendar.getInstance().getTime());
        return repository.save(entity)
                .map(x -> {
                    MovementsDTO mDto = mapper.entityToDto(x);
                    mDto.setAmount(x.getWithdrawlAmount());
                    return mDto;
                });
    }

    @Override
    public Flux<MovementsExplainDTO> getAllMovementsByNumber(String number) {
        return repository.findByNumber(number).map(mapper::entityDtoExplain);
    }

    private void validationMovement(MovementsDTO dto, CodesEnum codesEnum) {
        if (Objects.isNull(dto)) {
            throw new InvaliteInputException("Error, el formato de los datos es invalido");
        }
        if (Objects.isNull(dto.getNumber())) {
            throw new InvaliteInputException("Error, el parametro 'number' es invalido");
        }
        double amount = dto.getAmount();
        if (codesEnum == CodesEnum.TYPE_DEPOSIT) {
            if (amount < MIN_DEPOSIT_AMOUNT || amount > MAX_DEPOSIT_AMOUNT) {
                logger.debug("Error limites de deposito , nro: " + dto.getNumber() + " con monto: " + amount);
                throw new NotFoundException(String.format(Locale.getDefault(), "Error, los limites de DEPOSITO son min: %d sol y max: %d sol", MIN_DEPOSIT_AMOUNT, MAX_DEPOSIT_AMOUNT));
            }
        }
        if (codesEnum == CodesEnum.TYPE_WITHDRAWL) {
            if (amount < MIN_WITHDRAWAL_AMOUNT || amount > MAX_WITHDRAWAL_AMOUNT) {
                logger.debug("Error limites de retiro , nro: " + dto.getNumber() + " con monto: " + amount);
                throw new NotFoundException(String.format(Locale.getDefault(), "Error, los limites de RETIRO son min: %d sol y max: %d sol", MIN_WITHDRAWAL_AMOUNT, MAX_WITHDRAWAL_AMOUNT));
            }
        }
    }

}
