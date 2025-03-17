package EGEN5203.EcommerceTDD.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Entity
@Table(name = "cart")
@Data
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_id")  // Defines the column name in the database
    private Long cart_id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private Users user; // Reference to User entity

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    @JsonIgnore
    private Product product; // Reference to Product entity

    @Column(name = "product_name", nullable = false)  // Defines the column name and a constraint
    private String productName;

    @Column(name = "quantity", nullable = false)  // Defines the column quantity and a constraint
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;

    @Column(name = "price", nullable = false)  // Defines the column price and a constraint
    private Double price;
    // Calculate total price for this cart item
    public Double calculateItemTotal() {
        return  price*quantity;
    }
}
