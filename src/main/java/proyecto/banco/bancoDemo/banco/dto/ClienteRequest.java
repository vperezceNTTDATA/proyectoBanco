package proyecto.banco.bancoDemo.banco.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
@Setter
public class ClienteRequest {

    @NotBlank(message = "El campo 'numDocumento' no puede estar en blanco")
    @Size(max = 15, message = "El campo 'numDocumento' debe tener como máximo {15} caracteres")
    private String numDocumento;

    @NotBlank(message = "El campo 'nombre' no puede estar en blanco")
    private String nombre;

    @NotBlank(message = "El campo 'tipoCliente' no puede estar en blanco")
    private String tipoCliente;
}
