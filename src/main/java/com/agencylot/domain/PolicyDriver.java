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
 * A PolicyDriver.
 */
@Entity
@Table(name = "policy_driver")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "policydriver")
public class PolicyDriver implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "fixed_id")
    private String fixedId;

    @OneToOne
    @JoinColumn(unique = true)
    private Violation violations;

    @OneToOne
    @JoinColumn(unique = true)
    private PolicyContact policyContact;

    @OneToMany(mappedBy = "policyDriver")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<PersonalAutoVehicle> vehicles = new HashSet<>();

    @ManyToOne
    private ProductLine productLine;

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

    public Violation getViolations() {
        return violations;
    }

    public void setViolations(Violation violation) {
        this.violations = violation;
    }

    public PolicyContact getPolicyContact() {
        return policyContact;
    }

    public void setPolicyContact(PolicyContact policyContact) {
        this.policyContact = policyContact;
    }

    public Set<PersonalAutoVehicle> getVehicles() {
        return vehicles;
    }

    public void setVehicles(Set<PersonalAutoVehicle> personalAutoVehicles) {
        this.vehicles = personalAutoVehicles;
    }

    public ProductLine getProductLine() {
        return productLine;
    }

    public void setProductLine(ProductLine productLine) {
        this.productLine = productLine;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PolicyDriver policyDriver = (PolicyDriver) o;
        if(policyDriver.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, policyDriver.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "PolicyDriver{" +
            "id=" + id +
            ", fixedId='" + fixedId + "'" +
            '}';
    }
}
