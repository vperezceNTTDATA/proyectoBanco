package proyecto.banco.bancoDemo.banco.service;

import io.reactivex.Observable;
import io.reactivex.Single;
import proyecto.banco.bancoDemo.banco.dto.ClienteRequest;
import proyecto.banco.bancoDemo.banco.entity.Cliente;

public interface ClientService {
    Observable<Cliente> getClients();
    Single<Cliente> createClient(ClienteRequest clienteRequestDTO);
    Single<Cliente> getClientByDocNum(String docNum);


}
