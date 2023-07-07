package proyecto.banco.bancoDemo.banco.service.impl;
import io.reactivex.Observable;
import io.reactivex.Single;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import proyecto.banco.bancoDemo.banco.dto.ClienteRequest;
import proyecto.banco.bancoDemo.banco.entity.Cliente;
import proyecto.banco.bancoDemo.banco.repository.ClientRepository;
import proyecto.banco.bancoDemo.banco.repository.BankAccountRepository;
import proyecto.banco.bancoDemo.banco.service.ClientService;
import proyecto.banco.bancoDemo.model.exepcion.ConflictException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
public class ClientServiceImpl implements ClientService {
  private static final Logger logger = LoggerFactory.getLogger(ClientServiceImpl.class);
  @Autowired
  private ClientRepository clientRepository;
  @Autowired
  private BankAccountRepository bankAccountRepository;

  public Observable<Cliente> getClients() {
    return Observable.fromPublisher(clientRepository.findAll());
  }

  public Single<Cliente> getClientByDocNum(String docNum) {
    return Single.fromPublisher(clientRepository.findByNumDocumento(docNum));
  }

  @Override
  public Mono<Cliente> findClientByNumDocument(String docNum) {
    return clientRepository.findByNumDocumento(docNum);
  }

  @Override
  public Single<Cliente> createClient(ClienteRequest clienteRequestDTO) {
    logger.info("INI - createClient - ServiceIMPL");
    Cliente cliente = new Cliente(clienteRequestDTO.getNumDocumento(), clienteRequestDTO.getNombre());
    cliente.setTipoCliente(clienteRequestDTO.getTipoCliente());
    cliente.setCreated(LocalDateTime.now());

    return Single.fromPublisher(this.clientRepository.findByNumDocumento(clienteRequestDTO.getNumDocumento())
        .map(productEntity -> Mono.error(
            new ConflictException("Client docNumero already exists : " + clienteRequestDTO.getNumDocumento())
        )).then(this.clientRepository.save(cliente)));
  }
}