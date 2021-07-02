package com.grimels.lazurcity.repository;

import com.grimels.lazurcity.entity.RoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepository extends JpaRepository<RoomEntity, Integer> {
}
