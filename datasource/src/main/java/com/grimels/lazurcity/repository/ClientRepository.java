package com.grimels.lazurcity.repository;

import com.grimels.lazurcity.entity.ClientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<ClientEntity, Integer> {

    Optional<ClientEntity> findFirstByName(String clientName);

    Optional<ClientEntity> findFirstByPhoneNumber(String phoneNumber);

}
