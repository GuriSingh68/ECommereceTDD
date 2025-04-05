package EGEN5203.EcommerceTDD.model;

import EGEN5203.EcommerceTDD.enums.PaymentStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "payments") public class Payments {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "order_id", nullable = false)
    @JsonIgnore
    private Order order;

    @Column(name = "payment_method")
    private String paymentMethod;

    @Column(name = "card_last_four")
    private String cardLastFour;

    @Column(name = "amount", nullable = false)
    private Double amount;

    @Column(name = "payment_date")
    private LocalDateTime paymentDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status")
    private PaymentStatus status;
}
