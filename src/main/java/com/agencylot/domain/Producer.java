package com.agencylot.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Producer.
 */
@Entity
@Table(name = "producer")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "producer")
public class Producer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "fixed_id")
    private String fixedId;

    @Column(name = "deleted")
    private Boolean deleted;

    @Column(name = "external_reference_id")
    private String externalReferenceId;

    @OneToMany(mappedBy = "producer")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<AddressBook> addressBooks = new HashSet<>();

    @OneToMany(mappedBy = "producer")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Activity> activities = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFixedId() {
        return fixedId;
    }

    public void setFixedId(String fixedId) {
        this.fixedId = fixedId;
    }

    public Boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public String getExternalReferenceId() {
        return externalReferenceId;
    }

    public void setExternalReferenceId(String externalReferenceId) {
        this.externalReferenceId = externalReferenceId;
    }

    public Set<AddressBook> getAddressBooks() {
        return addressBooks;
    }

    public void setAddressBooks(Set<AddressBook> addressBooks) {
        this.addressBooks = addressBooks;
    }

    public Set<Activity> getActivities() {
        return activities;
    }

    public void setActivities(Set<Activity> activities) {
        this.activities = activities;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Producer producer = (Producer) o;
        if(producer.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, producer.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Producer{" +
            "id=" + id +
            ", fixedId='" + fixedId + "'" +
            ", deleted='" + deleted + "'" +
            ", externalReferenceId='" + externalReferenceId + "'" +
            '}';
    }
}
