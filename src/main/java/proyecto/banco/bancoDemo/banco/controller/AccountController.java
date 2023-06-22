package proyecto.banco.bancoDemo.banco.controller;


import io.reactivex.Single;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import proyecto.banco.bancoDemo.banco.entity.Cliente;
import proyecto.banco.bancoDemo.banco.service.ClientService;

import java.math.BigDecimal;

@RestController
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private ClientService clientService;


    @PostMapping("/{numDoc}/cuentas/{numCuenta}/deposito")
    public Single<ResponseEntity<String>> realizarDeposito(
            @PathVariable String numDoc,
            @PathVariable String numCuenta,
            @RequestParam BigDecimal monto
    ) {
        // Obtener el cliente y realizar el depósito
        Single<Cliente> cliente = clientService.getClientByDocNum(numDoc);

        return clientService.realizarDeposito(cliente, numCuenta, monto)
                .map(response -> {
                    if(response.isValid())return ResponseEntity.status(HttpStatus.CREATED).body("Depósito realizado exitosamente.");
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al depositar.");
                });
    }

    @PostMapping("/{numDoc}/cuentas/{numCuenta}/retiro")
    public Single<ResponseEntity<String>> realizarRetiro(
            @PathVariable String numDoc,
            @PathVariable String numCuenta,
            @RequestParam BigDecimal monto
    ) {
        Single<Cliente> cliente = clientService.getClientByDocNum(numDoc);

        return clientService.realizarRetiro(cliente, numCuenta, monto)
                .map(response -> {
                    if(response.isValid())return ResponseEntity.status(HttpStatus.CREATED).body("Retiro realizado exitosamente.");
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al retirar.");
                });
    }

    @PostMapping("/{numDoc}/creditos/{codCredito}/pago")
    public Single<ResponseEntity<String>> realizarPagoCredito(
            @PathVariable String numDoc,
            @PathVariable String codCredito,
            @RequestParam BigDecimal monto
    ) {
        // Obtener el cliente y realizar el pago del crédito
        Single<Cliente> cliente = clientService.getClientByDocNum(numDoc);

        return clientService.realizarPagoCredito(cliente, codCredito, monto)
                .map(response -> {
                    if(response.isValid())return ResponseEntity.status(HttpStatus.CREATED).body("Pago de credito realizado exitosamente.");
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al pagar credito.");
                });
    }

    @PostMapping("/{numDoc}/tarjetas/{numTarjeta}/carga")
    public Single<ResponseEntity<String>> cargarConsumoTarjetaCredito(
            @PathVariable String numDoc,
            @PathVariable String numTarjeta,
            @RequestParam BigDecimal monto
    ) {
        // Obtener el cliente y cargar el consumo en la tarjeta de crédito
        Single<Cliente> cliente = clientService.getClientByDocNum(numDoc);

        return clientService.cargarConsumoTarjetaCredito(cliente, numTarjeta, monto)
                .map(response -> {
                    if(response.isValid())return ResponseEntity.status(HttpStatus.CREATED).body("Carga de consumo en tarjeta de crédito realizada exitosamente.");
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al cargar consumo.");
                });
    }

}
