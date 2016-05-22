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
 * A PersonalAutoVehicle.
 */
@Entity
@Table(name = "personal_auto_vehicle")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "personalautovehicle")
public class PersonalAutoVehicle implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "fixed_id")
    private String fixedId;

    @Column(name = "year")
    private Integer year;

    @Column(name = "make")
    private String make;

    @Column(name = "model")
    private String model;

    @Column(name = "body_style")
    private String bodyStyle;

    @Column(name = "symbols")
    private String symbols;

    @ManyToOne
    private PolicyDriver policyDriver;

    @ManyToOne
    private ProductLine productLine;

    @OneToOne
    @JoinColumn(unique = true)
    private Address garageLocation;

    @OneToMany(mappedBy = "personalAutoVehicle")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Loss> losses = new HashSet<>();

    @OneToMany(mappedBy = "personalAutoVehicle")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Coverage> coverages = new HashSet<>();

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

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getBodyStyle() {
        return bodyStyle;
    }

    public void setBodyStyle(String bodyStyle) {
        this.bodyStyle = bodyStyle;
    }

    public String getSymbols() {
        return symbols;
    }

    public void setSymbols(String symbols) {
        this.symbols = symbols;
    }

    public PolicyDriver getPolicyDriver() {
        return policyDriver;
    }

    public void setPolicyDriver(PolicyDriver policyDriver) {
        this.policyDriver = policyDriver;
    }

    public ProductLine getProductLine() {
        return productLine;
    }

    public void setProductLine(ProductLine productLine) {
        this.productLine = productLine;
    }

    public Address getGarageLocation() {
        return garageLocation;
    }

    public void setGarageLocation(Address address) {
        this.garageLocation = address;
    }

    public Set<Loss> getLosses() {
        return losses;
    }

    public void setLosses(Set<Loss> losses) {
        this.losses = losses;
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
        PersonalAutoVehicle personalAutoVehicle = (PersonalAutoVehicle) o;
        if(personalAutoVehicle.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, personalAutoVehicle.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "PersonalAutoVehicle{" +
            "id=" + id +
            ", fixedId='" + fixedId + "'" +
            ", year='" + year + "'" +
            ", make='" + make + "'" +
            ", model='" + model + "'" +
            ", bodyStyle='" + bodyStyle + "'" +
            ", symbols='" + symbols + "'" +
            '}';
    }
}
