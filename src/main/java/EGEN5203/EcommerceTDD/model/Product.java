package EGEN5203.EcommerceTDD.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * Entity representing a product in the e-commerce application.
 * Maps to the "Product" table in the database and contains product details.
 */
@Entity
@ToString(exclude = {"users"})
@Table(name = "Product")
@Data
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long productId;

    @Column(name = "name", nullable = false)
    @NotBlank(message = "Product name cannot be blank")
    private String name;

    @Column(name = "description", length = 1000)
    private String description;

    @Column(name = "price", nullable = false)
    private Double price;

    @Column(name = "stock", nullable = false)
    @Min(value = 0, message = "Stock cannot be negative")
    private Integer stock;

    @Column(name = "category", nullable = false)
    private String category;

    @Column(name = "image_url")
    private String imageUrl;

    @ElementCollection // To store a list of tags
    @CollectionTable(name = "product_tags", joinColumns = @JoinColumn(name = "product_id"))
    @Column(name = "tag")
    private List<String> tags;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Users users;

    /**
     * Checks if the product is in stock.
     *
     * @return true if the stock is greater than zero, false otherwise.
     */
    public boolean isInStock() {
        return this.stock > 0; // Ensure this uses the correct field
    }
}