package com.agencylot.domain;

import io.swagger.annotations.ApiModel;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * external mapping begin                                                      
 * 
 */
@ApiModel(description = ""
    + "external mapping begin                                                 "
    + "")
@Entity
@Table(name = "accord_mapping")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "accordmapping")
public class AccordMapping implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "input")
    private String input;

    @Column(name = "output")
    private String output;

    @Column(name = "accrod_version")
    private String accrodVersion;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public String getAccrodVersion() {
        return accrodVersion;
    }

    public void setAccrodVersion(String accrodVersion) {
        this.accrodVersion = accrodVersion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AccordMapping accordMapping = (AccordMapping) o;
        if(accordMapping.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, accordMapping.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "AccordMapping{" +
            "id=" + id +
            ", input='" + input + "'" +
            ", output='" + output + "'" +
            ", accrodVersion='" + accrodVersion + "'" +
            '}';
    }
}
