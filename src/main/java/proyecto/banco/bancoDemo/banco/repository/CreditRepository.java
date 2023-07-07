package proyecto.banco.bancoDemo.banco.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import proyecto.banco.bancoDemo.banco.entity.Credit;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CreditRepository extends ReactiveMongoRepository<Credit, ObjectId> {
    @Query("{'idCliente': ?0 }")
    Flux<Credit> findByIdClient(String clienteId);
    Mono<Credit> findByNumero(String numero);
    Mono<Credit> findByNumeroAndIdCliente(String numero, String idCliente);
}
