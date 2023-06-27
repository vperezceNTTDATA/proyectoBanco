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
    private BigDecimal monto;
    private String action;
    private LocalDateTime fecha;

    private LocalDateTime created;
    private LocalDateTime updated;

    public Movimiento(ObjectId id, String idProduct, BigDecimal monto, String action) {
        this.id = id;
        this.idProduct = idProduct;
        this.monto = monto;
        this.action = action;
        this.fecha = LocalDateTime.now();
        this.created = LocalDateTime.now();
    }
}
