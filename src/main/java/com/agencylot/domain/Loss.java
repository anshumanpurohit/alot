package com.agencylot.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A Loss.
 */
@Entity
@Table(name = "loss")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "loss")
public class Loss implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "fixed_id")
    private String fixedId;

    @Column(name = "external_ref_id")
    private String externalRefId;

    @Column(name = "description")
    private String description;

    @Column(name = "loss_occurred_date")
    private LocalDate lossOccurredDate;

    @Column(name = "claim_amount", precision=10, scale=2)
    private BigDecimal claimAmount;

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

    public String getExternalRefId() {
        return externalRefId;
    }

    public void setExternalRefId(String externalRefId) {
        this.externalRefId = externalRefId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getLossOccurredDate() {
        return lossOccurredDate;
    }

    public void setLossOccurredDate(LocalDate lossOccurredDate) {
        this.lossOccurredDate = lossOccurredDate;
    }

    public BigDecimal getClaimAmount() {
        return claimAmount;
    }

    public void setClaimAmount(BigDecimal claimAmount) {
        this.claimAmount = claimAmount;
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
        Loss loss = (Loss) o;
        if(loss.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, loss.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Loss{" +
            "id=" + id +
            ", fixedId='" + fixedId + "'" +
            ", externalRefId='" + externalRefId + "'" +
            ", description='" + description + "'" +
            ", lossOccurredDate='" + lossOccurredDate + "'" +
            ", claimAmount='" + claimAmount + "'" +
            '}';
    }
}
