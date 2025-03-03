package EGEN5203.EcommerceTDD.model;

import jakarta.persistence.*;
import lombok.Data;
@Entity
@Table(name = "Product")
@Data public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")  // Defines the column name in the database
    private Long id;

    @Column(name = "product_name", nullable = false)  // Defines the column name and a constraint
    private String productName;
    @Column(name = "quantity", nullable = false)  // Defines the column quantity and a constraint
    private Integer quantity;
    @Column(name = "price", nullable = false)  // Defines the column price and a constraint
    private Double price;

}
