package proyecto.banco.bancoDemo.banco.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@Setter
@Document(collection = "tarjetaCredito")
public class TarjetaCredito {
    @Id
    private ObjectId id;
    @Field("numero")
    private String numero;
    @DBRef
    private Cliente cliente;
    @Field("limiteCredito")
    private BigDecimal limiteCredito;
    @Field("saldo")
    private BigDecimal saldo;

    public TarjetaCredito(ObjectId id, Cliente cliente, BigDecimal limiteCredito) {
        this.id = id;
        this.cliente = cliente;
        this.limiteCredito = limiteCredito;
        this.saldo = BigDecimal.ZERO;
    }

    public void cargarConsumo(BigDecimal monto) {
        saldo = saldo.add(monto);
    }
}
