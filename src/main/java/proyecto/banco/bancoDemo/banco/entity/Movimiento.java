package proyecto.banco.bancoDemo.banco.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Document(collection = "transactions")
public class Movimiento {
    @Id
    private ObjectId id;

    @Field
    private String idProduct;
    private BigDecimal monto;
    private Date fecha;

    private LocalDateTime created;
    private LocalDateTime updated;
}
