package com.grimels.lazurcity.repository;

import com.grimels.lazurcity.entity.AccommodationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface AccommodationRepository extends JpaRepository<AccommodationEntity, Integer> {

    @Query(
        value = "SELECT AE " +
            "FROM AccommodationEntity AE " +
            "WHERE (AE.startDate BETWEEN ?1 AND ?2) OR (AE.endDate BETWEEN ?1 AND ?2)"
    )
    List<AccommodationEntity> findAllByDateInRange(Date startDate, Date endDate);

}
