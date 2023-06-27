package proyecto.banco.bancoDemo.banco.service;

import io.reactivex.Observable;
import io.reactivex.Single;
import proyecto.banco.bancoDemo.banco.dto.ResponseDTO;
import proyecto.banco.bancoDemo.banco.entity.Movimiento;

import java.math.BigDecimal;

public interface MovimientoService {
    Observable<Movimiento> getMovimientos();
    Observable<Movimiento> findTransactionsByProduct(String cuentaNum);

    Single<ResponseDTO> makeDeposit(String docClient, String numCuenta, BigDecimal monto);
    Single<ResponseDTO> makeRetiro(String docClient, String numCuenta, BigDecimal monto);
    Single<ResponseDTO> makeCreditPaid(String docClient, String numCuenta, BigDecimal monto);
    Single<ResponseDTO> makeCreditCardConsume(String docClient, String numCuenta, BigDecimal monto);
}
