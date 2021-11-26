package com.grimels.lazurcity.mapper;

import com.grimels.lazurcity.entity.ClientEntity;
import com.grimels.lazurcityapi.model.Client;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface ClientMapper {

    Client fromClientEntity(ClientEntity clientEntity);

    ClientEntity toClientEntity(Client client);

}
