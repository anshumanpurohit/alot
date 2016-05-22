package com.agencylot.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

import com.agencylot.domain.enumeration.CommunicationPreference;

/**
 * A PolicyContact.
 */
@Entity
@Table(name = "policy_contact")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "policycontact")
public class PolicyContact implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "fixed_id")
    private String fixedId;

    @Column(name = "contact_reference_id")
    private String contactReferenceId;

    @Column(name = "deleted")
    private Boolean deleted;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "middle_initial")
    private String middleInitial;

    @Column(name = "dob")
    private LocalDate dob;

    @Column(name = "work_phone")
    private String workPhone;

    @Column(name = "home_phone")
    private String homePhone;

    @Column(name = "email_address")
    private String emailAddress;

    @Enumerated(EnumType.STRING)
    @Column(name = "communication_preference")
    private CommunicationPreference communicationPreference;

    @Column(name = "company_name")
    private String companyName;

    @Column(name = "external_reference_id")
    private String externalReferenceId;

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

    public String getContactReferenceId() {
        return contactReferenceId;
    }

    public void setContactReferenceId(String contactReferenceId) {
        this.contactReferenceId = contactReferenceId;
    }

    public Boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMiddleInitial() {
        return middleInitial;
    }

    public void setMiddleInitial(String middleInitial) {
        this.middleInitial = middleInitial;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public String getWorkPhone() {
        return workPhone;
    }

    public void setWorkPhone(String workPhone) {
        this.workPhone = workPhone;
    }

    public String getHomePhone() {
        return homePhone;
    }

    public void setHomePhone(String homePhone) {
        this.homePhone = homePhone;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public CommunicationPreference getCommunicationPreference() {
        return communicationPreference;
    }

    public void setCommunicationPreference(CommunicationPreference communicationPreference) {
        this.communicationPreference = communicationPreference;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getExternalReferenceId() {
        return externalReferenceId;
    }

    public void setExternalReferenceId(String externalReferenceId) {
        this.externalReferenceId = externalReferenceId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PolicyContact policyContact = (PolicyContact) o;
        if(policyContact.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, policyContact.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "PolicyContact{" +
            "id=" + id +
            ", fixedId='" + fixedId + "'" +
            ", contactReferenceId='" + contactReferenceId + "'" +
            ", deleted='" + deleted + "'" +
            ", firstName='" + firstName + "'" +
            ", lastName='" + lastName + "'" +
            ", middleInitial='" + middleInitial + "'" +
            ", dob='" + dob + "'" +
            ", workPhone='" + workPhone + "'" +
            ", homePhone='" + homePhone + "'" +
            ", emailAddress='" + emailAddress + "'" +
            ", communicationPreference='" + communicationPreference + "'" +
            ", companyName='" + companyName + "'" +
            ", externalReferenceId='" + externalReferenceId + "'" +
            '}';
    }
}
