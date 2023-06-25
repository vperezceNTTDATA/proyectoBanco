package proyecto.banco.bancoDemo.banco.service;

import io.reactivex.Observable;
import io.reactivex.Single;
import proyecto.banco.bancoDemo.banco.dto.AccountSaldoDTO;
import proyecto.banco.bancoDemo.banco.entity.Movimiento;

import java.math.BigDecimal;

public interface MovimientoService {
    Observable<Movimiento> getMovimientos();
    Single<BigDecimal> consultarSaldoCuentaBancaria(String cuentaNum);
    Single<BigDecimal> consultarSaldoTarjetaCredito(String tarjetaNum);
    Observable<Movimiento> consultarMovimientosCuentaBancaria(String cuentaNum);
    Observable<AccountSaldoDTO> consultarSaldoCuentas(String docNumero);


}
