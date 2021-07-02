package com.grimels.lazurcity.repository;

import com.grimels.lazurcity.entity.AccommodationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccommodationRepository extends JpaRepository<AccommodationEntity, Integer> {
}
