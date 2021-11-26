package com.grimels.lazurcity.service;

import com.grimels.lazurcity.entity.ClientEntity;
import com.grimels.lazurcityapi.model.Client;

import java.util.List;
import java.util.Optional;

public interface ClientService {

    List<Client> findAll();

    Optional<Client> findById(int clientId);

    Optional<Client> findFirstByName(String clientName);

    Optional<Client> findFirstByPhoneNumber(String phoneNumber);

    Client saveClient(Client clientEntity);

}
