package com.agencylot.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Discount.
 */
@Entity
@Table(name = "discount")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "discount")
public class Discount implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "amount")
    private Integer amount;

    @ManyToOne
    private ProductLine productLine;

    @OneToOne
    @JoinColumn(unique = true)
    private DiscountDef definition;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public ProductLine getProductLine() {
        return productLine;
    }

    public void setProductLine(ProductLine productLine) {
        this.productLine = productLine;
    }

    public DiscountDef getDefinition() {
        return definition;
    }

    public void setDefinition(DiscountDef discountDef) {
        this.definition = discountDef;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Discount discount = (Discount) o;
        if(discount.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, discount.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Discount{" +
            "id=" + id +
            ", amount='" + amount + "'" +
            '}';
    }
}
