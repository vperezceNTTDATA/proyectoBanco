package proyecto.banco.bancoDemo.banco.service.impl;

import io.reactivex.Observable;
import io.reactivex.Single;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import proyecto.banco.bancoDemo.banco.dto.AccountProductDTO;
import proyecto.banco.bancoDemo.banco.dto.BalanceProductDTO;
import proyecto.banco.bancoDemo.banco.entity.CuentaBancaria;
import proyecto.banco.bancoDemo.banco.entity.Movimiento;
import proyecto.banco.bancoDemo.banco.entity.TarjetaCredito;
import proyecto.banco.bancoDemo.banco.repository.CreditoRepository;
import proyecto.banco.bancoDemo.banco.repository.CuentaBancariaRepository;
import proyecto.banco.bancoDemo.banco.repository.MovimientosRepository;
import proyecto.banco.bancoDemo.banco.repository.TarjetaCreditoRepository;
import proyecto.banco.bancoDemo.banco.service.MovimientoService;
import proyecto.banco.bancoDemo.model.exepcion.ConflictException;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@Service
public class MovimientoServiceImpl implements MovimientoService {
    private static final Logger logger = LoggerFactory.getLogger(MovimientoServiceImpl.class);
    @Autowired
    private MovimientosRepository movimientosRepository;
    @Autowired
    private CuentaBancariaRepository cuentaBancariaRepository;
    @Autowired
    private TarjetaCreditoRepository tarjetaCreditoRepository;
    @Autowired
    private CreditoRepository creditoRepository;

    @Override
    public Observable<Movimiento> getMovimientos() {
        return Observable.fromPublisher(movimientosRepository.findAll());
    }
    @Override
    public Observable<Movimiento> findTransactionsByProduct(String cuentaNum) {
        logger.info("INI - consultarMovimientosCuentaBancaria - ServiceIMPL");
        return this.findProductByProdNumber(cuentaNum)
                    .flatMapObservable(product -> Observable.fromPublisher(movimientosRepository.findByCuentaBancariaNumero(cuentaNum))
                            .switchIfEmpty(Observable.empty()));
    }
    public Single<AccountProductDTO> findProductByProdNumber(String cuentaNum) {
        logger.info("INI - findProductByProdNumber - ServiceIMPL");
        return Single.fromPublisher(cuentaBancariaRepository.findByNumero(cuentaNum)
                .map(cuentaBancaria -> {
                    AccountProductDTO accountProductDTO = new AccountProductDTO();
                    return accountProductDTO;
                })
                .switchIfEmpty(tarjetaCreditoRepository.findByNumero(cuentaNum)
                        .map(tarjetaCredito -> {
                            AccountProductDTO accountProductDTO = new AccountProductDTO();
                            return accountProductDTO;
                        }))
                .switchIfEmpty(creditoRepository.findByNumero(cuentaNum)
                        .map(credito -> {
                            AccountProductDTO accountProductDTO = new AccountProductDTO();
                            return accountProductDTO;
                        }))
                .switchIfEmpty(Mono.error(new ConflictException("ID no encontrado en ningún producto"))));
    }
    public Observable<BalanceProductDTO> consultarSaldoCuentas(String docNumero) {
        BigDecimal p;
        Observable<CuentaBancaria> clienteCuenta = Observable.fromPublisher(cuentaBancariaRepository.findByIdCliente(docNumero));
        Observable<TarjetaCredito> clienteTarjeta = Observable.fromPublisher(tarjetaCreditoRepository.findByIdCliente(docNumero));

        return Observable.merge(
                clienteCuenta.map(a -> new BalanceProductDTO("XD", a.getTipoCuenta().name(), a.getSaldo(), a.getNumero())),
                clienteTarjeta.map(b -> new BalanceProductDTO("XD", "TARJETA_CREDITO", b.getSaldo(), b.getNumero())));
    }
    public Single<BigDecimal> consultarSaldoCuentaBancaria(String cuentaNum) {
        Single<CuentaBancaria> cuentaBancaria = Single.fromPublisher(cuentaBancariaRepository.findByNumero(cuentaNum));
        return cuentaBancaria.map(CuentaBancaria::getSaldo).toMaybe().switchIfEmpty(Single.just(BigDecimal.ZERO));
    }
    public Single<BigDecimal>  consultarSaldoTarjetaCredito(String tarjetaNum) {
        Single<TarjetaCredito> tarjetaCredito = Single.fromPublisher(tarjetaCreditoRepository.findByNumero(tarjetaNum));
        return tarjetaCredito.map(TarjetaCredito::getSaldo).toMaybe().switchIfEmpty(Single.just(BigDecimal.ZERO));
    }
}
