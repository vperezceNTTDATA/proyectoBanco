package proyecto.banco.bancoDemo.banco.controller;


import io.reactivex.Observable;
import io.reactivex.Single;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import proyecto.banco.bancoDemo.banco.dto.AccountRequest;
import proyecto.banco.bancoDemo.banco.dto.AccountSaldoDTO;
import proyecto.banco.bancoDemo.banco.entity.Cliente;
import proyecto.banco.bancoDemo.banco.entity.Credito;
import proyecto.banco.bancoDemo.banco.entity.CuentaBancaria;
import proyecto.banco.bancoDemo.banco.entity.TarjetaCredito;
import proyecto.banco.bancoDemo.banco.service.AccountService;
import proyecto.banco.bancoDemo.banco.service.ClientService;
import proyecto.banco.bancoDemo.banco.service.MovimientoService;

import javax.validation.Valid;
import java.math.BigDecimal;

@RestController
@RequestMapping(AccountController.ACCOUNT)
public class AccountController {
    private static final Logger logger = LoggerFactory.getLogger(AccountController.class);
    public static final String BANK_ACCOUNT = "/bankAccount";
    public static final String CREDIT_CARD = "/creditCard";
    public static final String CREDIT = "/credit";
    public static final String ACCOUNT = "/accounts";

    @Autowired
    private ClientService clientService;
    @Autowired
    private MovimientoService movimientoService;
    @Autowired
    private AccountService accountService;

    @PostMapping(AccountController.BANK_ACCOUNT)
    public Single<CuentaBancaria> createClientAccount(@RequestBody @Valid AccountRequest accountRequest) {
        logger.info("INI - createClientAccount");
        return accountService.createClientAccount(accountRequest);
    }

    @PostMapping(AccountController.CREDIT)
    public Single<Credito> createClientCredit(@RequestBody @Valid AccountRequest accountRequest) {
        logger.info("INI - createClientCredit");
        return accountService.createClientCredit(accountRequest);
    }

    @PostMapping(AccountController.CREDIT_CARD)
    public Single<TarjetaCredito> createClientCreditCard(@RequestBody @Valid AccountRequest accountRequest) {
        logger.info("INI - createClientCreditCard");
        return accountService.createClientCreditCard(accountRequest);
    }

    @GetMapping("/{clienteId}/cuentas/{cuentaId}/saldo")
    public Single<ResponseEntity<BigDecimal>> consultarSaldoCuentaBancaria(
            @PathVariable String clienteId,
            @PathVariable String cuentaId
    ) {
        return movimientoService.consultarSaldoCuentaBancaria(cuentaId)
                .map(response -> ResponseEntity.status(HttpStatus.CREATED).body(response))
                .onErrorResumeNext(throwable -> Single.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new BigDecimal("0.00"))));

    }

    @GetMapping("/{clienteId}/tarjetas/{tarjetaId}/saldo")
    public Single<ResponseEntity<BigDecimal>> consultarSaldoTarjetaCredito(
            @PathVariable String clienteId,
            @PathVariable String tarjetaId
    ) {
        return movimientoService.consultarSaldoTarjetaCredito(tarjetaId)
                .map(response -> ResponseEntity.status(HttpStatus.CREATED).body(response))
                .onErrorResumeNext(throwable -> Single.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new BigDecimal("0.00"))));

    }

    @GetMapping("/cuenta/saldo/{clienteId}")
    public Observable<ResponseEntity<AccountSaldoDTO>> consultarSaldoTarjetaCredito(
            @PathVariable String clienteId
    ) {
        return movimientoService.consultarSaldoCuentas(clienteId)
                .map(response -> ResponseEntity.status(HttpStatus.CREATED).body(response));

    }

}
