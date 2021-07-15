package com.grimels.lazurcity.entity;

import com.grimels.lazurcity.entity.base.BaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "clients")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class ClientEntity extends BaseEntity {

    @Column(name = "name", length = 255)
    private String name;
    @Column(name = "comment")
    private String comment;

    @Column(name = "phone_number", length = 15)
    private String phoneNumber;

    @OneToMany(mappedBy = "client")
    @ToString.Exclude
    private Set<AccommodationEntity> accommodationList;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ClientEntity that = (ClientEntity) o;

        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            super.getId(),
            super.getCreatedDate(),
            super.getModifiedDate(),
            name,
            comment,
            phoneNumber
        );
    }
}
