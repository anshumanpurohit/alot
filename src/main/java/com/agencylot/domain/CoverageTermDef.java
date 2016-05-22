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
 * A CoverageTermDef.
 */
@Entity
@Table(name = "coverage_term_def")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "coveragetermdef")
public class CoverageTermDef implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "description")
    private String description;

    @Column(name = "begin_effective_date")
    private LocalDate beginEffectiveDate;

    @Column(name = "end_effective_date")
    private LocalDate endEffectiveDate;

    @Column(name = "state")
    private String state;

    @ManyToOne
    private CoverageDef coverageDef;

    @OneToMany(mappedBy = "coverageTermDef")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<CoverageTermOptionDef> covTermOptionDefs = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getBeginEffectiveDate() {
        return beginEffectiveDate;
    }

    public void setBeginEffectiveDate(LocalDate beginEffectiveDate) {
        this.beginEffectiveDate = beginEffectiveDate;
    }

    public LocalDate getEndEffectiveDate() {
        return endEffectiveDate;
    }

    public void setEndEffectiveDate(LocalDate endEffectiveDate) {
        this.endEffectiveDate = endEffectiveDate;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public CoverageDef getCoverageDef() {
        return coverageDef;
    }

    public void setCoverageDef(CoverageDef coverageDef) {
        this.coverageDef = coverageDef;
    }

    public Set<CoverageTermOptionDef> getCovTermOptionDefs() {
        return covTermOptionDefs;
    }

    public void setCovTermOptionDefs(Set<CoverageTermOptionDef> coverageTermOptionDefs) {
        this.covTermOptionDefs = coverageTermOptionDefs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CoverageTermDef coverageTermDef = (CoverageTermDef) o;
        if(coverageTermDef.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, coverageTermDef.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "CoverageTermDef{" +
            "id=" + id +
            ", description='" + description + "'" +
            ", beginEffectiveDate='" + beginEffectiveDate + "'" +
            ", endEffectiveDate='" + endEffectiveDate + "'" +
            ", state='" + state + "'" +
            '}';
    }
}
