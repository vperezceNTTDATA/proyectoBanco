package proyecto.banco.bancoDemo.banco.service;

import io.reactivex.Observable;
import io.reactivex.Single;
import proyecto.banco.bancoDemo.banco.dto.AccountRequest;
import proyecto.banco.bancoDemo.banco.dto.BalanceProductDTO;
import proyecto.banco.bancoDemo.banco.entity.BankAccount;
import proyecto.banco.bancoDemo.banco.entity.Credit;
import proyecto.banco.bancoDemo.banco.entity.CreditCard;
import proyecto.banco.bancoDemo.banco.entity.DebitCard;

/*
* Account Service.
*/
public interface AccountService {
  Single<BankAccount> createClientAccount(AccountRequest accountRequest);

  Single<Credit> createClientCredit(AccountRequest accountRequest);

  Single<CreditCard> createClientCreditCard(AccountRequest accountRequest);

  Single<DebitCard> createDebitCardAccount(AccountRequest accountRequest);

  Observable<BalanceProductDTO> findBalanceProduct(String docNumberClient);

  Single<BankAccount> findResumenSaldoByDebitCard(String cuentaNum);
}
