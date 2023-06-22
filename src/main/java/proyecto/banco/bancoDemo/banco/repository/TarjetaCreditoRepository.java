package proyecto.banco.bancoDemo.banco.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import proyecto.banco.bancoDemo.banco.entity.TarjetaCredito;
import reactor.core.publisher.Flux;

public interface TarjetaCreditoRepository extends ReactiveMongoRepository<TarjetaCredito, ObjectId> {
    Flux<TarjetaCredito> findByNumero(String numero);

    @Query(value = "{'cliente.numero': ?0}")
    Flux<TarjetaCredito> findByIdCliente(String idCliente);
}
