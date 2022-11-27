package com.nttdata.bc39.grupo04.account.service;

import com.nttdata.bc39.grupo04.account.dto.AccountDTO;
import com.nttdata.bc39.grupo04.account.dto.HolderDTO;
import com.nttdata.bc39.grupo04.account.persistence.AccountEntity;
import com.nttdata.bc39.grupo04.account.persistence.AccountRepository;
import com.nttdata.bc39.grupo04.api.exceptions.BadRequestException;
import com.nttdata.bc39.grupo04.api.exceptions.InvaliteInputException;
import com.nttdata.bc39.grupo04.api.exceptions.NotFoundException;
import com.nttdata.bc39.grupo04.api.utils.Constants;
import io.netty.util.internal.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import static com.nttdata.bc39.grupo04.api.utils.Constants.*;

@Service
public class AccountServiceImpl implements AccountService {
    private final AccountRepository repository;
    private final AccountMapper mapper;
    private Logger LOG = LoggerFactory.getLogger(AccountServiceImpl.class);

    @Autowired
    public AccountServiceImpl(AccountRepository repository, AccountMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Mono<AccountDTO> getByAccountNumber(String accountNumber) {
        if (Objects.isNull(accountNumber)) {
            throw new InvaliteInputException("Error, numero de cuenta invalido");
        }
        Mono<AccountEntity> entityMono = repository.findByAccount(accountNumber);
        if (Objects.isNull(entityMono.block())) {
            throw new NotFoundException("Error, no existe la cuenta bancaria con Nro: " + accountNumber);
        }
        return entityMono.map(mapper::entityToDto);
    }

    @Override
    public Flux<AccountDTO> getAllAccountByCustomer(String customerId) {
        if (Objects.isNull(customerId)) {
            throw new InvaliteInputException("Error, codigo de cliente invalido");
        }
        return repository.findAll().filter(x -> x.getCustomerId().equals(customerId)).map(mapper::entityToDto);
    }

    @Override
    public Mono<AccountDTO> createAccount(AccountDTO dto) {
        validateCreateAccount(dto);
        AccountEntity entity = mapper.dtoToEntity(dto);
        entity.setAccount(generateAccountNumber());
        entity.setCreateDate(Calendar.getInstance().getTime());
        return repository.save(entity).onErrorMap(DuplicateKeyException.class, ex -> new InvaliteInputException("Error , ya existe una cuenta con el Nro: " + dto.getAccount())).map(mapper::entityToDto);
    }

    @Override
    public Mono<AccountDTO> makeDepositAccount(double amount, String accountNumber) {
        AccountEntity entity = repository.findByAccount(accountNumber).block();
        if (Objects.isNull(entity)) {
            throw new NotFoundException("Error, no existe la cuenta bancaria con Nro: " + accountNumber);
        }
        if (amount < MIN_DEPOSIT_AMOUNT || amount > MAX_DEPOSIT_AMOUNT) {
            throw new NotFoundException(String.format(Locale.getDefault(), "Error, los limites de DEPOSITO son min: %d sol y max: %d sol", MIN_DEPOSIT_AMOUNT, MAX_DEPOSIT_AMOUNT));
        }
        double newAvailableBalance = entity.getAvailableBalance() + amount;
        entity.setAvailableBalance(newAvailableBalance);
        entity.setModifyDate(Calendar.getInstance().getTime());
        return repository.save(entity).map(mapper::entityToDto);
    }

    @Override
    public Mono<AccountDTO> makeWithdrawal(double amount, String accountNumber) {
        AccountEntity entity = repository.findByAccount(accountNumber).block();
        if (Objects.isNull(entity)) {
            throw new NotFoundException("Error, no existe la cuenta bancaria con Nro: " + accountNumber);
        }
        if (amount < MIN_WITHDRAWAL_AMOUNT || amount > MAX_WITHDRAWAL_AMOUNT) {
            throw new NotFoundException(String.format(Locale.getDefault(), "Error, los limites de RETIRO son min: %d sol y max: %d sol", MIN_WITHDRAWAL_AMOUNT, MAX_WITHDRAWAL_AMOUNT));
        }
        double availableBalance = entity.getAvailableBalance();
        if (amount > availableBalance) {
            throw new BadRequestException("Error,saldo insuficiente.");
        }
        availableBalance -= amount;
        entity.setAvailableBalance(availableBalance);
        entity.setModifyDate(new Date());
        return repository.save(entity).map(mapper::entityToDto);
    }

    @Override
    public Mono<Void> deleteAccount(String accountNumber) {
        return repository.deleteByAccount(accountNumber);
    }

    private void validateCreateAccount(AccountDTO dto) {
        if (Objects.isNull(dto)) {
            throw new InvaliteInputException("Error, body invalido");
        }
        if (Objects.isNull(dto.getAccountType())) {
            throw new InvaliteInputException("Error, tipo de cuenta invalido");
        }
        if (Objects.isNull(dto.getProductId())) {
            throw new InvaliteInputException("Error, codigo de producto invalido");
        }
        if (Objects.isNull(dto.getCustomerId())) {
            throw new InvaliteInputException("Error, codigo de cliente invalido");
        }

        if (dto.getAccountType().equals(Constants.CODE_ACCOUNT_EMPRESARIAL)) {
            if (Objects.isNull(dto.getHolders())) {
                throw new InvaliteInputException("Error, titular o titulares de la cuenta, invalido");
            }
            if (Objects.isNull(dto.getHolders().get(0))) {
                throw new InvaliteInputException("Error, es necesario enviar el titular o titulares de la cuenta");
            }

            for (HolderDTO holder : dto.getHolders()) {
                if (StringUtil.isNullOrEmpty(holder.getCode()) || StringUtil.isNullOrEmpty(holder.getName())) {
                    throw new InvaliteInputException("Error, algunos de los tituales tienen datos invalidos o en blanco");
                }
            }
            if (dto.getProductId().equals(Constants.CODE_PRODUCT_CUENTA_AHORRO)) {
                throw new InvaliteInputException("Error, una cuenta empresarial no puede tener cuentas de ahorro");
            }
            if (dto.getProductId().equals(CODE_PRODUCT_PLAZO_FIJO)) {
                throw new InvaliteInputException("Error, una cuenta empresarial no puede tener cuentas de plazo fijo");
            }
        }
        if (dto.getAccountType().equals(CODE_ACCOUNT_PERSONAL)) {
            if (!Objects.isNull(dto.getHolders())) {
                throw new InvaliteInputException("Error, las cuentas personales tiene como titular al cliente, " + "no hay necesitad de enviarlo.");
            }
            if (!Objects.isNull(dto.getSignatories())) {
                throw new InvaliteInputException("Error, las cuentas personales no requieren firmante autorizados");
            }
            if (dto.getProductId().equals(CODE_PRODUCT_CUENTA_AHORRO)) {
                AccountEntity account = repository.findAll().filter(x -> x.getProductId().equals(CODE_PRODUCT_CUENTA_AHORRO)
                        && x.getCustomerId().equals(dto.getCustomerId())).blockFirst();
                if (account != null) {
                    throw new InvaliteInputException("Error, un cliente personal solo puede tener un máximo de una cuenta de ahorro");
                }
            }
            if (dto.getProductId().equals(CODE_PRODUCT_CUENTA_CORRIENTE)) {
                AccountEntity account = repository.findAll().filter(x -> x.getProductId().equals(CODE_PRODUCT_CUENTA_CORRIENTE)
                        && x.getCustomerId().equals(dto.getCustomerId())).blockFirst();
                if (account != null) {
                    throw new InvaliteInputException("Error, un cliente personal solo puede tener un máximo de una cuenta corriente");
                }
            }
        }
    }

    private String generateAccountNumber() {
        Date todayDate = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        return System.currentTimeMillis() + sdf.format(todayDate);
    }
}