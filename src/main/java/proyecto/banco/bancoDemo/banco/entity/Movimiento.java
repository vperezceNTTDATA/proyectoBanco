package proyecto.banco.bancoDemo.banco.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Setter
@Document(collection = "transactions")
public class Movimiento {
    @Id
    private ObjectId id;
    @Field
    private String idProduct;
    private String idProductReceive;
    private String numberCreditCard;
    private BigDecimal monto;
    private String action;
    private LocalDateTime fecha;
    private Boolean withCommission;
    private LocalDateTime created;
    private LocalDateTime updated;

    public Movimiento(ObjectId id, String idProduct, BigDecimal monto, String action) {
        this.id = id;
        this.idProduct = idProduct;
        this.monto = monto;
        this.action = action;
        this.fecha = LocalDateTime.now();
        this.created = LocalDateTime.now();
        this.withCommission = false;
    }
    public Movimiento(ObjectId id, String idProduct, BigDecimal newMonto, String action, BigDecimal monto, String numberCreditCard) {
        this.id = id;
        this.idProduct = idProduct;
        this.monto = newMonto;
        this.withCommission = newMonto.compareTo(monto) != 0;
        this.action = action;
        this.numberCreditCard = numberCreditCard;

        this.fecha = LocalDateTime.now();
        this.created = LocalDateTime.now();
    }
    public Movimiento(ObjectId id, String idProduct, BigDecimal monto, String action, String idProductReceive) {
        this.id = id;
        this.idProduct = idProduct;
        this.monto = monto;
        this.idProductReceive = idProductReceive;
        this.action = action;
        this.fecha = LocalDateTime.now();
        this.created = LocalDateTime.now();
        this.withCommission = false;
    }
}
