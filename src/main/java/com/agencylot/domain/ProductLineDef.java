package com.agencylot.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

import com.agencylot.domain.enumeration.ProductLineType;

/**
 * A ProductLineDef.
 */
@Entity
@Table(name = "product_line_def")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "productlinedef")
public class ProductLineDef implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "product_line_type")
    private ProductLineType productLineType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ProductLineType getProductLineType() {
        return productLineType;
    }

    public void setProductLineType(ProductLineType productLineType) {
        this.productLineType = productLineType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ProductLineDef productLineDef = (ProductLineDef) o;
        if(productLineDef.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, productLineDef.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ProductLineDef{" +
            "id=" + id +
            ", productLineType='" + productLineType + "'" +
            '}';
    }
}
