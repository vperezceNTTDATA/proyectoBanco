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
import proyecto.banco.bancoDemo.banco.enums.TipoCuenta;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Setter
@Document(collection = "credits")
public class Credito {
    @Id
    private ObjectId id;
    @Field("numero")
    private String numero;
    @Field
    private String idCliente;
    @Field("tipoCredito")
    private TipoCredito tipoCredito;
    @Field("monto")
    private BigDecimal monto;
    @Field("saldo")
    private BigDecimal saldo;

    private LocalDateTime created;
    private LocalDateTime updated;

    public Credito(ObjectId id, String numero, String idCliente, String tipoCredito, BigDecimal monto, BigDecimal saldo) {
        this.id = id;
        this.numero = numero;
        this.idCliente = idCliente;
        this.monto = monto;
        this.saldo = saldo;
        if(tipoCredito.equals(TipoCredito.PERSONAL.name())){
            this.tipoCredito = TipoCredito.PERSONAL;
        }else if(tipoCredito.equals(TipoCredito.EMPRESARIAL.name())){
            this.tipoCredito = TipoCredito.EMPRESARIAL;
        }
        this.created = LocalDateTime.now();
    }

}
