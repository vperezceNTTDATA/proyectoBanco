package proyecto.banco.bancoDemo.banco.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import proyecto.banco.bancoDemo.banco.enums.TipoCliente;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


@Getter
@NoArgsConstructor
@Setter
@Document(collection = "cliente")
public class Cliente {
    @Id
    private ObjectId id;
    @Field("nombre")
    private String nombre;
    @Field("numDocumento")
    private String numDocumento;
    @Field("tipoCliente")
    private TipoCliente tipoCliente;
    @Field("cuentasBancarias")
    private List<CuentaBancaria> cuentasBancarias;
    @Field("creditos")
    private List<Credito> creditos;
    @Field("tarjetasCredito")
    private List<TarjetaCredito> tarjetasCredito;

    public Cliente(ObjectId id) {
        this.id = id;
        this.cuentasBancarias = new ArrayList<>();
        this.creditos = new ArrayList<>();
        this.tarjetasCredito = new ArrayList<>();
    }

    public Cliente(ObjectId id, String numDocumento, String nombre) {
        this.id = id;
        this.numDocumento = numDocumento;
        this.nombre = nombre;
        this.cuentasBancarias = new ArrayList<>();
        this.creditos = new ArrayList<>();
        this.tarjetasCredito = new ArrayList<>();
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
