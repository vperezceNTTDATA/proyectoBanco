package proyecto.banco.bancoDemo.banco.service;

import io.reactivex.Observable;
import proyecto.banco.bancoDemo.banco.entity.Movimiento;

public interface MovimientoService {
    Observable<Movimiento> getMovimientos();
    Observable<Movimiento> findTransactionsByProduct(String cuentaNum);


}
