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
import proyecto.banco.bancoDemo.banco.dto.ResponseDTO;
import proyecto.banco.bancoDemo.banco.entity.Movimiento;
import proyecto.banco.bancoDemo.banco.service.MovimientoService;

import java.math.BigDecimal;

@RestController
@RequestMapping("/transactions")
public class TransactionRestController {
    private static final Logger logger = LoggerFactory.getLogger(TransactionRestController.class);
    public static final String GET_TRANSACTIONSByACCOUNT = "/{idAccount}/account";
    public static final String POST_MAKE_DEPOSITO = "/{numCliente}/account/{numProd}/deposit";
    public static final String POST_MAKE_RETIRO = "/{numCliente}/account/{numProd}/retiro";
    public static final String POST_PAY_CREDIT = "/{numCliente}/credit/{numProd}/payment";
    public static final String POST_PAY_CREDIT_CARD = "/{numCliente}/creditCard/{numProd}/consume";

    @Autowired
    private MovimientoService movimientoService;

    @RequestMapping(value = TransactionRestController.GET_TRANSACTIONSByACCOUNT, produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Observable<Movimiento> findTransactionsByProduct( @PathVariable String idAccount) {
        logger.info("INI - findTransactionsByProduct");
        return movimientoService.findTransactionsByProduct(idAccount);
    }

    @PostMapping(TransactionRestController.POST_MAKE_DEPOSITO)
    public Single<ResponseEntity<String>> doDeposit(@PathVariable String numCliente, @PathVariable String numProd, @RequestParam String monto) {
        return movimientoService.makeDeposit(numCliente, numProd, new BigDecimal(monto))
                .map(response -> ResponseEntity.status(HttpStatus.CREATED).body("Depósito realizado exitosamente."));
    }

    @PostMapping(TransactionRestController.POST_MAKE_RETIRO)
    public Single<ResponseEntity<String>> doRetiro(@PathVariable String numCliente, @PathVariable String numProd, @RequestParam String monto) {
        return movimientoService.makeRetiro(numCliente, numProd, new BigDecimal(monto))
                .map(response -> ResponseEntity.status(HttpStatus.CREATED).body("Retiro realizado exitosamente."));
    }

    @PostMapping(TransactionRestController.POST_PAY_CREDIT)
    public Single<ResponseEntity<String>> doCreditPaid(@PathVariable String numCliente, @PathVariable String numProd, @RequestParam String monto) {
        return movimientoService.makeCreditPaid(numCliente, numProd, new BigDecimal(monto))
                .filter(ResponseDTO::isValid)
                .map(response -> ResponseEntity.status(HttpStatus.CREATED).body("Pago de crédito realizado exitosamente."))
                .switchIfEmpty(Single.just(ResponseEntity.status(HttpStatus.ACCEPTED).body("Credit ya está pagado")));
    }

    @PostMapping(TransactionRestController.POST_PAY_CREDIT_CARD)
    public Single<ResponseEntity<String>> doCreditCardConsume(@PathVariable String numCliente, @PathVariable String numProd, @RequestParam String monto) {
        return movimientoService.makeCreditCardConsume(numCliente, numProd, new BigDecimal(monto))
                .map(response -> ResponseEntity.status(HttpStatus.CREATED).body("Consumo de la tarjeta de crédito realizado exitosamente."));
    }

}
