package proyecto.banco.bancoDemo.banco.controller;

import io.reactivex.Observable;
import io.reactivex.Single;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import proyecto.banco.bancoDemo.banco.entity.Cliente;
import proyecto.banco.bancoDemo.banco.entity.Movimiento;
import proyecto.banco.bancoDemo.banco.service.ClientService;
import proyecto.banco.bancoDemo.banco.service.MovimientoService;

import java.math.BigDecimal;

@RestController
@RequestMapping("/transactions")
public class TransactionRestController {
    private static final Logger logger = LoggerFactory.getLogger(TransactionRestController.class);
    public static final String GET_TRANSACTIIONSByACCOUNT = "/{idAccount}/account";
    @Autowired
    private MovimientoService movimientoService;

    @RequestMapping(value ="/mov" , produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Observable<Movimiento> getMovimiento() {
        return movimientoService.getMovimientos();
    }

    @RequestMapping(value = TransactionRestController.GET_TRANSACTIIONSByACCOUNT, produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Observable<Movimiento> findTransactionsByProduct( @PathVariable String idAccount) {
        logger.info("INI - findTransactionsByProduct");
        return movimientoService.findTransactionsByProduct(idAccount);
    }


    @PostMapping("/{numDoc}/cuentas/{numCuenta}/deposito")
    public Single<ResponseEntity<String>> realizarDeposito(
            @PathVariable String numDoc,
            @PathVariable String numCuenta,
            @RequestParam String monto
    ) {
        // Obtener el cliente y realizar el depósito
       return null;
    }

    @PostMapping("/{numDoc}/cuentas/{numCuenta}/retiro")
    public Single<ResponseEntity<String>> realizarRetiro(
            @PathVariable String numDoc,
            @PathVariable String numCuenta,
            @RequestParam BigDecimal monto
    ) {
        return null;
    }

    @PostMapping("/{numDoc}/creditos/{codCredito}/pago")
    public Single<ResponseEntity<String>> realizarPagoCredito(
            @PathVariable String numDoc,
            @PathVariable String codCredito,
            @RequestParam BigDecimal monto
    ) {
        return null;
    }

    @PostMapping("/{numDoc}/tarjetas/{numTarjeta}/carga")
    public Single<ResponseEntity<String>> cargarConsumoTarjetaCredito(
            @PathVariable String numDoc,
            @PathVariable String numTarjeta,
            @RequestParam BigDecimal monto
    ) {
        return null;
    }

}
