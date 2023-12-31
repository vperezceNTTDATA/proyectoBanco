package proyecto.banco.bancoDemo.banco.controller;


import io.reactivex.Observable;
import io.reactivex.Single;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import proyecto.banco.bancoDemo.banco.dto.ClienteRequest;
import proyecto.banco.bancoDemo.banco.entity.Cliente;
import proyecto.banco.bancoDemo.banco.service.ClientService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@Validated
@RequestMapping(ClienteRestController.CLIENTS)
public class ClienteRestController {
    private static final Logger logger = LoggerFactory.getLogger(ClienteRestController.class);
    public static final String CLIENTS = "/api/clients";
    public static final String GET_CLIENT = "/client/{docNumber}";
    @Autowired
    private ClientService clientService;
    @RequestMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Observable<Cliente> getClients() {
        return clientService.getClients();
    }
    @PostMapping
    public Single<Cliente> createClient(@RequestBody @Valid ClienteRequest clienteRequest) {
        logger.info("INI - createClient");
        return clientService.createClient(clienteRequest);
    }
    @RequestMapping(value = ClienteRestController.GET_CLIENT)
    public Mono<Cliente> findClientByNumDocument(@PathVariable String docNumber) {
        logger.info("INI - findClientByNumDocument");
        return clientService.findClientByNumDocument(docNumber);
    }

}
