package com.grimels.lazurcity.mapper;

import com.grimels.lazurcity.entity.RoomEntity;
import com.grimels.lazurcity.entity.RoomTypeEntity;
import com.grimels.lazurcityapi.model.RoomDTO;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface RoomMapper {

    @Mapping(source = "roomEntity.roomType.type", target = "type")
    @Mapping(target = "isBusy", expression = "java(com.grimels.lazurcity.util.RoomsUtil.isBusyRoom(roomEntity))")
    RoomDTO toRoomDTO(RoomEntity roomEntity);

    @Mappings({
        @Mapping(source = "roomTypeEntity", target = "roomType"),
        @Mapping(source = "roomDTO.id", target = "id"),
        @Mapping(source = "roomDTO.createdDate", target = "createdDate"),
        @Mapping(source = "roomDTO.modifiedDate", target = "modifiedDate")
    })
    RoomEntity toRoomEntity(RoomDTO roomDTO, RoomTypeEntity roomTypeEntity);

}
