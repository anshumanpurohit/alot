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
 * A CoverageDef.
 */
@Entity
@Table(name = "coverage_def")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "coveragedef")
public class CoverageDef implements Serializable {

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

    @OneToMany(mappedBy = "coverageDef")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<CoverageTermDef> coverageTermDefs = new HashSet<>();

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

    public Set<CoverageTermDef> getCoverageTermDefs() {
        return coverageTermDefs;
    }

    public void setCoverageTermDefs(Set<CoverageTermDef> coverageTermDefs) {
        this.coverageTermDefs = coverageTermDefs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CoverageDef coverageDef = (CoverageDef) o;
        if(coverageDef.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, coverageDef.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "CoverageDef{" +
            "id=" + id +
            ", description='" + description + "'" +
            ", beginEffectiveDate='" + beginEffectiveDate + "'" +
            ", endEffectiveDate='" + endEffectiveDate + "'" +
            ", state='" + state + "'" +
            '}';
    }
}
