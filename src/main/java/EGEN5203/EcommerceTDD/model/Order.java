package EGEN5203.EcommerceTDD.model;

import EGEN5203.EcommerceTDD.enums.OrderStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;
    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
    private Payments payments;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems;

    @Column(name = "total_price", nullable = false)
    private Double totalPrice;

    @Column(name = "order_date", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_status")
    private OrderStatus status;
}
