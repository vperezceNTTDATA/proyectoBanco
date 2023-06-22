package proyecto.banco.bancoDemo.banco.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@Setter
public class ClienteRequestDTO {
    private String numDocumento;
    private String nombre;
    private String tipoCliente;
}
