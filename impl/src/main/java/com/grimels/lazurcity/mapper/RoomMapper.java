package com.grimels.lazurcity.mapper;

import com.grimels.lazurcity.entity.RoomEntity;
import com.grimels.lazurcityapi.model.Room;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR, uses = {AccommodationMapper.class, ClientMapper.class})
public interface RoomMapper {

    @Mapping(target = "isBusy", expression = "java(com.grimels.lazurcity.util.RoomsUtil.isBusyRoom(roomEntity))")
    @Mapping(source = "accommodationList", target = "accommodations")
    Room fromRoomEntity(RoomEntity roomEntity);

    @Mapping(source = "isBusy", target = "isBusy")
    @Mapping(source = "roomEntity.accommodationList", target = "accommodations")
    Room fromRoomEntity(RoomEntity roomEntity, Boolean isBusy);

    @Mapping(source = "room.id", target = "id")
    @Mapping(source = "room.createdDate", target = "createdDate")
    @Mapping(source = "room.modifiedDate", target = "modifiedDate")
    RoomEntity toRoomEntity(Room room);

}
