package proyecto.banco.bancoDemo.banco.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import proyecto.banco.bancoDemo.banco.entity.Cliente;
import reactor.core.publisher.Mono;

public interface ClientRepository extends ReactiveMongoRepository<Cliente, ObjectId> {

    Mono<Cliente> findByNumDocumento(String docNum);
}
