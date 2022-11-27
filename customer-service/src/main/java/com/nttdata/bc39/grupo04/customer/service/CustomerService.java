package com.nttdata.bc39.grupo04.customer.service;

import com.nttdata.bc39.grupo04.customer.dto.CustomerDto;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CustomerService {
    Flux<CustomerDto> getAllCustomers();

    Mono<CustomerDto> getCustomerById(String customerId);

    Mono<Void> deleteCustomerById(String customerId);

    Mono<CustomerDto> createCustomer(CustomerDto customerDto);

    Mono<CustomerDto> updateCustomerById(String customerId, CustomerDto customerDto);
}
