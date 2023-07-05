package proyecto.banco.bancoDemo.banco.controller;

import io.reactivex.Observable;
import io.reactivex.Single;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import proyecto.banco.bancoDemo.banco.dto.ResumenSaldoDTO;
import proyecto.banco.bancoDemo.banco.entity.BankAccount;
import proyecto.banco.bancoDemo.banco.entity.Movimiento;
import proyecto.banco.bancoDemo.banco.service.MovimientoService;
import proyecto.banco.bancoDemo.banco.service.ReportService;

@RestController
@RequestMapping(ReportRestController.REPORT)
public class ReportRestController {
    private static final Logger logger = LoggerFactory.getLogger(ReportRestController.class);
    public static final String GET_COMMISSIONS = "/{idProduct}/report";
    public static final String GET_RESUMEN_DAILY = "/{idProduct}/resumen";
    public static final String GET_RESUMEN_10_DEBIT_CREDIT = "/10/{idProduct}/debit-credit";
    public static final String GET_RESUMEN_DEBIT_CARD = "/{idProduct}/debit";
    public static final String REPORT = "/api/report";
    @Autowired
    private ReportService reportService;

    @RequestMapping(value = ReportRestController.GET_COMMISSIONS, produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Observable<Movimiento> findTransactionsByProduct(@PathVariable String idProduct) {
        logger.info("INI - findTransactionsByProduct");
        return reportService.getCommissionsByProduct(idProduct);
    }
    @RequestMapping(value = ReportRestController.GET_RESUMEN_DAILY, produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Observable<ResumenSaldoDTO> findResumenSaldoDaily(@PathVariable String idProduct) {
        logger.info("INI - findResumenSaldoDaily");
        return reportService.getResumenDailyByProduct(idProduct);
    }
    @RequestMapping(value = ReportRestController.GET_RESUMEN_10_DEBIT_CREDIT, produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Observable<Movimiento> findTenResumenByDebitAndCreditCard(@PathVariable String idProduct) {
        logger.info("INI - findTenResumenByDebitAndCreditCard");
        return reportService.findTenResumenByDebitAndCreditCard(idProduct);
    }
    /* @RequestMapping(value = ReportRestController.GET_RESUMEN_DEBIT_CARD, produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Single<BankAccount> findResumenSaldoByDebitCard(@PathVariable String idProduct) {
        logger.info("INI - findResumenSaldoByDebitCard");
        return reportService.findResumenSaldoByDebitCard(idProduct);
    } */
}
