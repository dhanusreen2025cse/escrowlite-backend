package com.escrowlite.escrowlite.controller;

import com.escrowlite.escrowlite.model.Client;
import com.escrowlite.escrowlite.model.Freelancer;
import com.escrowlite.escrowlite.repository.ClientRepository;
import com.escrowlite.escrowlite.repository.FreelancerRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/setup")
public class SetupController {

    private final ClientRepository clientRepository;
    private final FreelancerRepository freelancerRepository;

    public SetupController(ClientRepository clientRepository, FreelancerRepository freelancerRepository) {
        this.clientRepository = clientRepository;
        this.freelancerRepository = freelancerRepository;
    }

    @PostMapping("/seed")
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, Object> seedDefaults() {
        Client client = clientRepository.findByEmail("alice@example.com")
            .orElseGet(() -> clientRepository.save(Client.builder()
                .name("Alice")
                .email("alice@example.com")
                .build()));
        Freelancer freelancer = freelancerRepository.findByEmail("bob@example.com")
            .orElseGet(() -> freelancerRepository.save(Freelancer.builder()
                .name("Bob")
                .email("bob@example.com")
                .build()));
        return Map.of("client", client, "freelancer", freelancer);
    }
}