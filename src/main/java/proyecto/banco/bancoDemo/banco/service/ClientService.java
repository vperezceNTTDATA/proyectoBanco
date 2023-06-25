package proyecto.banco.bancoDemo.banco.service;

import io.reactivex.Observable;
import io.reactivex.Single;
import proyecto.banco.bancoDemo.banco.dto.AccountRequest;
import proyecto.banco.bancoDemo.banco.dto.ClienteRequest;
import proyecto.banco.bancoDemo.banco.dto.ResponseDTO;
import proyecto.banco.bancoDemo.banco.entity.Cliente;
import proyecto.banco.bancoDemo.banco.entity.CuentaBancaria;

import java.math.BigDecimal;

public interface ClientService {
    Observable<Cliente> getClients();
    Single<Cliente> createClient(ClienteRequest clienteRequestDTO);
    Single<Cliente> getClientByDocNum(String docNum);







    /* Transaction history */
    Single<ResponseDTO> realizarDeposito(Single<Cliente> cliente, String numCuenta, BigDecimal monto);
    Single<ResponseDTO> realizarRetiro(Single<Cliente> cliente, String numCuenta, BigDecimal monto);
    Single<ResponseDTO> realizarPagoCredito(Single<Cliente> cliente, String numCuenta, BigDecimal monto);
    Single<ResponseDTO> cargarConsumoTarjetaCredito(Single<Cliente> cliente, String numTarjeta, BigDecimal monto);

}
