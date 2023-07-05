package proyecto.banco.bancoDemo.banco.service;

import io.reactivex.Observable;
import io.reactivex.Single;
import proyecto.banco.bancoDemo.banco.dto.ResumenSaldoDTO;
import proyecto.banco.bancoDemo.banco.entity.BankAccount;
import proyecto.banco.bancoDemo.banco.entity.Movimiento;

public interface ReportService {
    Observable<Movimiento> getCommissionsByProduct(String cuentaNum);
    Observable<ResumenSaldoDTO> getResumenDailyByProduct(String cuentaNum);
    Observable<Movimiento> findTenResumenByDebitAndCreditCard(String cuentaNum);
   // Single<BankAccount> findResumenSaldoByDebitCard(String cuentaNum);
}
