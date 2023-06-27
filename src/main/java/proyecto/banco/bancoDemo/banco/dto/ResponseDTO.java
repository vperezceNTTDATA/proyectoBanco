package proyecto.banco.bancoDemo.banco.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class ResponseDTO {
    boolean isValid;
    String description;
}
