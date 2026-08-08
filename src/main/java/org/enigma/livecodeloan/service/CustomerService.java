package org.enigma.livecodeloan.service;

import org.enigma.livecodeloan.model.request.CustomerRequest;
import org.enigma.livecodeloan.model.response.CustomerResponse;

import java.util.List;

public interface CustomerService {
    CustomerResponse create(CustomerRequest customerRequest);

    CustomerResponse update(CustomerRequest customerRequest);

    CustomerResponse getById(String id);

    List<CustomerResponse> getAll();

    void delete(String id);
}
