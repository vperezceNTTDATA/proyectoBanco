package proyecto.banco.bancoDemo.banco.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import proyecto.banco.bancoDemo.banco.entity.CuentaBancaria;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CuentaBancariaRepository extends ReactiveMongoRepository<CuentaBancaria, ObjectId> {
    Mono<CuentaBancaria> findByNumero(String numero);

    @Query(value = "{'cliente.numero': ?0}")
    Flux<CuentaBancaria> findByIdCliente(String idCliente);
}
