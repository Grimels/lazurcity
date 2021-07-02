package com.grimels.lazurcity;

import com.grimels.lazurcity.controller.AccommodationsControllerImpl;
import com.grimels.lazurcity.controller.ClientsControllerImpl;
import com.grimels.lazurcity.controller.RoomsControllerImpl;
import com.grimels.lazurcity.mapper.AccommodationMapper;
import com.grimels.lazurcity.mapper.ClientMapper;
import com.grimels.lazurcity.mapper.RoomMapper;
import com.grimels.lazurcity.repository.AccommodationRepository;
import com.grimels.lazurcity.repository.ClientRepository;
import com.grimels.lazurcity.repository.RoomRepository;
import com.grimels.lazurcity.repository.RoomTypeRepository;
import com.grimels.lazurcity.service.AccommodationService;
import com.grimels.lazurcity.service.ClientService;
import com.grimels.lazurcity.service.RoomService;
import com.grimels.lazurcity.service.impl.AccommodationServiceImpl;
import com.grimels.lazurcity.service.impl.ClientServiceImpl;
import com.grimels.lazurcity.service.impl.RoomServiceImpl;
import com.grimels.lazurcity.validation.AccommodationValidation;
import com.grimels.lazurcityapi.controller.AccommodationsController;
import com.grimels.lazurcityapi.controller.ClientsController;
import com.grimels.lazurcityapi.controller.RoomsController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@ComponentScan
@EnableJpaRepositories
public class LazurcityConfig {

    @Bean
    public RoomService roomService(RoomRepository roomRepository,
                                   RoomTypeRepository roomTypeRepository,
                                   RoomMapper roomMapper) {
        return new RoomServiceImpl(roomRepository, roomTypeRepository, roomMapper);
    }

    @Bean
    public RoomsController roomsController(RoomService roomService) {
        return new RoomsControllerImpl(roomService);
    }

    @Bean
    public ClientService clientService(ClientRepository clientRepository,
                                       ClientMapper clientMapper) {
        return new ClientServiceImpl(clientRepository, clientMapper);
    }

    @Bean
    public ClientsController clientsController(ClientService clientService) {
        return new ClientsControllerImpl(clientService);
    }

    @Bean
    public AccommodationService accommodationService(AccommodationRepository accommodationRepository,
                                                     AccommodationMapper accommodationMapper,
                                                     AccommodationValidation accommodationValidation) {
        return new AccommodationServiceImpl(accommodationRepository, accommodationMapper, accommodationValidation);
    }

    @Bean
    public AccommodationValidation accommodationValidation() {
        return new AccommodationValidation();
    }

    @Bean
    public AccommodationsController accommodationsController(AccommodationService accommodationService) {
        return new AccommodationsControllerImpl(accommodationService);
    }

}
