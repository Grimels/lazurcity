package com.grimels.lazurcity.entity;

import com.grimels.lazurcity.entity.base.BaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "accommodations")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class AccommodationEntity extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "clientId", referencedColumnName = "id", nullable = false)
    private ClientEntity client;
    @ManyToOne
    @JoinColumn(name = "roomId", referencedColumnName = "id", nullable = false)
    private RoomEntity room;

    @Column(name = "price")
    private Double price;
    @Column(name = "is_final")
    private Boolean isFinal;
    @Column(name = "start_date")
    private Date startDate;
    @Column(name = "end_date")
    private Date endDate;

    @Column(name = "quantity")
    private Integer quantity;

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
