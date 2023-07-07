package proyecto.banco.bancoDemo.banco.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import proyecto.banco.bancoDemo.banco.enums.TipoCliente;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Setter
@Document(collection = "clients")
public class Cliente {
    @Id
    private String id;
    @Field("nombre")
    private String nombre;
    @Field("numDocumento")
    @Indexed(unique = true)
    private String numDocumento;
    @Field("tipoCliente")
    private TipoCliente tipoCliente;

    private LocalDateTime created;
    private LocalDateTime updated;

    public Cliente(String numDocumento, String nombre) {
        this.numDocumento = numDocumento;
        this.nombre = nombre;
    }

    public TipoCliente getTipoCliente() {
        return tipoCliente;
    }

    public void setTipoCliente(String tipoCliente) {
        if(tipoCliente.equals(TipoCliente.PERSONAL.name())){
            this.tipoCliente = TipoCliente.PERSONAL;
        }else{
           this.tipoCliente = TipoCliente.EMPRESARIAL;
        }
    }
}
