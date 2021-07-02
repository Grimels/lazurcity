package com.grimels.lazurcity.repository;

import com.grimels.lazurcity.entity.ClientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<ClientEntity, Integer> {
}
