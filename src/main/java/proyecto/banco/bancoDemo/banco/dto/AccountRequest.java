package proyecto.banco.bancoDemo.banco.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.util.List;

@Getter
@NoArgsConstructor
@Setter
public class AccountRequest {
  @NotBlank(message = "El campo 'numDocumento' no puede estar en blanco")
  private String numDocumento;
  @NotBlank(message = "El campo 'numProducto' no puede estar en blanco")
  private String numProducto;
  private String numberDebitCard;
  private String saldo;
  private String tipoCuenta;
  private String monto;
  private List<String> listTitulares;
  private List<String> firmantesAut;

  public BigDecimal getSaldo() {
    return new BigDecimal(saldo);
  }

  public BigDecimal getMonto() {
    return new BigDecimal(monto);
  }
}
