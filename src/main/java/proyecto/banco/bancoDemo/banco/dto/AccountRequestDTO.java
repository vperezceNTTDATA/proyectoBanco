package proyecto.banco.bancoDemo.banco.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@NoArgsConstructor
@Setter
public class AccountRequestDTO {
    private String idCliente;
    private String numDocumento;
    private BigDecimal saldo;
    private String tipoCuenta;
    private String numProducto;

    private List<String> listTitulares;
    private List<String> firmantesAut;
}
