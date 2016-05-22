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
 * A Lead.
 */
@Entity
@Table(name = "lead")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "lead")
public class Lead implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "requested_time")
    private LocalDate requestedTime;

    @Column(name = "response_time")
    private LocalDate responseTime;

    @Column(name = "premimum_amount", precision=10, scale=2)
    private BigDecimal premimumAmount;

    @ManyToOne
    private Job job;

    @OneToOne
    @JoinColumn(unique = true)
    private Carrier carrier;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getRequestedTime() {
        return requestedTime;
    }

    public void setRequestedTime(LocalDate requestedTime) {
        this.requestedTime = requestedTime;
    }

    public LocalDate getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(LocalDate responseTime) {
        this.responseTime = responseTime;
    }

    public BigDecimal getPremimumAmount() {
        return premimumAmount;
    }

    public void setPremimumAmount(BigDecimal premimumAmount) {
        this.premimumAmount = premimumAmount;
    }

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    public Carrier getCarrier() {
        return carrier;
    }

    public void setCarrier(Carrier carrier) {
        this.carrier = carrier;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Lead lead = (Lead) o;
        if(lead.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, lead.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Lead{" +
            "id=" + id +
            ", requestedTime='" + requestedTime + "'" +
            ", responseTime='" + responseTime + "'" +
            ", premimumAmount='" + premimumAmount + "'" +
            '}';
    }
}
