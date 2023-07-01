package proyecto.banco.bancoDemo.banco.service;

import io.reactivex.Observable;
import proyecto.banco.bancoDemo.banco.dto.ResumenSaldoDTO;
import proyecto.banco.bancoDemo.banco.entity.Movimiento;

public interface ReportService {
    Observable<Movimiento> getCommissionsByProduct(String cuentaNum);
    Observable<ResumenSaldoDTO> getResumenDailyByProduct(String cuentaNum);
}
