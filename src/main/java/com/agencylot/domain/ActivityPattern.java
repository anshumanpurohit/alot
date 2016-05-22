package com.agencylot.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A ActivityPattern.
 */
@Entity
@Table(name = "activity_pattern")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "activitypattern")
public class ActivityPattern implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "fixed_id")
    private String fixedId;

    @Column(name = "deleted")
    private Boolean deleted;

    @Column(name = "description")
    private String description;

    @Column(name = "escalation_days")
    private Integer escalationDays;

    @Column(name = "create_activity_for")
    private String createActivityFor;

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

    public Boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getEscalationDays() {
        return escalationDays;
    }

    public void setEscalationDays(Integer escalationDays) {
        this.escalationDays = escalationDays;
    }

    public String getCreateActivityFor() {
        return createActivityFor;
    }

    public void setCreateActivityFor(String createActivityFor) {
        this.createActivityFor = createActivityFor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ActivityPattern activityPattern = (ActivityPattern) o;
        if(activityPattern.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, activityPattern.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ActivityPattern{" +
            "id=" + id +
            ", fixedId='" + fixedId + "'" +
            ", deleted='" + deleted + "'" +
            ", description='" + description + "'" +
            ", escalationDays='" + escalationDays + "'" +
            ", createActivityFor='" + createActivityFor + "'" +
            '}';
    }
}
