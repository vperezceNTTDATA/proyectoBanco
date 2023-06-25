package proyecto.banco.bancoDemo.banco.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import proyecto.banco.bancoDemo.banco.enums.TipoCuenta;

import java.math.BigDecimal;
import java.util.List;


@Getter
@NoArgsConstructor
@Setter
@Document(collection = "bankAccounts")
public class CuentaBancaria {
    @Id
    private ObjectId id;
    @Field("numero")
    private String numero;
    @DBRef
    private Cliente cliente;
    @Field("tipoCuenta")
    private TipoCuenta tipoCuenta;
    @Field("movimientosMensuales")
    private int movimientosMensuales;
    @Field("saldo")
    private BigDecimal saldo;

    private List<String> titulares;
    private List<String> firmantesAutorizados;

    public CuentaBancaria(ObjectId id, String numero, Cliente cliente, TipoCuenta tipoCuenta, int movimientosMensuales, BigDecimal saldo) {
        this.id = id;
        this.numero = numero;
        this.cliente = cliente;
        this.tipoCuenta = tipoCuenta;
        this.movimientosMensuales = movimientosMensuales;
        this.saldo = saldo;
    }

    public CuentaBancaria(ObjectId id, String numero, Cliente cliente, String tipoCuenta, BigDecimal saldo) {
        this.id = id;
        this.numero = numero;
        this.cliente = cliente;

        if(tipoCuenta.equals(TipoCuenta.AHORRO.name())){
            this.tipoCuenta = TipoCuenta.AHORRO;
        }else if(tipoCuenta.equals(TipoCuenta.CUENTA_CORRIENTE.name())){
            this.tipoCuenta = TipoCuenta.CUENTA_CORRIENTE;
        }else{
            this.tipoCuenta = TipoCuenta.PLAZO_FIJO;
        }

        this.movimientosMensuales = 0;
        this.saldo = saldo;
    }

}
