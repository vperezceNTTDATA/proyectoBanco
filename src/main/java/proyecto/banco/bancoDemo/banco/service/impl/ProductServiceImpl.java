package proyecto.banco.bancoDemo.banco.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import proyecto.banco.bancoDemo.banco.dto.AccountProductDTO;
import proyecto.banco.bancoDemo.banco.entity.BankAccount;
import proyecto.banco.bancoDemo.banco.entity.Credit;
import proyecto.banco.bancoDemo.banco.entity.CreditCard;
import proyecto.banco.bancoDemo.banco.entity.DebitCard;
import proyecto.banco.bancoDemo.banco.repository.*;
import proyecto.banco.bancoDemo.banco.service.ProductService;
import proyecto.banco.bancoDemo.model.exepcion.ConflictException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ProductServiceImpl implements ProductService {
  private static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);
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
  public Mono<BankAccount> updateBankAccount(BankAccount bankAccount) {
    logger.info("INI - updateBankAccount - ServiceIMPL");
    return bankAccountRepository.save(bankAccount);
  }
  @Override
  public Mono<CreditCard> updateCreditCard(CreditCard creditCard) {
    logger.info("INI - updateCreditCard - ServiceIMPL");
    return creditCardRepository.save(creditCard);
  }
  @Override
  public Mono<Credit> updateCredit(Credit credit) {
    logger.info("INI - updateCredit - ServiceIMPL");
    return creditRepository.save(credit);
  }
  @Override
  public Mono<BankAccount> findBankAccountById(String accountNumber) {
    logger.info("INI - findBankAccountById - ServiceIMPL");
    return bankAccountRepository.findByNumero(accountNumber);
  }
  @Override
  public Mono<BankAccount> findBankAccountByIdAndIdClient(String accountNumber, String idClient) {
    logger.info("INI - findBankAccountByIdAndIdClient - ServiceIMPL");
    return bankAccountRepository.findByNumeroAndIdCliente(accountNumber, idClient);
  }

  @Override
  public Mono<Credit> findCreditByIdAndIdClient(String accountNumber, String idClient) {
    logger.info("INI - findByNumeroAndIdCliente - ServiceIMPL");
    return creditRepository.findByNumeroAndIdCliente(accountNumber, idClient);
  }

  @Override
  public Mono<CreditCard> findCreditCardByIdAndIdClient(String accountNumber, String idClient) {
    logger.info("INI - findByNumeroAndIdCliente - ServiceIMPL");
    return creditCardRepository.findByCardNumberAndIdCliente(accountNumber, idClient);
  }
  @Override
  public Mono<DebitCard> findDebitById(String cuentaNum) {
    logger.info("INI - findDebitById - ServiceIMPL");
    return debitCardRepository.findByCardNumber(cuentaNum);
  }

  @Override
  public Mono<BankAccount> findBankAccountByDebitCard(String numDebitCard) {
    return debitCardRepository.findByCardNumber(numDebitCard)
        .flatMap(numberProduct -> Flux.fromIterable(numberProduct.getNumberBankAccounts())
            .flatMap(numberAccount -> bankAccountRepository.findByNumero(numberAccount))
            .take(1).single());
  }

  @Override
  public Mono<AccountProductDTO> findAnyProductByProdNumber(String cuentaNum) {
    logger.info("INI - findAnyProductByProdNumber - ServiceIMPL");
    return bankAccountRepository.findByNumero(cuentaNum)
        .map(bankAccount -> new AccountProductDTO(bankAccount.getId()))
        .switchIfEmpty(creditCardRepository.findByCardNumber(cuentaNum)
            .map(creditCard -> new AccountProductDTO(creditCard.getId())))
        .switchIfEmpty(creditRepository.findByNumero(cuentaNum)
            .map(credit -> new AccountProductDTO(credit.getId())))
        .switchIfEmpty(Mono.error(new ConflictException("ID no encontrado en ningún producto")));
  }

}
