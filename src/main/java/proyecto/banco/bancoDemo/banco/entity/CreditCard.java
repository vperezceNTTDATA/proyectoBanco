package proyecto.banco.bancoDemo.banco.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Setter
@Document(collection = "creditCards")
public class CreditCard {
    @Id
    private ObjectId id;
    @Field("cardNumber")
    @Indexed(unique = true)
    private String cardNumber;
    @Field
    private String idCliente;
    @Field("availableBalance")
    private BigDecimal availableBalance;
    @Field("utilizedBalance")
    private BigDecimal utilizedBalance;
    @Field("interests")
    private BigDecimal interests;
    private Boolean isExpired;
    @Field("paymentDate")
    private LocalDateTime paymentDate;
    @Field("billingCycleEndDate")
    private LocalDateTime billingCycleEndDate;
    private LocalDateTime created;
    private LocalDateTime updated;

    public CreditCard(ObjectId id, String cardNumber, String idCliente, BigDecimal availableBalance) {
        this.id = id;
        this.cardNumber = cardNumber;
        this.idCliente = idCliente;
        this.availableBalance = availableBalance;
        this.utilizedBalance = BigDecimal.ZERO;
        this.created = LocalDateTime.now();

        this.isExpired = false;
        this.interests = BigDecimal.ONE;
        this.paymentDate = LocalDateTime.now().plusDays(20);
        this.billingCycleEndDate = LocalDateTime.now().plusDays(25);
    }




}
