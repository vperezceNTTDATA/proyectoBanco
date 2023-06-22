package proyecto.banco.bancoDemo.banco.controller;

import io.reactivex.Observable;
import io.reactivex.Single;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import proyecto.banco.bancoDemo.banco.dto.AccountSaldoDTO;
import proyecto.banco.bancoDemo.banco.entity.Movimiento;
import proyecto.banco.bancoDemo.banco.service.ClientService;
import proyecto.banco.bancoDemo.banco.service.MovimientoService;

import java.math.BigDecimal;

@RestController
@RequestMapping("/mov")
public class MovimientoRestController {

    @Autowired
    private MovimientoService movimientoService;

    @Autowired
    private ClientService clientService;

    @RequestMapping(value ="/mov" , produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Observable<Movimiento> getMovimiento() {
        return movimientoService.getMovimientos();
    }

    @GetMapping("/{clienteId}/cuentas/{cuentaId}/saldo")
    public Single<ResponseEntity<BigDecimal>> consultarSaldoCuentaBancaria(
            @PathVariable String clienteId,
            @PathVariable String cuentaId
    ) {
        return movimientoService.consultarSaldoCuentaBancaria(cuentaId)
                .map(response -> ResponseEntity.status(HttpStatus.CREATED).body(response))
                .onErrorResumeNext(throwable -> Single.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new BigDecimal("0.00"))));

    }

    @GetMapping("/{clienteId}/tarjetas/{tarjetaId}/saldo")
    public Single<ResponseEntity<BigDecimal>> consultarSaldoTarjetaCredito(
            @PathVariable String clienteId,
            @PathVariable String tarjetaId
    ) {
        return movimientoService.consultarSaldoTarjetaCredito(tarjetaId)
                .map(response -> ResponseEntity.status(HttpStatus.CREATED).body(response))
                .onErrorResumeNext(throwable -> Single.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new BigDecimal("0.00"))));

    }

    @GetMapping("/cuenta/saldo/{clienteId}")
    public Observable<ResponseEntity<AccountSaldoDTO>> consultarSaldoTarjetaCredito(
            @PathVariable String clienteId
    ) {
        return movimientoService.consultarSaldoCuentas(clienteId)
                .map(response -> ResponseEntity.status(HttpStatus.CREATED).body(response));

    }

    @GetMapping("/{clienteId}/cuentas/{cuentaId}/movimientos")
    public Observable<Movimiento> consultarMovimientosCuentaBancaria(
            @PathVariable String clienteId,
            @PathVariable String cuentaId
    ) {
        return movimientoService.consultarMovimientosCuentaBancaria(cuentaId);
    }

}
