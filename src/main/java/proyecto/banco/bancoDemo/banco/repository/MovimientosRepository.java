package proyecto.banco.bancoDemo.banco.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import proyecto.banco.bancoDemo.banco.entity.Cliente;
import proyecto.banco.bancoDemo.banco.entity.Movimiento;
import reactor.core.publisher.Flux;

public interface MovimientosRepository extends ReactiveMongoRepository<Movimiento, ObjectId> {
    Flux<Cliente> findByNumDocumento(String docNum);

    @Query(value = "{'cuentaBancaria.numero': ?0}")
    Flux<Movimiento> findByCuentaBancariaNumero(String cuentaNum);


}
