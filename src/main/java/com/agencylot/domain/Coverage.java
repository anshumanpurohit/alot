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
 * A Coverage.
 */
@Entity
@Table(name = "coverage")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "coverage")
public class Coverage implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "fixed_id")
    private String fixedId;

    @ManyToOne
    private ProductLine productLine;

    @OneToOne
    @JoinColumn(unique = true)
    private CoverageDef definition;

    @OneToMany(mappedBy = "coverage")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<CoverageTerm> terms = new HashSet<>();

    @ManyToOne
    private PersonalAutoVehicle personalAutoVehicle;

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

    public ProductLine getProductLine() {
        return productLine;
    }

    public void setProductLine(ProductLine productLine) {
        this.productLine = productLine;
    }

    public CoverageDef getDefinition() {
        return definition;
    }

    public void setDefinition(CoverageDef coverageDef) {
        this.definition = coverageDef;
    }

    public Set<CoverageTerm> getTerms() {
        return terms;
    }

    public void setTerms(Set<CoverageTerm> coverageTerms) {
        this.terms = coverageTerms;
    }

    public PersonalAutoVehicle getPersonalAutoVehicle() {
        return personalAutoVehicle;
    }

    public void setPersonalAutoVehicle(PersonalAutoVehicle personalAutoVehicle) {
        this.personalAutoVehicle = personalAutoVehicle;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Coverage coverage = (Coverage) o;
        if(coverage.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, coverage.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Coverage{" +
            "id=" + id +
            ", fixedId='" + fixedId + "'" +
            '}';
    }
}
