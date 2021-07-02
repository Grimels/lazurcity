package com.grimels.lazurcity.service.impl;

import com.grimels.lazurcity.entity.ClientEntity;
import com.grimels.lazurcity.mapper.ClientMapper;
import com.grimels.lazurcity.repository.ClientRepository;
import com.grimels.lazurcity.service.ClientService;
import com.grimels.lazurcityapi.model.ClientDTO;
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
    public List<ClientDTO> findAll() {
        return clientRepository.findAll().stream()
            .map(clientMapper::toClientDTO)
            .collect(Collectors.toList());
    }

    @Override
    public Optional<ClientDTO> findById(int clientId) {
        return clientRepository.findById(clientId)
            .map(clientMapper::toClientDTO);
    }

    @Override
    @Transactional
    public ClientDTO saveClient(ClientDTO clientCreationRequest) {
        ClientEntity clientEntity = clientMapper.toClientEntity(clientCreationRequest);
        ClientEntity savedClient = clientRepository.save(clientEntity);

        return clientMapper.toClientDTO(savedClient);
    }

}
