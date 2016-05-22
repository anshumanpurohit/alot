package com.agencylot.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A CoverageTermOption.
 */
@Entity
@Table(name = "coverage_term_option")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "coveragetermoption")
public class CoverageTermOption implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "fixed_id")
    private String fixedId;

    @ManyToOne
    private CoverageTerm coverageTerm;

    @OneToOne
    @JoinColumn(unique = true)
    private CoverageTermOptionDef definition;

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

    public CoverageTerm getCoverageTerm() {
        return coverageTerm;
    }

    public void setCoverageTerm(CoverageTerm coverageTerm) {
        this.coverageTerm = coverageTerm;
    }

    public CoverageTermOptionDef getDefinition() {
        return definition;
    }

    public void setDefinition(CoverageTermOptionDef coverageTermOptionDef) {
        this.definition = coverageTermOptionDef;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CoverageTermOption coverageTermOption = (CoverageTermOption) o;
        if(coverageTermOption.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, coverageTermOption.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "CoverageTermOption{" +
            "id=" + id +
            ", fixedId='" + fixedId + "'" +
            '}';
    }
}
