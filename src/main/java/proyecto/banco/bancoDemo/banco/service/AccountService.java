package proyecto.banco.bancoDemo.banco.service;

import io.reactivex.Observable;
import io.reactivex.Single;
import proyecto.banco.bancoDemo.banco.dto.AccountProductDTO;
import proyecto.banco.bancoDemo.banco.dto.AccountRequest;
import proyecto.banco.bancoDemo.banco.dto.BalanceProductDTO;
import proyecto.banco.bancoDemo.banco.entity.*;
import reactor.core.publisher.Mono;

public interface AccountService {
    Single<BankAccount> createClientAccount(AccountRequest accountRequest);
    Single<Credit> createClientCredit(AccountRequest accountRequest);
    Single<CreditCard> createClientCreditCard(AccountRequest accountRequest);
    Single<DebitCard> createDebitCardAccount(AccountRequest accountRequest);
    Observable<BalanceProductDTO> findBalanceProduct(String docNumberClient);
    Single<BankAccount> findResumenSaldoByDebitCard(String cuentaNum);
}
