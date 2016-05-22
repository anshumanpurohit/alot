package com.agencylot.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A ProductLine.
 */
@Entity
@Table(name = "product_line")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "productline")
public class ProductLine implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "start_effective_date")
    private LocalDate startEffectiveDate;

    @Column(name = "end_effective_date")
    private LocalDate endEffectiveDate;

    @OneToOne
    @JoinColumn(unique = true)
    private ProductLineDef productLineDef;

    @OneToMany(mappedBy = "productLine")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Discount> discounts = new HashSet<>();

    @OneToMany(mappedBy = "productLine")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<PolicyDriver> drivers = new HashSet<>();

    @OneToMany(mappedBy = "productLine")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<NamedInsured> insureds = new HashSet<>();

    @OneToMany(mappedBy = "productLine")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<PersonalAutoVehicle> vehicles = new HashSet<>();

    @OneToMany(mappedBy = "productLine")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Coverage> coverages = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getStartEffectiveDate() {
        return startEffectiveDate;
    }

    public void setStartEffectiveDate(LocalDate startEffectiveDate) {
        this.startEffectiveDate = startEffectiveDate;
    }

    public LocalDate getEndEffectiveDate() {
        return endEffectiveDate;
    }

    public void setEndEffectiveDate(LocalDate endEffectiveDate) {
        this.endEffectiveDate = endEffectiveDate;
    }

    public ProductLineDef getProductLineDef() {
        return productLineDef;
    }

    public void setProductLineDef(ProductLineDef productLineDef) {
        this.productLineDef = productLineDef;
    }

    public Set<Discount> getDiscounts() {
        return discounts;
    }

    public void setDiscounts(Set<Discount> discounts) {
        this.discounts = discounts;
    }

    public Set<PolicyDriver> getDrivers() {
        return drivers;
    }

    public void setDrivers(Set<PolicyDriver> policyDrivers) {
        this.drivers = policyDrivers;
    }

    public Set<NamedInsured> getInsureds() {
        return insureds;
    }

    public void setInsureds(Set<NamedInsured> namedInsureds) {
        this.insureds = namedInsureds;
    }

    public Set<PersonalAutoVehicle> getVehicles() {
        return vehicles;
    }

    public void setVehicles(Set<PersonalAutoVehicle> personalAutoVehicles) {
        this.vehicles = personalAutoVehicles;
    }

    public Set<Coverage> getCoverages() {
        return coverages;
    }

    public void setCoverages(Set<Coverage> coverages) {
        this.coverages = coverages;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ProductLine productLine = (ProductLine) o;
        if(productLine.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, productLine.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ProductLine{" +
            "id=" + id +
            ", startEffectiveDate='" + startEffectiveDate + "'" +
            ", endEffectiveDate='" + endEffectiveDate + "'" +
            '}';
    }
}
