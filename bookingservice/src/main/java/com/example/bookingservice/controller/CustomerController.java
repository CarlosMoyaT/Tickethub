package com.example.bookingservice.controller;

import com.example.bookingservice.entity.Customer;
import com.example.bookingservice.request.CustomerRequest;
import com.example.bookingservice.respository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {

    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerController(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @GetMapping
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable Long id) {
        return customerRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<Customer> getCustomerByEmail(@PathVariable String email) {
        return customerRepository.findByEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Customer> createCustomer(@RequestBody CustomerRequest request) {
        if (customerRepository.findByEmail(request.getEmail()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        Customer customer = Customer.builder()
                .id(System.currentTimeMillis())
                .name(request.getName())
                .email(request.getEmail())
                .address(request.getAddress() != null ? request.getAddress() : "")
                .build();
        Customer saved = customerRepository.save(customer);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }
}
