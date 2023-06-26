package proyecto.banco.bancoDemo.banco.service;

import io.reactivex.Observable;
import io.reactivex.Single;
import proyecto.banco.bancoDemo.banco.dto.AccountRequest;
import proyecto.banco.bancoDemo.banco.dto.BalanceProductDTO;
import proyecto.banco.bancoDemo.banco.entity.Credito;
import proyecto.banco.bancoDemo.banco.entity.CuentaBancaria;
import proyecto.banco.bancoDemo.banco.entity.TarjetaCredito;

public interface AccountService {
    Single<CuentaBancaria> createClientAccount(AccountRequest accountRequest);
    Single<Credito> createClientCredit(AccountRequest accountRequest);
    Single<TarjetaCredito> createClientCreditCard(AccountRequest accountRequest);
    Observable<BalanceProductDTO> findBalanceProduct(String docNumberClient);

}
