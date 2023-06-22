package proyecto.banco.bancoDemo.banco.controller;


import io.reactivex.Observable;
import io.reactivex.Single;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import proyecto.banco.bancoDemo.banco.dto.AccountRequestDTO;
import proyecto.banco.bancoDemo.banco.dto.ClienteRequestDTO;
import proyecto.banco.bancoDemo.banco.entity.Cliente;
import proyecto.banco.bancoDemo.banco.service.ClientService;


@RestController
@RequestMapping("/client")
public class ClienteRestController {
    private static final Logger logger = LoggerFactory.getLogger(ClienteRestController.class);

    @Autowired
    private ClientService clientService;

    @RequestMapping(value ="/clients" , produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Observable<Cliente> getClients() {
        return clientService.getClients();
    }

    @PostMapping("/client")
    public Single<ResponseEntity<Cliente>> createClient(@RequestBody ClienteRequestDTO clienteRequestDTO) {
        logger.info("INI - createClient");

        return clientService.createClient(clienteRequestDTO)
                .map(response -> ResponseEntity.status(HttpStatus.CREATED).body(response))
                .onErrorResumeNext(throwable -> Single.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Cliente())));
    }

    @PostMapping("/client/account")
    public Single<ResponseEntity<String>> createClientAccount(@RequestBody AccountRequestDTO accountRequestDTO) {
        logger.info("INI - createClientAccount");

        return clientService.createClientAccount(accountRequestDTO)
                .map(response -> ResponseEntity.status(HttpStatus.CREATED).body(response.getObservacion()))
                .onErrorResumeNext(throwable -> Single.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("INTERNAL_SERVER_ERROR")));

    }


}
