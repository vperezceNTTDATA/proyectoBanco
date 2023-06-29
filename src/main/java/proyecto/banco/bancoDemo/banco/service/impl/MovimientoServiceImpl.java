package proyecto.banco.bancoDemo.banco.service.impl;

import io.reactivex.Observable;
import io.reactivex.Single;
import org.bson.types.Decimal128;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import proyecto.banco.bancoDemo.banco.dto.AccountProductDTO;
import proyecto.banco.bancoDemo.banco.dto.ResponseDTO;
import proyecto.banco.bancoDemo.banco.entity.Movimiento;
import proyecto.banco.bancoDemo.banco.repository.*;
import proyecto.banco.bancoDemo.banco.service.MovimientoService;
import proyecto.banco.bancoDemo.model.exepcion.ConflictException;
import proyecto.banco.bancoDemo.model.exepcion.NotFoundException;
import proyecto.banco.bancoDemo.util.Constantes;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@Service
public class MovimientoServiceImpl implements MovimientoService {
    private static final Logger logger = LoggerFactory.getLogger(MovimientoServiceImpl.class);
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private MovimientosRepository movimientosRepository;
    @Autowired
    private BankAccountRepository bankAccountRepository;
    @Autowired
    private CreditCardRepository creditCardRepository;
    @Autowired
    private CreditRepository creditRepository;

    @Override
    public Observable<Movimiento> findTransactionsByProduct(String cuentaNum) {
        logger.info("INI - consultarMovimientosCuentaBancaria - ServiceIMPL");
        return this.findProductByProdNumber(cuentaNum)
                    .flatMapObservable(product -> Observable.fromPublisher(movimientosRepository.findByCuentaBancariaNumero(product.getObjectId().toString()))
                            .switchIfEmpty(Observable.empty()));
    }

    @Override
    public Single<ResponseDTO> makeDeposit(String docNumClient, String numCuenta, BigDecimal monto) {
        logger.info("INI - makeDeposit - ServiceIMPL");
        return Single.fromPublisher(clientRepository.findByNumDocumento(docNumClient)
                .flatMap(client -> bankAccountRepository.findByNumero(numCuenta)
                                    .flatMap(cuentaBancaria -> {
                                        BigDecimal newMonto = monto;
                                        cuentaBancaria.setMovimientosActuales(cuentaBancaria.getMovimientosActuales() + 1);
                                        if(cuentaBancaria.getMovimientosActuales() >= cuentaBancaria.getMovimientosMensuales())newMonto = monto.subtract(Constantes.COMMISSION_AMOUNT);
                                        cuentaBancaria.setSaldo(cuentaBancaria.getSaldo().add(newMonto));
                                        bankAccountRepository.save(cuentaBancaria).subscribe();
                                        movimientosRepository.save(new Movimiento(new ObjectId(), cuentaBancaria.getId().toString(), newMonto, Constantes.DEPOSIT, monto)).subscribe();
                                        return Mono.just(new ResponseDTO(true, ""));
                                    }).switchIfEmpty(Mono.error(new ConflictException("Cuenta bancaria: " + numCuenta + " no existe."))))
                .switchIfEmpty(Mono.error(new ConflictException("Cliente: " + docNumClient + " no existe esta cuenta"))));
    }
    @Override
    public Single<ResponseDTO> makeRetiro(String docNumClient, String numCuenta, BigDecimal monto)  {
        logger.info("INI - makeRetiro - ServiceIMPL");
        return Single.fromPublisher(clientRepository.findByNumDocumento(docNumClient)
                .flatMap(client -> bankAccountRepository.findByNumero(numCuenta)
                        .flatMap(cuentaBancaria -> {
                            BigDecimal newMonto = monto;
                            cuentaBancaria.setMovimientosActuales(cuentaBancaria.getMovimientosActuales() + 1);
                            if(cuentaBancaria.getMovimientosActuales() >= cuentaBancaria.getMovimientosMensuales())newMonto = monto.add(Constantes.COMMISSION_AMOUNT);
                            if(cuentaBancaria.getSaldo().compareTo(newMonto) < 0)return Mono.error(new ConflictException("No tienes suficiente saldo."));
                            cuentaBancaria.setSaldo(cuentaBancaria.getSaldo().subtract(newMonto));
                            bankAccountRepository.save(cuentaBancaria).subscribe();
                            movimientosRepository.save(new Movimiento(new ObjectId(), cuentaBancaria.getId().toString(), newMonto, Constantes.RETIRO, monto)).subscribe();
                            return Mono.just(new ResponseDTO(true, ""));
                        }).switchIfEmpty(Mono.error(new ConflictException("Cuenta bancaria: " + numCuenta + " no existe."))))
                .switchIfEmpty(Mono.error(new ConflictException("Cliente: " + docNumClient + " no existe esta cuenta"))));
    }
    @Override
    public Single<ResponseDTO> makeCreditPaid(String docNumClient, String numCuenta, BigDecimal monto) {
        logger.info("INI - makeCreditPaid - ServiceIMPL");
        return Single.fromPublisher(clientRepository.findByNumDocumento(docNumClient)
                .flatMap(client -> creditRepository.findByNumero(numCuenta)
                        .flatMap(credit -> {
                            if(credit.getMonto().compareTo(credit.getMontoPagado()) == 0)return Mono.just(new ResponseDTO(false, ""));
                            if((credit.getMonto().subtract(credit.getMontoPagado())).compareTo(monto) < 0)credit.setMontoPagado(credit.getMonto());
                            else credit.setMontoPagado(credit.getMontoPagado().add(monto));
                            creditRepository.save(credit).subscribe();
                            movimientosRepository.save(new Movimiento(new ObjectId(), credit.getId().toString(), monto, Constantes.PAGO_CREDIT)).subscribe();
                            return Mono.just(new ResponseDTO(true, ""));
                        }).switchIfEmpty(Mono.error(new ConflictException("Credit: " + numCuenta + " no existe."))))
                .switchIfEmpty(Mono.error(new ConflictException("Cliente: " + docNumClient + " no existe esta cuenta"))));
    }
    @Override
    public Single<ResponseDTO> makeCreditCardConsume(String docNumClient, String numCuenta, BigDecimal monto) {
        logger.info("INI - makeCreditCardConsume - ServiceIMPL");
        return Single.fromPublisher(clientRepository.findByNumDocumento(docNumClient)
                .flatMap(client -> creditCardRepository.findByNumero(numCuenta)
                        .flatMap(creditCard -> {
                            if((creditCard.getLimiteCredito().subtract(creditCard.getConsumo())).compareTo(monto) < 0)return Mono.error(new ConflictException("El monto del consumo excede al limite de su tarjeta."));
                            creditCard.setConsumo(creditCard.getConsumo().add(monto));
                            creditCardRepository.save(creditCard).subscribe();
                            movimientosRepository.save(new Movimiento(new ObjectId(), creditCard.getId().toString(), monto, Constantes.CONSUMO_CREDIT_CARD)).subscribe();
                            return Mono.just(new ResponseDTO(true, ""));
                        }).switchIfEmpty(Mono.error(new ConflictException("Tarjeta de crédito: " + numCuenta + " no existe."))))
                .switchIfEmpty(Mono.error(new ConflictException("Cliente: " + docNumClient + " no existe esta cuenta"))));
    }

    @Override
    public Single<ResponseDTO> makeTransfer(String numSend, String numRec, BigDecimal monto) {
        logger.info("INI - makeTransfer - ServiceIMPL");

        return Single.fromPublisher(bankAccountRepository.findByNumero(numSend)
                .flatMap(bankAccountSend -> bankAccountRepository.findByNumero(numRec)
                        .flatMap(bankAccountRec -> {
                            if(bankAccountSend.getSaldo().compareTo(monto) < 0)return Mono.error(new ConflictException("No tienes suficiente saldo."));
                            bankAccountSend.setSaldo(bankAccountSend.getSaldo().subtract(monto));
                            bankAccountRepository.save(bankAccountSend).subscribe();
                            bankAccountRec.setSaldo(bankAccountRec.getSaldo().add(monto));
                            bankAccountRepository.save(bankAccountRec).subscribe();
                            if(bankAccountRec.getIdCliente().equals(bankAccountSend.getIdCliente()))movimientosRepository.save(new Movimiento(new ObjectId(), bankAccountSend.getId().toString(), monto, Constantes.TRANS_PROPIO, bankAccountRec.getId().toString())).subscribe();
                            else movimientosRepository.save(new Movimiento(new ObjectId(), bankAccountSend.getId().toString(), monto, Constantes.TRANS_TERCERO, bankAccountRec.getId().toString())).subscribe();
                            return Mono.just(new ResponseDTO(true, ""));
                        })
                        .switchIfEmpty(Mono.error(new ConflictException("Número de cuenta: " + numRec + " no existe."))))
                .switchIfEmpty(Mono.error(new ConflictException("Número de cuenta: " + numSend + " no existe."))));

    }

    @Override
    public Observable<Movimiento> getCommissionsByProduct(String cuentaNum) {
        logger.info("INI - getCommissionsByProduct - ServiceIMPL");
        return Observable.fromPublisher(bankAccountRepository.findByNumero(cuentaNum)
                .flatMapMany(bankAccountSend -> movimientosRepository.findByCuentaBancariaNumero(bankAccountSend.getId().toString())
                        .switchIfEmpty(Mono.error(new NotFoundException("No tiene movimientos con comisiones."))))
                .switchIfEmpty(Mono.error(new NotFoundException("Número de cuenta: " + cuentaNum + " no existe."))));
    }


    public Single<AccountProductDTO> findProductByProdNumber(String cuentaNum) {
        logger.info("INI - findProductByProdNumber - ServiceIMPL");
        return Single.fromPublisher(bankAccountRepository.findByNumero(cuentaNum)
                .map(bankAccount -> new AccountProductDTO(bankAccount.getId()))
                .switchIfEmpty(creditCardRepository.findByNumero(cuentaNum)
                        .map(creditCard -> new AccountProductDTO(creditCard.getId())))
                .switchIfEmpty(creditRepository.findByNumero(cuentaNum)
                        .map(credit -> new AccountProductDTO(credit.getId())))
                .switchIfEmpty(Mono.error(new ConflictException("ID no encontrado en ningún producto"))));
    }
}
