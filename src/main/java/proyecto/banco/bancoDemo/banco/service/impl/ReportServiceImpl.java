package proyecto.banco.bancoDemo.banco.service.impl;

import io.reactivex.Observable;
import io.reactivex.Single;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import proyecto.banco.bancoDemo.banco.dto.AccountProductDTO;
import proyecto.banco.bancoDemo.banco.dto.ResumenSaldoDTO;
import proyecto.banco.bancoDemo.banco.entity.Movimiento;
import proyecto.banco.bancoDemo.banco.repository.*;
import proyecto.banco.bancoDemo.banco.service.ReportService;
import proyecto.banco.bancoDemo.model.exepcion.ConflictException;
import proyecto.banco.bancoDemo.model.exepcion.NotFoundException;
import reactor.core.publisher.Mono;
@Service
public class ReportServiceImpl implements ReportService {

    private static final Logger logger = LoggerFactory.getLogger(ReportServiceImpl.class);
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private MovimientosRepository movimientosRepository;
    @Autowired
    private BankAccountRepository bankAccountRepository;
    @Autowired
    private CreditCardRepository creditCardRepository;
    @Autowired
    private CreditRepository creditRepository;
    @Override
    public Observable<Movimiento> getCommissionsByProduct(String cuentaNum) {
        logger.info("INI - getCommissionsByProduct - ServiceIMPL");
        return Observable.fromPublisher(bankAccountRepository.findByNumero(cuentaNum)
                .flatMapMany(bankAccountSend -> movimientosRepository.findByCuentaBancariaNumero(bankAccountSend.getId().toString())
                        .filter(Movimiento::getWithCommission)
                        .switchIfEmpty(Mono.error(new NotFoundException("No tiene movimientos con comisiones."))))
                .switchIfEmpty(Mono.error(new NotFoundException("Número de cuenta: " + cuentaNum + " no existe."))));
    }
    @Override
    public Observable<ResumenSaldoDTO> getResumenDailyByProduct(String cuentaNum) {
        return null;
    }
}
