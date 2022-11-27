package com.nttdata.bc39.grupo04.customer.service;

import com.nttdata.bc39.grupo04.api.exceptions.InvaliteInputException;
import com.nttdata.bc39.grupo04.api.exceptions.NotFoundException;
import com.nttdata.bc39.grupo04.customer.dto.CustomerDto;
import com.nttdata.bc39.grupo04.customer.persistence.CustomerEntity;
import com.nttdata.bc39.grupo04.customer.persistence.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Calendar;
import java.util.Objects;

@Service
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository repository;
    private final CustomerMapper mapper;

    @Autowired
    public CustomerServiceImpl(CustomerRepository repository, CustomerMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Flux<CustomerDto> getAllCustomers() {
        return repository.findAll().map(mapper::entityToDto);
    }

    @Override
    public Mono<CustomerDto> getCustomerById(String customerId) {
        if (Objects.isNull(customerId)) {
            throw new InvaliteInputException("Invalid customer with code: " + customerId);
        }
        Mono<CustomerEntity> entityMono = repository.findByCode(customerId);
        if (Objects.isNull(entityMono.block())) {
            throw new NotFoundException("The customer with code: " + customerId + " not exists");
        }
        return entityMono.map(mapper::entityToDto);
    }

    @Override
    public Mono<Void> deleteCustomerById(String customerId) {
        Mono<CustomerEntity> entityMono = repository.findByCode(customerId);
        if (Objects.isNull(entityMono.block())) {
            throw new NotFoundException("The customer with code: " + customerId + " not exists");
        }
        return repository.deleteByCode(customerId);
    }

    @Override
    public Mono<CustomerDto> createCustomer(CustomerDto customerDto) {
        CustomerEntity entity = mapper.dtoToEntity(customerDto);
        entity.setDate(Calendar.getInstance().getTime());
        return repository.save(entity)
                .onErrorMap(DuplicateKeyException.class,
                        ex -> new InvaliteInputException("Duplicate key, customer with code: " + customerDto.getCode()))
                .map(mapper::entityToDto);
    }

    @Override
    public Mono<CustomerDto> updateCustomerById(String customerId, CustomerDto customerDto) {
        CustomerEntity entity = repository.findByCode(customerId).block();
        if (Objects.isNull(entity)) {
            throw new NotFoundException("The customer with code: " + customerId + " not exists");
        }
        entity.setName(customerDto.getName());
        return repository.save(entity).map(mapper::entityToDto);
    }
}
