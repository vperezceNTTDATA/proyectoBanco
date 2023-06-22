package proyecto.banco.bancoDemo.banco.service;

import io.reactivex.Observable;
import io.reactivex.Single;
import proyecto.banco.bancoDemo.banco.dto.AccountRequestDTO;
import proyecto.banco.bancoDemo.banco.dto.ClienteRequestDTO;
import proyecto.banco.bancoDemo.banco.dto.ResponseDTO;
import proyecto.banco.bancoDemo.banco.entity.Cliente;

import java.math.BigDecimal;

public interface ClientService {
    Observable<Cliente> getClients();
    Single<Cliente> createClient(ClienteRequestDTO clienteRequestDTO);

    Single<ResponseDTO> createClientAccount(AccountRequestDTO accountRequestDTO);
    Single<Cliente> getClientByDocNum(String docNum);
    Single<ResponseDTO> realizarDeposito(Single<Cliente> cliente, String numCuenta, BigDecimal monto);
    Single<ResponseDTO> realizarRetiro(Single<Cliente> cliente, String numCuenta, BigDecimal monto);
    Single<ResponseDTO> realizarPagoCredito(Single<Cliente> cliente, String numCuenta, BigDecimal monto);
    Single<ResponseDTO> cargarConsumoTarjetaCredito(Single<Cliente> cliente, String numTarjeta, BigDecimal monto);

}
