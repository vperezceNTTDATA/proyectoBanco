package proyecto.banco.bancoDemo.banco.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.Date;
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Document(collection = "transactions")
public class Movimiento {
    @Id
    private ObjectId id;

    @DBRef
    private CuentaBancaria cuentaBancaria;
    private BigDecimal monto;
    private Date fecha;
}
