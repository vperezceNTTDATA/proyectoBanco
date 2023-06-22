package proyecto.banco.bancoDemo.banco.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import proyecto.banco.bancoDemo.banco.enums.TipoCredito;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@Setter
@Document(collection = "credito")
public class Credito {
    @Id
    private ObjectId id;
    @Field("numero")
    private String numero;
    @DBRef
    private Cliente cliente;
    @Field("tipoCredito")
    private TipoCredito tipoCredito;
    @Field("monto")
    private BigDecimal monto;
    @Field("saldo")
    private BigDecimal saldo;

    public Credito(ObjectId id, Cliente cliente, TipoCredito tipoCredito, BigDecimal monto) {
        this.id = id;
        this.cliente = cliente;
        this.tipoCredito = tipoCredito;
        this.monto = monto;
        this.saldo = monto;
    }

    public void realizarPago(BigDecimal monto) {
        saldo = saldo.subtract(monto);
    }
}
