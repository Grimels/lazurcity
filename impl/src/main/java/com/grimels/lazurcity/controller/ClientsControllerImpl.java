package com.grimels.lazurcity.controller;

import com.grimels.lazurcity.entity.ClientEntity;
import com.grimels.lazurcity.exception.NotFoundStatusException;
import com.grimels.lazurcity.service.ClientService;
import com.grimels.lazurcityapi.controller.ClientsController;
import com.grimels.lazurcityapi.model.ClientDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ClientsControllerImpl implements ClientsController {

    private ClientService clientService;

    @Override
    public List<ClientDTO> getClients() {
        return clientService.findAll();
    }

    @Override
    public ClientDTO getClient(int clientId) {
        return clientService.findById(clientId)
            .orElseThrow(() -> NotFoundStatusException.createEntityNotFoundByIdError(ClientEntity.class.getName(), clientId));
    }

    @Override
    public ClientDTO saveClient(ClientDTO clientCreationRequest) {
        return clientService.saveClient(clientCreationRequest);
    }

}
