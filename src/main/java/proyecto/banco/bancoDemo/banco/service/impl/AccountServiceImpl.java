package proyecto.banco.bancoDemo.banco.service.impl;

import io.reactivex.Observable;
import io.reactivex.Single;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import proyecto.banco.bancoDemo.banco.dto.AccountProductDTO;
import proyecto.banco.bancoDemo.banco.dto.AccountRequest;
import proyecto.banco.bancoDemo.banco.dto.BalanceProductDTO;
import proyecto.banco.bancoDemo.banco.dto.ResponseDTO;
import proyecto.banco.bancoDemo.banco.entity.*;
import proyecto.banco.bancoDemo.banco.enums.TipoCliente;
import proyecto.banco.bancoDemo.banco.enums.TipoCuenta;
import proyecto.banco.bancoDemo.banco.repository.*;
import proyecto.banco.bancoDemo.banco.service.AccountService;
import proyecto.banco.bancoDemo.model.exepcion.ConflictException;
import proyecto.banco.bancoDemo.model.exepcion.NotFoundException;
import proyecto.banco.bancoDemo.util.Constantes;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@Service
public class AccountServiceImpl implements AccountService {
    private static final Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private BankAccountRepository bankAccountRepository;
    @Autowired
    private CreditRepository creditRepository;
    @Autowired
    private CreditCardRepository creditCardRepository;
    @Autowired
    private DebitCardRepository debitCardRepository;

    @Override
    public Single<BankAccount> createClientAccount(AccountRequest accountRequest) {
        logger.info("INI - createClientAccount - ServiceIMPL");

        Single<Cliente> clienteSingle = Single.fromPublisher(
                clientRepository.findByNumDocumento(accountRequest.getNumDocumento())
                        .flatMap(cliente -> {
                            if(cliente.getTipoCliente().name().equals(TipoCliente.EMPRESARIAL.name())) {
                                if (accountRequest.getTipoCuenta().equals(TipoCuenta.AHORRO.name())) return Mono.error(new ConflictException("Cliente empresarial: " + accountRequest.getNumDocumento() + " no puede tener una cuenta de ahorro."));
                                else if (accountRequest.getTipoCuenta().equals(TipoCuenta.PLAZO_FIJO.name())) return Mono.error(new ConflictException("Cliente empresarial: " + accountRequest.getNumDocumento() + " no puede tener una cuenta a plazo fijo."));
                            }
                            else if(cliente.getTipoCliente().name().equals(TipoCliente.PERSONAL.name()))  {
                                return bankAccountRepository.findByIdCliente(cliente.getId().toString()).flatMap(bankAccount -> Mono.error(
                                        new ConflictException("Cliente personal: " + accountRequest.getNumDocumento() + " ya tiene una cuenta de ahorro, una cuenta corriente o cuentas a plazo fijo")))
                                        .then(this.getExpiredCreditCard(cliente.getId().toString()).then(Mono.just(cliente)));
                            }
                            else if(cliente.getTipoCliente().name().equals(TipoCliente.PERSONAL_VIP.name()))  {
                                return creditCardRepository.findByIdCliente(cliente.getId().toString())
                                        .switchIfEmpty(Mono.error(new ConflictException("Cliente VIP: " + accountRequest.getNumDocumento() + " debe tener una tarjeta de crédito. ")))
                                        .then(this.getExpiredCreditCard(cliente.getId().toString()).then(Mono.just(cliente)));
                            }
                            else if(cliente.getTipoCliente().name().equals(TipoCliente.EMPRESARIAL_PYME.name()))  {
                                return bankAccountRepository.findByIdCliente(cliente.getId().toString())
                                        .filter(bankAccount -> bankAccount.getTipoCuenta().name().equals(TipoCuenta.CUENTA_CORRIENTE.name()))
                                        .switchIfEmpty(Mono.error(new ConflictException("Cliente PYME: " + accountRequest.getNumDocumento() + " debe de tener una cuenta corriente")))
                                        .then(creditCardRepository.findByIdCliente(cliente.getId().toString())
                                                .switchIfEmpty(Mono.error(new ConflictException("Cliente PYME: " + accountRequest.getNumDocumento() + " debe tener una tarjeta de crédito. ")))
                                                .then(this.getExpiredCreditCard(cliente.getId().toString()).then(Mono.just(cliente))));
                            }
                            return this.getExpiredCreditCard(cliente.getId().toString()).then(Mono.just(cliente));
                        }).switchIfEmpty(Mono.error(new NotFoundException("Cliente: " + accountRequest.getNumDocumento() + " no existe esta cuenta")))
                );
        BankAccount bankAccount = new BankAccount(new ObjectId(), accountRequest.getNumProducto(), clienteSingle.blockingGet().getId().toString(), accountRequest.getTipoCuenta(), accountRequest.getSaldo());
        if(clienteSingle.blockingGet().getTipoCliente().name().equals(TipoCliente.EMPRESARIAL.name())){
            if(validarTitularFirmantes(accountRequest)){
                bankAccount.setTitulares(accountRequest.getListTitulares());
                bankAccount.setFirmantesAutorizados(accountRequest.getFirmantesAut());
            }
        }
        return Single.fromPublisher(bankAccountRepository.save(bankAccount));
    }
    @Override
    public Single<Credit> createClientCredit(AccountRequest accountRequest) {
        logger.info("INI - createClientCredit - ServiceIMPL");
        return  Single.fromPublisher(clientRepository.findByNumDocumento(accountRequest.getNumDocumento())
                        .switchIfEmpty(Mono.error(new NotFoundException("Cliente: " + accountRequest.getNumDocumento() + " no existe esta cuenta")))
                        .flatMap(cliente -> this.getExpiredCreditCard(cliente.getId().toString())
                                    .then(creditRepository.findByIdClient(cliente.getId().toString())
                                    .filter(cli -> cli.getTipoCredito().name().equals(TipoCliente.PERSONAL.name()))
                                    .flatMap(e -> Mono.error(new ConflictException("Cliente: " + accountRequest.getNumDocumento() + " - Solo se permite un solo crédito por persona.")))
                                            .then(creditRepository.save(new Credit(new ObjectId(), accountRequest.getNumProducto(), cliente.getId().toString() , cliente.getTipoCliente().name(), accountRequest.getMonto()))))));
    }
    @Override
    public Single<CreditCard> createClientCreditCard(AccountRequest accountRequest) {
        logger.info("INI - createClientCreditCard - ServiceIMPL");
        return  Single.fromPublisher(clientRepository.findByNumDocumento(accountRequest.getNumDocumento())
                .flatMap(cliente -> this.getExpiredCreditCard(cliente.getId().toString())
                        .then(creditCardRepository.save(new CreditCard(new ObjectId(), accountRequest.getNumProducto(), cliente.getId().toString(), accountRequest.getMonto()))))
                .switchIfEmpty(Mono.error(new NotFoundException("Cliente: " + accountRequest.getNumDocumento() + " no existe esta cuenta"))));
    }

    @Override
    public Single<DebitCard> createDebitCardAccount(AccountRequest accountRequest) {
        logger.info("INI - createDebitCardAccount - ServiceIMPL");
        return Single.fromPublisher(clientRepository.findByNumDocumento(accountRequest.getNumDocumento())
                .flatMapMany(client -> bankAccountRepository.findByNumero(accountRequest.getNumProducto())
                        .flatMapMany(cuentaBancaria -> debitCardRepository.findByCardNumber(accountRequest.getNumberDebitCard())
                                         .flatMap(debitCard -> {
                                             debitCard.setBankAccounts(debitCard.getNumberBankAccounts(), cuentaBancaria.getNumero());
                                             logger.info("INI - debitCard ");
                                             return debitCardRepository.save(debitCard);
                                         })
                                .defaultIfEmpty(new DebitCard())
                                .flatMap(debitCard -> {
                                    logger.info("INI - debitCard v3 " + debitCard.getId());
                                    if(debitCard.getId() == null) return debitCardRepository.save(new DebitCard(new ObjectId(), accountRequest.getNumberDebitCard(), cuentaBancaria.getNumero()));
                                    return Mono.just(debitCard);
                                 }))
                        .switchIfEmpty(Mono.error(new ConflictException("Cuenta bancaria: " + accountRequest.getNumProducto() + " no existe."))))
                .switchIfEmpty(Mono.error(new ConflictException("Cliente: " + accountRequest.getNumDocumento() + " no existe esta cuenta"))));}

    @Override
    public Observable<BalanceProductDTO> findBalanceProduct(String docNumberClient) {
        logger.info("INI - findBalanceProduct - ServiceIMPL");

        return Observable.fromPublisher(clientRepository.findByNumDocumento(docNumberClient)
                        .flatMapMany(cliente -> bankAccountRepository.findByIdCliente(cliente.getId().toString())
                               .map(bankAccount -> new BalanceProductDTO(Constantes.NAME_BANK_ACCOUNT, bankAccount.getTipoCuenta().name(), bankAccount.getSaldo(), bankAccount.getNumero()))
                                .concatWith(creditCardRepository.findByIdCliente(cliente.getId().toString())
                                           .map(cardCredit -> new BalanceProductDTO(Constantes.NAME_CREDIT_CARD, cliente.getTipoCliente().name(), cardCredit.getUtilizedBalance(), cardCredit.getCardNumber(), cardCredit.getAvailableBalance()))))
                .switchIfEmpty(Mono.error(new NotFoundException("Cliente: " + docNumberClient + " no existe esta cuenta"))));
    }

    public Flux<CreditCard> getExpiredCreditCard(String idCliente) {
        logger.info("INI - getExpiredCreditCard - ServiceIMPL");
        return creditCardRepository.findByIdCliente(idCliente)
                .filter(CreditCard::getIsExpired)
                .flatMap(creditCard -> Mono.error(new ConflictException("Posee alguna deuda vencida en la cuenta: " + creditCard.getCardNumber())));
    }

    private boolean validarTitularFirmantes(AccountRequest accountRequest){
        return accountRequest.getListTitulares().size() > 0 && accountRequest.getFirmantesAut().size() > 0;
    }
}
