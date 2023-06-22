package proyecto.banco.bancoDemo.banco.service.impl;

import io.reactivex.Observable;
import io.reactivex.Single;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import proyecto.banco.bancoDemo.banco.dto.AccountRequestDTO;
import proyecto.banco.bancoDemo.banco.dto.ClienteRequestDTO;
import proyecto.banco.bancoDemo.banco.dto.ResponseDTO;
import proyecto.banco.bancoDemo.banco.entity.Movimiento;
import proyecto.banco.bancoDemo.banco.enums.TipoCliente;
import proyecto.banco.bancoDemo.banco.enums.TipoCuenta;
import proyecto.banco.bancoDemo.banco.entity.Cliente;
import proyecto.banco.bancoDemo.banco.entity.CuentaBancaria;
import proyecto.banco.bancoDemo.banco.repository.ClientRepository;
import proyecto.banco.bancoDemo.banco.repository.CuentaBancariaRepository;
import proyecto.banco.bancoDemo.banco.repository.MovimientosRepository;
import proyecto.banco.bancoDemo.banco.service.ClientService;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
        Movimiento movimiento = new Movimiento(new ObjectId(), cuentaBancaria.blockingGet(), monto, new Date());
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
    public Single<Cliente> createClient(ClienteRequestDTO clienteRequestDTO) {
        logger.info("save Cliente");

        if (validarClient(clienteRequestDTO)) {
            Cliente cliente = new Cliente();
            cliente.setId(new ObjectId());
            cliente.setNombre(clienteRequestDTO.getNombre());
            cliente.setNumDocumento(clienteRequestDTO.getNumDocumento());
            cliente.setTipoCliente(clienteRequestDTO.getTipoCliente());

            return Single.fromPublisher(clientRepository.save(cliente));
        } else {
            return Single.fromPublisher(Mono.empty());
        }
    }

    @Override
    public Single<ResponseDTO> createClientAccount(AccountRequestDTO accountRequestDTO) {

        if(validarClientAccount(accountRequestDTO)){
           return Single.just(new ResponseDTO(false, "Datos insuficientes"));
        }

        Single<Cliente> clienteSingle = Single.fromPublisher(clientRepository.findByNumDocumento(accountRequestDTO.getNumDocumento()));
        Observable<CuentaBancaria> cliente = Observable.fromPublisher(cuentaBancariaRepository.findByIdCliente(accountRequestDTO.getNumDocumento()));

        if (esClientePersonal(clienteSingle) && tieneCuentaBancaria(cliente)) {
            return Single.just(new ResponseDTO(false, "Un cliente personal solo puede tener una cuenta de ahorro, una cuenta corriente o cuentas a plazo fijo."));
        }

        if (esClienteEmpresarial(clienteSingle) && accountRequestDTO.getTipoCuenta() == TipoCuenta.AHORRO.name()) {
            return Single.just(new ResponseDTO(false, "Un cliente empresarial no puede tener una cuenta de ahorro."));
        }

        if (esClienteEmpresarial(clienteSingle) && accountRequestDTO.getTipoCuenta() == TipoCuenta.PLAZO_FIJO.name()) {
            return Single.just(new ResponseDTO(false, "Un cliente empresarial no puede tener una cuenta a plazo fijo."));
        }

        CuentaBancaria cuentaBancaria = new CuentaBancaria(new ObjectId(), accountRequestDTO.getNumProducto(), clienteSingle.blockingGet(), accountRequestDTO.getTipoCuenta(), accountRequestDTO.getSaldo());

        /* TEST */
        if(clienteSingle.blockingGet().getTipoCliente().name().equals(TipoCliente.EMPRESARIAL.name())){
            if(validarTitularFirmantes(accountRequestDTO)){
                cuentaBancaria.setTitulares(accountRequestDTO.getListTitulares());
                cuentaBancaria.setFirmantesAutorizados(accountRequestDTO.getFirmantesAut());
            }else{
                List<String> titular = new ArrayList<>();
                titular.add(accountRequestDTO.getNumDocumento());
                cuentaBancaria.setTitulares(titular);
            }
        }

        cuentaBancariaRepository.save(cuentaBancaria);
        return Single.just(new ResponseDTO(true, "Cuenta bancaria creada exitosamente."));
    }

    private boolean validarTitularFirmantes(AccountRequestDTO accountRequestDTO){
        return accountRequestDTO.getListTitulares().size() > 0 && accountRequestDTO.getFirmantesAut().size() > 0;
    }

    private boolean validarClientAccount(AccountRequestDTO accountRequestDTO){
        return accountRequestDTO.getNumDocumento().isEmpty() && accountRequestDTO.getNumProducto().isEmpty();
    }

    private boolean esClientePersonal(Single<Cliente> clienteSingle) {
        return clienteSingle.blockingGet().getTipoCliente().equals(TipoCliente.PERSONAL);
    }

    private boolean esClienteEmpresarial(Single<Cliente> clienteSingle) {
        return clienteSingle.blockingGet().getTipoCliente().equals(TipoCliente.EMPRESARIAL);
    }

    private boolean tieneCuentaBancaria(Observable<CuentaBancaria> cliente) {
        return cliente.count().blockingGet() > 0 ;
    }

    private boolean validarClient(ClienteRequestDTO cliente) {
        if(!cliente.getNumDocumento().isEmpty() && !cliente.getNombre().isEmpty()){
            //verificar numDOC en mongoDB
            return true;
        }
        return false;
    }
}