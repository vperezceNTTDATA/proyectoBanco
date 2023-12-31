package proyecto.banco.bancoDemo.banco.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import proyecto.banco.bancoDemo.banco.entity.DebitCard;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface DebitCardRepository extends ReactiveMongoRepository<DebitCard, ObjectId> {
    Mono<DebitCard> findByCardNumber(String cardNumber);
    @Query(value = "{'bankAccounts._id': ?0}")
    Flux<DebitCard> findByCuentaBancariaNumero(ObjectId idBankAccount);

}
