package proyecto.banco.bancoDemo.banco.controller;


import io.reactivex.Observable;
import io.reactivex.Single;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import proyecto.banco.bancoDemo.banco.dto.AccountRequest;
import proyecto.banco.bancoDemo.banco.dto.BalanceProductDTO;
import proyecto.banco.bancoDemo.banco.entity.Credit;
import proyecto.banco.bancoDemo.banco.entity.BankAccount;
import proyecto.banco.bancoDemo.banco.entity.CreditCard;
import proyecto.banco.bancoDemo.banco.entity.DebitCard;
import proyecto.banco.bancoDemo.banco.service.AccountService;
import javax.validation.Valid;

@RestController
@RequestMapping(AccountController.ACCOUNT)
public class AccountController {
    private static final Logger logger = LoggerFactory.getLogger(AccountController.class);
    public static final String BANK_ACCOUNT = "/bankAccount";
    public static final String CREDIT_CARD = "/creditCard";
    public static final String CREDIT = "/credit";
    public static final String ACCOUNT = "/accounts";
    public static final String GET_BALANCE = "/{clienteNumDoc}/balances";
    public static final String CREATE_CREDIT_DEBIT = "/account/debit";
    @Autowired
    private AccountService accountService;
    @PostMapping(AccountController.BANK_ACCOUNT)
    public Single<BankAccount> createClientAccount(@RequestBody @Valid AccountRequest accountRequest) {
        logger.info("INI - createClientAccount");
        return accountService.createClientAccount(accountRequest);
    }

    @PostMapping(AccountController.CREDIT)
    public Single<Credit> createClientCredit(@RequestBody @Valid AccountRequest accountRequest) {
        logger.info("INI - createClientCredit");
        return accountService.createClientCredit(accountRequest);
    }

    @PostMapping(AccountController.CREDIT_CARD)
    public Single<CreditCard> createClientCreditCard(@RequestBody @Valid AccountRequest accountRequest) {
        logger.info("INI - createClientCreditCard");
        return accountService.createClientCreditCard(accountRequest);
    }
    @PostMapping(AccountController.CREATE_CREDIT_DEBIT)
    public Single<DebitCard> createClientDebitCard(@RequestBody @Valid AccountRequest accountRequest) {
        logger.info("INI - createDebitCardAccount");
        return accountService.createDebitCardAccount(accountRequest);
    }

    @RequestMapping(value = AccountController.GET_BALANCE, produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Observable<BalanceProductDTO> findBalanceProduct(@PathVariable String clienteNumDoc) {
        logger.info("INI - findBalanceProduct");
        return accountService.findBalanceProduct(clienteNumDoc);
    }

}
