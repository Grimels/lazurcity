package com.grimels.lazurcity.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.grimels.lazurcity.entity.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Set;

@Entity
@Table(name = "room_type")
@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties({"roomList"})
public class RoomTypeEntity extends BaseEntity {

    @Column(name = "type")
    private String type;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "roomType")
    private Set<RoomEntity> roomList;

}
