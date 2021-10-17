package com.grimels.lazurcity.entity;

import com.grimels.lazurcity.entity.base.BaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "accommodations")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class AccommodationEntity extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "client_id", referencedColumnName = "id", nullable = false)
    private ClientEntity client;
    @ManyToOne
    @JoinColumn(name = "room_id", referencedColumnName = "id", nullable = false)
    private RoomEntity room;

    @Column(name = "price")
    private Double price;
    @Column(name = "is_final")
    private Boolean isFinal;
    @Column(name = "start_date")
    private LocalDate startDate;
    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "comment")
    private String comment;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        AccommodationEntity that = (AccommodationEntity) o;

        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            super.getId(),
            super.getCreatedDate(),
            super.getModifiedDate(),
            client.getId(),
            room.getId(),
            price,
            startDate,
            endDate,
            quantity
        );
    }
}
