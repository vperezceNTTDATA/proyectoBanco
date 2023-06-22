package proyecto.banco.bancoDemo.banco.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Document(collection = "movimiento")
public class Movimiento {
    @Id
    private String id;

    @DBRef
    private CuentaBancaria cuentaBancaria;
    private BigDecimal monto;
    private LocalDateTime fecha;
}
