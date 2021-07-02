package com.grimels.lazurcity.entity;

import com.grimels.lazurcity.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "accommodations")
@EqualsAndHashCode(callSuper = true)
@Data
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
    @Column(name = "start_date")
    private Date startDate;
    @Column(name = "end_date")
    private Date endDate;

    @Column(name = "adults_quantity")
    private Integer adultsQuantity;
    @Column(name = "children_quantity")
    private Integer childrenQuantity;

}
