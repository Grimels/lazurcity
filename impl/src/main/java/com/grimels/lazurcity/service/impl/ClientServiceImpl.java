package com.grimels.lazurcity.service.impl;

import com.grimels.lazurcity.entity.ClientEntity;
import com.grimels.lazurcity.mapper.ClientMapper;
import com.grimels.lazurcity.repository.ClientRepository;
import com.grimels.lazurcity.service.ClientService;
import com.grimels.lazurcityapi.model.Client;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Data
@AllArgsConstructor
public class ClientServiceImpl implements ClientService {

    private ClientRepository clientRepository;
    private ClientMapper clientMapper;

    @Override
    public List<Client> findAll() {
        return clientRepository.findAll().stream()
            .map(clientMapper::fromClientEntity)
            .collect(Collectors.toList());
    }

    @Override
    public Optional<Client> findById(int clientId) {
        return clientRepository.findById(clientId)
            .map(clientMapper::fromClientEntity);
    }

    @Override
    @Transactional
    public Client saveClient(Client clientCreationRequest) {
        ClientEntity clientEntity = clientMapper.toClientEntity(clientCreationRequest);
        ClientEntity savedClient = clientRepository.save(clientEntity);

        return clientMapper.fromClientEntity(savedClient);
    }

}
