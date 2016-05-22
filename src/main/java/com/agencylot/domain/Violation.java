package com.agencylot.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A Violation.
 */
@Entity
@Table(name = "violation")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "violation")
public class Violation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "external_violation_code")
    private String externalViolationCode;

    @Column(name = "description")
    private String description;

    @Column(name = "violation_occurred_date")
    private LocalDate violationOccurredDate;

    @Column(name = "additional_details")
    private String additionalDetails;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getExternalViolationCode() {
        return externalViolationCode;
    }

    public void setExternalViolationCode(String externalViolationCode) {
        this.externalViolationCode = externalViolationCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getViolationOccurredDate() {
        return violationOccurredDate;
    }

    public void setViolationOccurredDate(LocalDate violationOccurredDate) {
        this.violationOccurredDate = violationOccurredDate;
    }

    public String getAdditionalDetails() {
        return additionalDetails;
    }

    public void setAdditionalDetails(String additionalDetails) {
        this.additionalDetails = additionalDetails;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Violation violation = (Violation) o;
        if(violation.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, violation.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Violation{" +
            "id=" + id +
            ", externalViolationCode='" + externalViolationCode + "'" +
            ", description='" + description + "'" +
            ", violationOccurredDate='" + violationOccurredDate + "'" +
            ", additionalDetails='" + additionalDetails + "'" +
            '}';
    }
}
