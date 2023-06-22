package proyecto.banco.bancoDemo.banco.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;


@Getter
@NoArgsConstructor
@Setter
@AllArgsConstructor
public class AccountSaldoDTO {
    private String tipoProducto;
    private BigDecimal saldo;
    private String numProducto;
}
