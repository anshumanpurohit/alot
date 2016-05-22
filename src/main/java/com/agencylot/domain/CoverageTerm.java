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
 * A CoverageTerm.
 */
@Entity
@Table(name = "coverage_term")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "coverageterm")
public class CoverageTerm implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "fixed_id")
    private String fixedId;

    @ManyToOne
    private Coverage coverage;

    @OneToOne
    @JoinColumn(unique = true)
    private CoverageTermDef definition;

    @OneToMany(mappedBy = "coverageTerm")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<CoverageTermOption> termOptions = new HashSet<>();

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

    public Coverage getCoverage() {
        return coverage;
    }

    public void setCoverage(Coverage coverage) {
        this.coverage = coverage;
    }

    public CoverageTermDef getDefinition() {
        return definition;
    }

    public void setDefinition(CoverageTermDef coverageTermDef) {
        this.definition = coverageTermDef;
    }

    public Set<CoverageTermOption> getTermOptions() {
        return termOptions;
    }

    public void setTermOptions(Set<CoverageTermOption> coverageTermOptions) {
        this.termOptions = coverageTermOptions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CoverageTerm coverageTerm = (CoverageTerm) o;
        if(coverageTerm.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, coverageTerm.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "CoverageTerm{" +
            "id=" + id +
            ", fixedId='" + fixedId + "'" +
            '}';
    }
}
