package com.grimels.lazurcity.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.grimels.lazurcity.entity.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Set;

@Entity
@Table(name = "rooms")
@EqualsAndHashCode(callSuper = true)
@RequiredArgsConstructor
@Data
@SuperBuilder
@AllArgsConstructor
@JsonIgnoreProperties({"accommodationList"})
public class RoomEntity extends BaseEntity {

    @Column(name = "name")
    private String name;
    @Column(name = "description")
    private String description;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "type_id", nullable = false)
    private RoomTypeEntity roomType;

    @Column(name = "max_adults_quantity")
    private Integer maxAdultsQuantity;
    @Column(name = "max_children_quantity")
    private Integer maxChildrenQuantity;

    @OneToMany(mappedBy = "room", fetch = FetchType.LAZY)
    private Set<AccommodationEntity> accommodationList;

}
