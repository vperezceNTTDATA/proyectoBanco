package proyecto.banco.bancoDemo.banco.service.impl;

import io.reactivex.Single;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import proyecto.banco.bancoDemo.banco.dto.AccountRequest;
import proyecto.banco.bancoDemo.banco.entity.Cliente;
import proyecto.banco.bancoDemo.banco.entity.Credito;
import proyecto.banco.bancoDemo.banco.entity.CuentaBancaria;
import proyecto.banco.bancoDemo.banco.entity.TarjetaCredito;
import proyecto.banco.bancoDemo.banco.enums.TipoCliente;
import proyecto.banco.bancoDemo.banco.enums.TipoCuenta;
import proyecto.banco.bancoDemo.banco.repository.ClientRepository;
import proyecto.banco.bancoDemo.banco.repository.CreditoRepository;
import proyecto.banco.bancoDemo.banco.repository.CuentaBancariaRepository;
import proyecto.banco.bancoDemo.banco.repository.TarjetaCreditoRepository;
import proyecto.banco.bancoDemo.banco.service.AccountService;
import proyecto.banco.bancoDemo.model.exepcion.ConflictException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class AccountServiceImpl implements AccountService {
    private static final Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private CuentaBancariaRepository cuentaBancariaRepository;
    @Autowired
    private CreditoRepository creditoRepository;
    @Autowired
    private TarjetaCreditoRepository tarjetaCreditoRepository;

    @Override
    public Single<CuentaBancaria> createClientAccount(AccountRequest accountRequest) {
        logger.info("INI - createClientAccount - ServiceIMPL");

        Single<Cliente> clienteSingle = Single.fromPublisher(
                clientRepository.findByNumDocumento(accountRequest.getNumDocumento())
                        .flatMap(cliente -> {
                            if(cliente.getTipoCliente().name().equals(TipoCliente.EMPRESARIAL.name())) {
                                if (accountRequest.getTipoCuenta().equals(TipoCuenta.AHORRO.name())) {
                                    return Mono.error(new ConflictException("Cliente empresarial: " + accountRequest.getNumDocumento() + " no puede tener una cuenta de ahorro."));
                                } else if (accountRequest.getTipoCuenta().equals(TipoCuenta.PLAZO_FIJO.name())) {
                                    {
                                        return Mono.error(new ConflictException("Cliente empresarial: " + accountRequest.getNumDocumento() + " no puede tener una cuenta a plazo fijo."));
                                    }
                                }
                            }
                            else if(cliente.getTipoCliente().name().equals(TipoCliente.PERSONAL.name()))  {
                                return cuentaBancariaRepository.findByIdCliente(accountRequest.getNumDocumento()).flatMap(cuentaBancaria -> Mono.error(
                                        new ConflictException("Cliente personal: " + accountRequest.getNumDocumento() + " ya tiene una cuenta de ahorro, una cuenta corriente o cuentas a plazo fijo")
                                )).then(Mono.just(cliente));
                            }
                            return Mono.just(cliente);
                        }).switchIfEmpty(Mono.error(new ConflictException("Cliente: " + accountRequest.getNumDocumento() + " no existe esta cuenta")))
                );

        CuentaBancaria cuentaBancaria = new CuentaBancaria(new ObjectId(), accountRequest.getNumProducto(), clienteSingle.blockingGet(), accountRequest.getTipoCuenta(), accountRequest.getSaldo());

        if(clienteSingle.blockingGet().getTipoCliente().name().equals(TipoCliente.EMPRESARIAL.name())){
            if(validarTitularFirmantes(accountRequest)){
                cuentaBancaria.setTitulares(accountRequest.getListTitulares());
                cuentaBancaria.setFirmantesAutorizados(accountRequest.getFirmantesAut());
            }
        }
        return Single.fromPublisher(cuentaBancariaRepository.save(cuentaBancaria));
    }

    @Override
    public Single<Credito> createClientCredit(AccountRequest accountRequest) {
        logger.info("INI - createClientCredit - ServiceIMPL");
        return  Single.fromPublisher(clientRepository.findByNumDocumento(accountRequest.getNumDocumento())
                        .switchIfEmpty(Mono.error(new ConflictException("Cliente: " + accountRequest.getNumDocumento() + " no existe esta cuenta")))
                        .flatMap(cliente -> creditoRepository.findByIdClient(cliente.getId().toString())
                                    .filter(cli -> cli.getTipoCredito().name().equals(TipoCliente.PERSONAL.name()))
                                    .flatMap(e -> Mono.error(new ConflictException("Cliente: " + accountRequest.getNumDocumento() + " - Solo se permite un solo credito por persona.")))
                                            .then(creditoRepository.save(new Credito(new ObjectId(), accountRequest.getNumProducto(), cliente.getId().toString() , cliente.getTipoCliente().name(), accountRequest.getMonto(), accountRequest.getSaldo())))));
    }

    @Override
    public Single<TarjetaCredito> createClientCreditCard(AccountRequest accountRequest) {
        logger.info("INI - createClientCreditCard - ServiceIMPL");
        return  Single.fromPublisher(clientRepository.findByNumDocumento(accountRequest.getNumDocumento())
                .flatMap(cliente -> {
                    TarjetaCredito tarjetaCredito = new TarjetaCredito(new ObjectId(), accountRequest.getNumProducto(), cliente, accountRequest.getMonto());
                    return tarjetaCreditoRepository.save(tarjetaCredito);
                })
                .switchIfEmpty(Mono.error(new ConflictException("Cliente: " + accountRequest.getNumDocumento() + " no existe esta cuenta"))));
    }

    private boolean validarTitularFirmantes(AccountRequest accountRequest){
        return accountRequest.getListTitulares().size() > 0 && accountRequest.getFirmantesAut() != null;
    }

    private Mono<Void> verificarClienteExiste(String docNumero){
        return this.clientRepository.findByNumDocumento(docNumero)
                .flatMap(productEntity -> Mono.error(
                        new ConflictException("Client docNumero already exists : " + docNumero)
                ));
    }

}
