package proyecto.banco.bancoDemo.banco.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import proyecto.banco.bancoDemo.banco.entity.BankAccount;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BankAccountRepository extends ReactiveMongoRepository<BankAccount, ObjectId> {
    Mono<BankAccount> findByNumero(String numero);
    Mono<BankAccount> findByNumeroAndIdCliente(String numero, String idCliente);
    @Query("{'idCliente': ?0 }")
    Flux<BankAccount> findByIdCliente(String idCliente);

}
