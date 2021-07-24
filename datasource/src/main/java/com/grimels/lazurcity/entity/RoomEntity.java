package com.grimels.lazurcity.entity;

import com.grimels.lazurcity.entity.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "rooms")
@RequiredArgsConstructor
@Getter
@Setter
@ToString
@SuperBuilder
@AllArgsConstructor
public class RoomEntity extends BaseEntity {

    @Column(name = "name")
    private String name;
    @Column(name = "description")
    private String description;
    @Column(name = "type")
    private String type;

    @OneToMany(mappedBy = "room", fetch = FetchType.LAZY)
    @ToString.Exclude
    private Set<AccommodationEntity> accommodationList;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        RoomEntity that = (RoomEntity) o;

        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            super.getId(),
            super.getCreatedDate(),
            super.getModifiedDate(),
            name,
            description,
            type
        );
    }
}
