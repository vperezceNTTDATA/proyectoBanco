package proyecto.banco.bancoDemo.banco.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import proyecto.banco.bancoDemo.banco.entity.Credito;

public interface CreditoRepository extends ReactiveMongoRepository<Credito, ObjectId> {
}
