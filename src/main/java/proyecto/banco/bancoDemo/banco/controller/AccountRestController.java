package proyecto.banco.bancoDemo.banco.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import proyecto.banco.bancoDemo.banco.dto.AccountProductDTO;
import proyecto.banco.bancoDemo.banco.entity.BankAccount;
import proyecto.banco.bancoDemo.banco.entity.Credit;
import proyecto.banco.bancoDemo.banco.entity.CreditCard;
import proyecto.banco.bancoDemo.banco.entity.DebitCard;
import proyecto.banco.bancoDemo.banco.service.ProductService;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(AccountRestController.ACCOUNT)
public class AccountRestController {
  private static final Logger logger = LoggerFactory.getLogger(AccountRestController.class);
  public static final String ACCOUNT = "/api/accounts";
  public static final String BANK_GET_ACCOUNT = "/bank-account/{accountNumber}";
  static final String BANK_GET_ACCOUNT_CLIENT = "/bank-account/{accountNumber}/client/{idClient}";
  public static final String GET_CREDIT_CLIENT = "/credit/{accountNumber}/client/{idClient}";
  static final String GET_CREDIT_CARD_CLIENT = "/credit-card/{accountNumber}/client/{idClient}";
  public static final String GET_DEBIT_CLIENT = "/debit/{accountNumber}";
  public static final String BANK_GET_BY_DEBIT = "/debit/bank-account/{accountNumber}";
  public static final String BANK_GET_ANY_ACCOUNT = "/any-product/{accountNumber}";
  public static final String UPDATE_BANK_ACCOUNT = "/update/bankAccount";
  public static final String UPDATE_CREDIT = "/update/credit";
  public static final String UPDATE_CREDIT_CARD = "/update/credit-card";

  @Autowired
  private ProductService productService;

  @RequestMapping(AccountRestController.BANK_GET_ACCOUNT)
  public Mono<BankAccount> findBankAccountByNumber(@PathVariable String accountNumber) {
    logger.info("INI - findBankAccountByNumber");
    return productService.findBankAccountById(accountNumber);
  }
  @RequestMapping(AccountRestController.BANK_GET_ACCOUNT_CLIENT)
  public Mono<BankAccount> findBankAccountByIdAndIdClient(@PathVariable String accountNumber, @PathVariable String idClient) {
    logger.info("INI - findBankAccountByIdAndIdClient");
    return productService.findBankAccountByIdAndIdClient(accountNumber, idClient);
  }

  @RequestMapping(AccountRestController.GET_CREDIT_CLIENT)
  public Mono<Credit> findCreditByIdAndIdClient(@PathVariable String accountNumber, @PathVariable String idClient) {
    logger.info("INI - findCreditByIdAndIdClient");
    return productService.findCreditByIdAndIdClient(accountNumber, idClient);
  }

  @RequestMapping(AccountRestController.GET_CREDIT_CARD_CLIENT)
  public Mono<CreditCard> findCreditCardByIdAndIdClient(@PathVariable String accountNumber, @PathVariable String idClient) {
    logger.info("INI - findCreditCardByIdAndIdClient");
    return productService.findCreditCardByIdAndIdClient(accountNumber, idClient);
  }

  @RequestMapping(AccountRestController.GET_DEBIT_CLIENT)
  public Mono<DebitCard> findDebitById(@PathVariable String accountNumber) {
    logger.info("INI - findDebitById");
    return productService.findDebitById(accountNumber);
  }

  @RequestMapping(AccountRestController.BANK_GET_BY_DEBIT)
  public Mono<BankAccount> findBankAccountByDebitCard(@PathVariable String accountNumber) {
    logger.info("INI - findBankAccountByDebitCard");
    return productService.findBankAccountByDebitCard(accountNumber);
  }

  @RequestMapping(AccountRestController.BANK_GET_ANY_ACCOUNT)
  public Mono<AccountProductDTO> findAnyProductByNumberProd(@PathVariable String accountNumber) {
    logger.info("INI - findAnyProductByNumberProd");
    return productService.findAnyProductByProdNumber(accountNumber);
  }

  @PostMapping(AccountRestController.UPDATE_BANK_ACCOUNT)
  public Mono<ResponseEntity<Void>> updateBankAccount(@RequestBody BankAccount bankAccount) {
    logger.info("INI - updateBankAccount");
    return productService.updateBankAccount(bankAccount).map(savedData -> ResponseEntity.ok().build());
  }

  @PostMapping(AccountRestController.UPDATE_CREDIT)
  public Mono<ResponseEntity<Void>> updateCredit(@RequestBody Credit credit) {
    logger.info("INI - updateCredit");
    return productService.updateCredit(credit).map(savedData -> ResponseEntity.ok().build());
  }

  @PostMapping(AccountRestController.UPDATE_CREDIT_CARD)
  public Mono<ResponseEntity<Void>> updateCreditCard(@RequestBody CreditCard creditCard) {
    logger.info("INI - updateCreditCard");
    return productService.updateCreditCard(creditCard).map(savedData -> ResponseEntity.ok().build());
  }

}
