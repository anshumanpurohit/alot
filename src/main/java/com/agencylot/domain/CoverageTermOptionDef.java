package com.agencylot.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A CoverageTermOptionDef.
 */
@Entity
@Table(name = "coverage_term_option_def")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "coveragetermoptiondef")
public class CoverageTermOptionDef implements Serializable {

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
    private CoverageTermDef coverageTermDef;

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

    public CoverageTermDef getCoverageTermDef() {
        return coverageTermDef;
    }

    public void setCoverageTermDef(CoverageTermDef coverageTermDef) {
        this.coverageTermDef = coverageTermDef;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CoverageTermOptionDef coverageTermOptionDef = (CoverageTermOptionDef) o;
        if(coverageTermOptionDef.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, coverageTermOptionDef.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "CoverageTermOptionDef{" +
            "id=" + id +
            ", description='" + description + "'" +
            ", beginEffectiveDate='" + beginEffectiveDate + "'" +
            ", endEffectiveDate='" + endEffectiveDate + "'" +
            ", state='" + state + "'" +
            '}';
    }
}
