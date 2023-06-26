package proyecto.banco.bancoDemo.banco.service.impl;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import proyecto.banco.bancoDemo.banco.dto.ClienteRequest;
import proyecto.banco.bancoDemo.banco.dto.ResponseDTO;
import proyecto.banco.bancoDemo.banco.entity.Movimiento;
import proyecto.banco.bancoDemo.banco.entity.Cliente;
import proyecto.banco.bancoDemo.banco.entity.CuentaBancaria;
import proyecto.banco.bancoDemo.banco.repository.ClientRepository;
import proyecto.banco.bancoDemo.banco.repository.CuentaBancariaRepository;
import proyecto.banco.bancoDemo.banco.repository.MovimientosRepository;
import proyecto.banco.bancoDemo.banco.service.ClientService;
import proyecto.banco.bancoDemo.model.exepcion.ConflictException;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

@Service
public class ClientServiceImpl implements ClientService {
    private static final Logger logger = LoggerFactory.getLogger(ClientServiceImpl.class);
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private MovimientosRepository movimientosRepository;
    @Autowired
    private CuentaBancariaRepository cuentaBancariaRepository;

    public Observable<Cliente> getClients() {
        return Observable.fromPublisher(clientRepository.findAll());
    }

    public Single<Cliente> getClientByDocNum(String docNum) {
        return Single.fromPublisher(clientRepository.findByNumDocumento(docNum));
    }
    @Override
    public Single<ResponseDTO> realizarDeposito(Single<Cliente> cliente, String numCuenta, BigDecimal monto) {
        Single<CuentaBancaria> cuentaBancaria = Single.fromPublisher(cuentaBancariaRepository.findByNumero(numCuenta))
                .map(cuenta -> {
                    cuenta.setSaldo(cuenta.getSaldo().add(monto));
                 return cuenta;
                });
        cuentaBancariaRepository.save(cuentaBancaria.blockingGet()).subscribe();
        Movimiento movimiento = new Movimiento();
        movimientosRepository.save(movimiento);

        return Single.just(new ResponseDTO(true, "Cuenta bancaria creada exitosamente."));
    }
    @Override
    public Single<ResponseDTO> realizarRetiro(Single<Cliente> cliente, String numCuenta, BigDecimal monto) {
        return null;
    }
    @Override
    public Single<ResponseDTO> realizarPagoCredito(Single<Cliente> cliente, String numCuenta, BigDecimal monto) {
        return null;
    }
    @Override
    public Single<ResponseDTO> cargarConsumoTarjetaCredito(Single<Cliente> cliente, String numTarjeta, BigDecimal monto) {
        return null;
    }

    @Override
    public Single<Cliente> createClient(ClienteRequest clienteRequestDTO) {
        logger.info("INI - createClient - ServiceIMPL");
            Cliente cliente = new Cliente(new ObjectId(), clienteRequestDTO.getNumDocumento(), clienteRequestDTO.getNombre());
            cliente.setTipoCliente(clienteRequestDTO.getTipoCliente());
            cliente.setCreated(LocalDateTime.now());

        return Single.fromPublisher(this.verificarClienteExiste(clienteRequestDTO.getNumDocumento())
                                    .then(this.clientRepository.save(cliente)));
    }

    private Mono<Void> verificarClienteExiste(String docNumero){
        return this.clientRepository.findByNumDocumento(docNumero)
                .flatMap(productEntity -> Mono.error(
                        new ConflictException("Client docNumero already exists : " + docNumero)
                ));
    }
}