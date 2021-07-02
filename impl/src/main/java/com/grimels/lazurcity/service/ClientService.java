package com.grimels.lazurcity.service;

import com.grimels.lazurcityapi.model.ClientDTO;

import java.util.List;
import java.util.Optional;

public interface ClientService {

    List<ClientDTO> findAll();

    Optional<ClientDTO> findById(int clientId);

    ClientDTO saveClient(ClientDTO clientEntity);

}
