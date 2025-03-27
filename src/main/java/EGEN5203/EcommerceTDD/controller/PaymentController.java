package EGEN5203.EcommerceTDD.controller;

import EGEN5203.EcommerceTDD.dto.UpdatePaymentStatusDto;
import EGEN5203.EcommerceTDD.enums.PaymentStatus;
import EGEN5203.EcommerceTDD.model.Payments;
import EGEN5203.EcommerceTDD.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/payments")
public class PaymentController {
    @Autowired
    PaymentService paymentService;
    @GetMapping("")
    public ResponseEntity<List<Payments>> getAllPaymentDetails(){
        List<Payments> payments=paymentService.fetchAllDetails();
        return ResponseEntity.status(HttpStatus.OK).body(payments);
    }
    @GetMapping("/users")
    public ResponseEntity<Payments> getPayments(@RequestParam Integer paymentId,@RequestParam Long userId){
        Payments payments=paymentService.getPaymentsById(paymentId,userId);
        return ResponseEntity.ok(payments);
    }
    @PatchMapping("/paymentStatus")
    public ResponseEntity<Payments> updateStatus(@RequestParam Integer paymentId,
                                                 @RequestParam Long userId,
                                                 @RequestBody UpdatePaymentStatusDto status){
        Payments payments=paymentService.updatePaymentStatus(paymentId,userId,status);
        return ResponseEntity.ok(payments);
    }
}
