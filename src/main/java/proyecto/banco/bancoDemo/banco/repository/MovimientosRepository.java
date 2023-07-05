package proyecto.banco.bancoDemo.banco.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import proyecto.banco.bancoDemo.banco.entity.Movimiento;
import reactor.core.publisher.Flux;

public interface MovimientosRepository extends ReactiveMongoRepository<Movimiento, ObjectId> {
    @Query(value = "{'idProduct': ?0}")
    Flux<Movimiento> findByCuentaBancariaNumero(String idProducto);
    @Query(value = "{'idProduct': ?0}")
    Flux<Movimiento> findTop10ByOrderByCreatedDesc(String idProducto);
}
