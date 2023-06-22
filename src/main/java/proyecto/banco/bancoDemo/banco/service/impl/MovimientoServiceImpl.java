package proyecto.banco.bancoDemo.banco.service.impl;

import io.reactivex.Observable;
import io.reactivex.Single;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import proyecto.banco.bancoDemo.banco.dto.AccountSaldoDTO;
import proyecto.banco.bancoDemo.banco.entity.CuentaBancaria;
import proyecto.banco.bancoDemo.banco.entity.Movimiento;
import proyecto.banco.bancoDemo.banco.entity.TarjetaCredito;
import proyecto.banco.bancoDemo.banco.enums.TipoCuenta;
import proyecto.banco.bancoDemo.banco.repository.CuentaBancariaRepository;
import proyecto.banco.bancoDemo.banco.repository.MovimientosRepository;
import proyecto.banco.bancoDemo.banco.repository.TarjetaCreditoRepository;
import proyecto.banco.bancoDemo.banco.service.MovimientoService;

import java.math.BigDecimal;

@Service
public class MovimientoServiceImpl implements MovimientoService {

    @Autowired
    private MovimientosRepository movimientosRepository;

    @Autowired
    private CuentaBancariaRepository cuentaBancariaRepository;

    @Autowired
    private TarjetaCreditoRepository tarjetaCreditoRepository;

    @Override
    public Observable<Movimiento> getMovimientos() {
        return Observable.fromPublisher(movimientosRepository.findAll());
    }

    @Override
    public Single<BigDecimal> consultarSaldoCuentaBancaria(String cuentaNum) {
        Single<CuentaBancaria> cuentaBancaria = Single.fromPublisher(cuentaBancariaRepository.findByNumero(cuentaNum));
        return cuentaBancaria.map(CuentaBancaria::getSaldo).toMaybe().switchIfEmpty(Single.just(BigDecimal.ZERO));
    }

    @Override
    public Single<BigDecimal>  consultarSaldoTarjetaCredito(String tarjetaNum) {
        Single<TarjetaCredito> tarjetaCredito = Single.fromPublisher(tarjetaCreditoRepository.findByNumero(tarjetaNum));
        return tarjetaCredito.map(TarjetaCredito::getSaldo).toMaybe().switchIfEmpty(Single.just(BigDecimal.ZERO));
    }

    @Override
    public Observable<Movimiento> consultarMovimientosCuentaBancaria(String cuentaNum) {
        return Observable.fromPublisher(movimientosRepository.findByCuentaBancariaNumero(cuentaNum));
    }

    @Override
    public Observable<AccountSaldoDTO> consultarSaldoCuentas(String docNumero) {
        BigDecimal p;
        Observable<CuentaBancaria> clienteCuenta = Observable.fromPublisher(cuentaBancariaRepository.findByIdCliente(docNumero));
        Observable<TarjetaCredito> clienteTarjeta = Observable.fromPublisher(tarjetaCreditoRepository.findByIdCliente(docNumero));

        return Observable.merge(
                clienteCuenta.map(a -> new AccountSaldoDTO(a.getTipoCuenta().name(), a.getSaldo(), a.getNumero())),
                clienteTarjeta.map(b -> new AccountSaldoDTO("TARJETA_CREDITO", b.getSaldo(), b.getNumero())));
    }
}
