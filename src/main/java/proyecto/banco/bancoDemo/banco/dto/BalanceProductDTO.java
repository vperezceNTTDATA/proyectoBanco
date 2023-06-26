package proyecto.banco.bancoDemo.banco.dto;
import lombok.*;
import java.math.BigDecimal;

@Data
public class BalanceProductDTO {
    private String nameProducto;
    private String tipoProducto;
    private BigDecimal saldo;
    private String numero;
    private BigDecimal limiteCredito;

    public BalanceProductDTO(String nameProducto, String tipoProducto, BigDecimal saldo, String numero) {
        this.nameProducto = nameProducto;
        this.tipoProducto = tipoProducto;
        this.saldo = saldo;
        this.numero = numero;
    }

    public BalanceProductDTO(String nameProducto, String tipoProducto, BigDecimal saldo, String numero, BigDecimal limiteCredito) {
        this.nameProducto = nameProducto;
        this.tipoProducto = tipoProducto;
        this.saldo = saldo;
        this.numero = numero;
        this.limiteCredito = limiteCredito;
    }
}
