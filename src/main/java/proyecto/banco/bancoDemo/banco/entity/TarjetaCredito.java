package proyecto.banco.bancoDemo.banco.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Setter
@Document(collection = "creditCards")
public class TarjetaCredito {
    @Id
    private ObjectId id;
    @Field("numero")
    @Indexed(unique = true)
    private String numero;
    @Field
    private String idCliente;
    @Field("limiteCredito")
    private BigDecimal limiteCredito;
    @Field("saldo")
    private BigDecimal saldo;

    private LocalDateTime created;
    private LocalDateTime updated;

    public TarjetaCredito(ObjectId id, String numero, String idCliente, BigDecimal limiteCredito) {
        this.id = id;
        this.numero = numero;
        this.idCliente = idCliente;
        this.limiteCredito = limiteCredito;
        this.saldo = BigDecimal.ZERO;
        this.created = LocalDateTime.now();
    }
}
