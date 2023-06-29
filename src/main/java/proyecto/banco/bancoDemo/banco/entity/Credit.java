package proyecto.banco.bancoDemo.banco.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import proyecto.banco.bancoDemo.banco.enums.CreditType;

import org.springframework.data.annotation.Id;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.index.Indexed;

import org.bson.types.ObjectId;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@Setter
@Document(collection = "credits")
public class Credit {
    @Id
    private ObjectId id;
    @Field("numero")
    @Indexed(unique = true)
    private String numero;
    @Field
    private String idCliente;
    @Field("tipoCredito")
    private CreditType tipoCredito;
    @Field("monto")
    private BigDecimal monto;
    @Field("montoPagado")
    private BigDecimal montoPagado;

    private LocalDateTime created;
    private LocalDateTime updated;

    public Credit(ObjectId id, String numero, String idCliente, String creditType, BigDecimal monto) {
        this.id = id;
        this.numero = numero;
        this.idCliente = idCliente;
        this.monto = monto;
        this.montoPagado = BigDecimal.ZERO;

        if(creditType.equals(CreditType.PERSONAL.name()))this.tipoCredito = CreditType.PERSONAL;
        else if(creditType.equals(CreditType.EMPRESARIAL.name()))this.tipoCredito = CreditType.EMPRESARIAL;
        else if(creditType.equals(CreditType.EMPRESARIAL_PYME.name()))this.tipoCredito = CreditType.EMPRESARIAL_PYME;
        else if(creditType.equals(CreditType.PERSONAL_VIP.name()))this.tipoCredito = CreditType.PERSONAL_VIP;

        this.created = LocalDateTime.now();
    }

}
