package proyecto.banco.bancoDemo.banco.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import proyecto.banco.bancoDemo.banco.entity.Cliente;
import proyecto.banco.bancoDemo.banco.entity.Credito;
import reactor.core.publisher.Flux;

public interface CreditoRepository extends ReactiveMongoRepository<Credito, ObjectId> {
    @Query("{'idCliente': ?0 }")
    Flux<Credito> findByIdClient(String clienteId);
}
