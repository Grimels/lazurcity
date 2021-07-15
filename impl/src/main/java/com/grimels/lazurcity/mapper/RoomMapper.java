package com.grimels.lazurcity.mapper;

import com.grimels.lazurcity.entity.RoomEntity;
import com.grimels.lazurcityapi.model.Room;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface RoomMapper {

    @Mapping(target = "isBusy", expression = "java(com.grimels.lazurcity.util.RoomsUtil.isBusyRoom(roomEntity))")
    Room fromRoomEntity(RoomEntity roomEntity);

    @Mappings({
        @Mapping(source = "room.id", target = "id"),
        @Mapping(source = "room.createdDate", target = "createdDate"),
        @Mapping(source = "room.modifiedDate", target = "modifiedDate")
    })
    RoomEntity toRoomEntity(Room room);

}
