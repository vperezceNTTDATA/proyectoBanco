package proyecto.banco.bancoDemo.banco.service;

import io.reactivex.Observable;
import io.reactivex.Single;
import proyecto.banco.bancoDemo.banco.dto.ClienteRequest;
import proyecto.banco.bancoDemo.banco.entity.Cliente;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ClientService {
    Observable<Cliente> getClients();
    Single<Cliente> createClient(ClienteRequest clienteRequestDTO);
    Single<Cliente> getClientByDocNum(String docNum);
    Mono<Cliente> findClientByNumDocument(String docNum);
}
