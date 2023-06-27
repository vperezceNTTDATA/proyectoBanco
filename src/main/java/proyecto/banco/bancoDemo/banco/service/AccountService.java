package proyecto.banco.bancoDemo.banco.service;

import io.reactivex.Observable;
import io.reactivex.Single;
import proyecto.banco.bancoDemo.banco.dto.AccountRequest;
import proyecto.banco.bancoDemo.banco.dto.BalanceProductDTO;
import proyecto.banco.bancoDemo.banco.entity.Credit;
import proyecto.banco.bancoDemo.banco.entity.BankAccount;
import proyecto.banco.bancoDemo.banco.entity.CreditCard;

public interface AccountService {
    Single<BankAccount> createClientAccount(AccountRequest accountRequest);
    Single<Credit> createClientCredit(AccountRequest accountRequest);
    Single<CreditCard> createClientCreditCard(AccountRequest accountRequest);
    Observable<BalanceProductDTO> findBalanceProduct(String docNumberClient);

}
