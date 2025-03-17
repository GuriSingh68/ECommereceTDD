package EGEN5203.EcommerceTDD.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
@Entity
@Table(name = "Product")
@Data public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")  // Defines the column name in the database
    private Long product_id;

    @Column(name = "product_name", nullable = false)// Defines the column name and a constraint
    @NotBlank(message = "Product name cannot be blank")
    private String productName;

    @Column(name = "description", length = 1000)
    private String description;

    @Column(name = "category")
    private String category;
    @Column(name = "quantity", nullable = false)  // Defines the column quantity and a constraint
    @Min(value = 0, message = "Quantity cannot be negative")
    private Integer quantity;
    @Column(name = "price", nullable = false)  // Defines the column price and a constraint
    private Double price;
    // Check if product is in stock
    public boolean isInStock() {
        return this.quantity > 0;
    }
}
