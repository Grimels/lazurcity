package com.grimels.lazurcity.service;

import com.grimels.lazurcityapi.model.Client;

import java.util.List;
import java.util.Optional;

public interface ClientService {

    List<Client> findAll();

    Optional<Client> findById(int clientId);

    Client saveClient(Client clientEntity);

}
