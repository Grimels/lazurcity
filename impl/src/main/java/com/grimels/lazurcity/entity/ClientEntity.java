package com.grimels.lazurcity.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.grimels.lazurcity.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Set;

@Entity
@Table(name = "clients")
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@JsonIgnoreProperties({"accommodationList"})
public class ClientEntity extends BaseEntity {

    @Column(name = "name", length = 255)
    private String name;
    @Column(name = "comment")
    private String comment;

    @Column(name = "phone_number", length = 15)
    private String phoneNumber;

    @OneToMany(mappedBy = "client")
    private Set<AccommodationEntity> accommodationList;

}
