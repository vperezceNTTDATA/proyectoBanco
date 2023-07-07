package proyecto.banco.bancoDemo.banco.entity;

import proyecto.banco.bancoDemo.banco.enums.TipoCuenta;

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
import java.util.List;


@NoArgsConstructor
@Setter
@Getter
@Document(collection = "bankAccounts")
public class BankAccount {
  @Id
  private String id;
  @Field("numero")
  @Indexed(unique = true)
  private String numero;
  @Field
  private String idCliente;
  @Field("tipoCuenta")
  private TipoCuenta tipoCuenta;
  @Field("movimientosMensuales")
  private int movimientosMensuales;
  private int movimientosActuales;
  @Field("saldo")
  private BigDecimal saldo;
  private List<String> titulares;
  private List<String> firmantesAutorizados;
  private LocalDateTime created;
  private LocalDateTime updated;

  public BankAccount(String numero, String idCliente, String tipoCuenta, BigDecimal saldo) {
    this.numero = numero;
    this.idCliente = idCliente;

    if(tipoCuenta.equals(TipoCuenta.AHORRO.name())){
      this.tipoCuenta = TipoCuenta.AHORRO;
    }else if(tipoCuenta.equals(TipoCuenta.CUENTA_CORRIENTE.name())){
      this.tipoCuenta = TipoCuenta.CUENTA_CORRIENTE;
    }else{
      this.tipoCuenta = TipoCuenta.PLAZO_FIJO;
    }

    this.movimientosMensuales = 20;
    this.movimientosActuales = 0;
    this.saldo = saldo;
    this.created = LocalDateTime.now();
  }

}
