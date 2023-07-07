package proyecto.banco.bancoDemo.banco.service;

import proyecto.banco.bancoDemo.banco.dto.AccountProductDTO;
import proyecto.banco.bancoDemo.banco.entity.BankAccount;
import proyecto.banco.bancoDemo.banco.entity.Credit;
import proyecto.banco.bancoDemo.banco.entity.CreditCard;
import proyecto.banco.bancoDemo.banco.entity.DebitCard;
import reactor.core.publisher.Mono;

public interface ProductService {
    Mono<BankAccount> updateBankAccount(BankAccount bankAccount);
    Mono<CreditCard> updateCreditCard(CreditCard creditCard);
    Mono<Credit> updateCredit(Credit credit);
    Mono<BankAccount> findBankAccountById(String accountNumber);
    Mono<BankAccount> findBankAccountByIdAndIdClient(String accountNumber, String idClient);
    Mono<Credit> findCreditByIdAndIdClient(String accountNumber, String idClient);
    Mono<CreditCard> findCreditCardByIdAndIdClient(String accountNumber, String idClient);
    Mono<AccountProductDTO> findAnyProductByProdNumber(String cuentaNum);
    Mono<DebitCard> findDebitById(String cuentaNum);
    Mono<BankAccount> findBankAccountByDebitCard(String numDebitCard);
}
