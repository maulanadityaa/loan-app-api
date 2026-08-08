package org.enigma.livecodeloan.service.impl;

import lombok.RequiredArgsConstructor;
import org.enigma.livecodeloan.constant.EStatus;
import org.enigma.livecodeloan.model.entity.Customer;
import org.enigma.livecodeloan.model.exception.ApplicationException;
import org.enigma.livecodeloan.model.request.CustomerRequest;
import org.enigma.livecodeloan.model.response.CustomerResponse;
import org.enigma.livecodeloan.repository.CustomerRepository;
import org.enigma.livecodeloan.service.CustomerService;
import org.enigma.livecodeloan.util.Helper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    @Override
    public CustomerResponse create(CustomerRequest customerRequest) {
        try {
            Customer customer = toCustomer(customerRequest);
            customerRepository.save(customer);

            return toCustomerResponse(customer);
        } catch (ParseException e) {
            e.printStackTrace();
            throw new ApplicationException("Cannot create customer", String.format("Cannot parse date=%s", customerRequest.getDateOfBirth()), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public CustomerResponse update(CustomerRequest customerRequest) {
        Customer customer = customerRepository.findById(customerRequest.getId()).orElse(null);

        try {
            if (customer != null) {
                customer = Customer.builder()
                        .id(customer.getId())
                        .firstName(customerRequest.getFirstName())
                        .lastName(customerRequest.getLastName())
                        .dob(Helper.stringToDate(customerRequest.getDateOfBirth()))
                        .mobilePhone(customerRequest.getPhone())
                        .marriageStatus(customerRequest.getMarriageStatus())
                        .status(EStatus.ACTIVE)
                        .user(customer.getUser())
                        .build();

                customerRepository.save(customer);
                return toCustomerResponse(customer);
            } else {
                throw new ApplicationException("Cannot not found", String.format("Customer with id=%s", customerRequest.getId()), HttpStatus.NOT_FOUND);
            }
        } catch (ParseException e) {
            e.printStackTrace();
            throw new ApplicationException("Cannot create customer", String.format("Cannot parse date=%s", customerRequest.getDateOfBirth()), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public CustomerResponse getById(String id) {
        Customer customer = customerRepository.findById(id).orElse(null);

        try {
            if (customer != null) {
                return toCustomerResponse(customer);
            } else {
                throw new ApplicationException("Customer not found", String.format("Cannot find with id=%s", id), HttpStatus.NOT_FOUND);
            }
        } catch (ParseException e) {
            e.printStackTrace();
            throw new ApplicationException("Cannot fetch customer", String.format("Cannot parse date=%s", id), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public List<CustomerResponse> getAll() {
        List<Customer> customers = customerRepository.findAll();
        List<CustomerResponse> customerResponseList = new ArrayList<>();

        try {
            for (Customer customer : customers) {
                if (customer.getStatus().equals(EStatus.ACTIVE)) {
                    customerResponseList.add(toCustomerResponse(customer));
                }
            }
            return customerResponseList;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void delete(String id) {
        Customer customer = customerRepository.findById(id).orElse(null);
        if (customer != null) {
            customer.setStatus(EStatus.INACTIVE);
            customerRepository.save(customer);
        } else {
            throw new ApplicationException("Cannot not found", String.format("Customer with id=%s", id), HttpStatus.NOT_FOUND);
        }
    }

    private static Customer toCustomer(CustomerRequest customerRequest) throws ParseException {
        return Customer.builder()
                .firstName(customerRequest.getFirstName())
                .lastName(customerRequest.getLastName())
                .dob(Helper.stringToDate(customerRequest.getDateOfBirth()))
                .mobilePhone(customerRequest.getPhone())
                .marriageStatus(customerRequest.getMarriageStatus())
                .status(EStatus.ACTIVE)
                .user(customerRequest.getUserCredential())
                .build();
    }

    private static CustomerResponse toCustomerResponse(Customer customer) throws ParseException {
        return CustomerResponse.builder()
                .id(customer.getId())
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .dateOfBirth(Helper.dateToString(customer.getDob()))
                .marriageStatus(customer.getMarriageStatus())
                .phone(customer.getMobilePhone())
                .build();
    }
}
