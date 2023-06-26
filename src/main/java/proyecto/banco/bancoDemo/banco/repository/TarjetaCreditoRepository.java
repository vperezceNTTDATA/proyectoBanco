package proyecto.banco.bancoDemo.banco.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import proyecto.banco.bancoDemo.banco.entity.CuentaBancaria;
import proyecto.banco.bancoDemo.banco.entity.TarjetaCredito;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TarjetaCreditoRepository extends ReactiveMongoRepository<TarjetaCredito, ObjectId> {
    Mono<TarjetaCredito> findByNumero(String numero);

    @Query("{'idCliente': ?0 }")
    Flux<TarjetaCredito> findByIdCliente(String idCliente);

}
