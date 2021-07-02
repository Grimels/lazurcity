package com.grimels.lazurcity.repository;

import com.grimels.lazurcity.entity.RoomTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoomTypeRepository extends JpaRepository<RoomTypeEntity, Integer> {

    Optional<RoomTypeEntity> findOneByType(String type);

}
