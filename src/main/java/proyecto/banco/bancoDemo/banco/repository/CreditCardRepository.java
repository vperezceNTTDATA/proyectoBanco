package proyecto.banco.bancoDemo.banco.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import proyecto.banco.bancoDemo.banco.entity.CreditCard;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CreditCardRepository extends ReactiveMongoRepository<CreditCard, ObjectId> {
    Mono<CreditCard> findByCardNumber(String numero);
    @Query("{'idCliente': ?0 }")
    Flux<CreditCard> findByIdCliente(String idCliente);

}
