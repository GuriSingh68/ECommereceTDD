package EGEN5203.EcommerceTDD.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "order_items")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    @JsonIgnore
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "item_price", nullable = false)
    private Double itemPrice;

    @Column(name = "total_price", nullable = false)
    private Double totalPrice;

    // If you want to add a derived user field
    @Transient
    private Users user;

    // Method to get user from the associated order
    public Users getUser() {
        return this.order != null ? this.order.getUser() : null;
    }
}