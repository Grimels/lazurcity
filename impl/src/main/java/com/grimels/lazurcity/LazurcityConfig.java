package com.grimels.lazurcity;

import com.grimels.lazurcity.controller.AccommodationsControllerImpl;
import com.grimels.lazurcity.controller.ClientsControllerImpl;
import com.grimels.lazurcity.controller.RoomsControllerImpl;
import com.grimels.lazurcity.controller.UserControllerImpl;
import com.grimels.lazurcity.mapper.AccommodationMapper;
import com.grimels.lazurcity.mapper.ClientMapper;
import com.grimels.lazurcity.mapper.RoomMapper;
import com.grimels.lazurcity.repository.AccommodationRepository;
import com.grimels.lazurcity.repository.ClientRepository;
import com.grimels.lazurcity.repository.RoomRepository;
import com.grimels.lazurcity.service.AccommodationService;
import com.grimels.lazurcity.service.ClientService;
import com.grimels.lazurcity.service.RoomService;
import com.grimels.lazurcity.service.impl.AccommodationServiceImpl;
import com.grimels.lazurcity.service.impl.ClientServiceImpl;
import com.grimels.lazurcity.service.impl.RoomServiceImpl;
import com.grimels.lazurcity.validation.AccommodationValidator;
import com.grimels.lazurcityapi.controller.AccommodationsController;
import com.grimels.lazurcityapi.controller.ClientsController;
import com.grimels.lazurcityapi.controller.RoomsController;
import com.grimels.lazurcityapi.controller.UserController;
import com.grimels.lazurcityapi.model.user.UserCredentials;
import org.springframework.boot.context.properties.ConfigurationProperties;
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
                                   RoomMapper roomMapper,
                                   AccommodationMapper accommodationMapper) {
        return new RoomServiceImpl(roomRepository, roomMapper, accommodationMapper);
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
                                                     ClientService clientService,
                                                     ClientRepository clientRepository,
                                                     RoomRepository roomRepository,
                                                     AccommodationMapper accommodationMapper,
                                                     RoomMapper roomMapper,
                                                     ClientMapper clientMapper,
                                                     AccommodationValidator accommodationValidator) {
        return new AccommodationServiceImpl(
                accommodationRepository,
                clientService,
                clientRepository,
                roomRepository,
                accommodationMapper,
                roomMapper,
                clientMapper,
                accommodationValidator
        );
    }

    @Bean
    public AccommodationValidator accommodationValidation() {
        return new AccommodationValidator();
    }

    @Bean
    public AccommodationsController accommodationsController(AccommodationService accommodationService) {
        return new AccommodationsControllerImpl(accommodationService);
    }

    @Bean
    @ConfigurationProperties(prefix = "signin.user")
    public UserCredentials userCredentials() {
        return new UserCredentials();
    }

    @Bean
    public UserController userController(UserCredentials userCredentials) {
        return new UserControllerImpl(userCredentials);
    }

}
