package com.escrowlite.escrowlite.repository;

import com.escrowlite.escrowlite.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Long> {
}