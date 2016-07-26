package com.sysdt.lock.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sysdt.lock.dao.ClienteMapper;
import com.sysdt.lock.model.Cliente;
import com.sysdt.lock.model.ClienteExample;

@Service
@Transactional
public class ClienteService {

	@Autowired
	private ClienteMapper clienteMapper;
	
	public Cliente obtenerClientePorId(int id){
		return clienteMapper.selectByPrimaryKey(id);
	}
	
	public List<Cliente> obtenerClientes(){
		return clienteMapper.selectByExample(null);
	}
	
	public Cliente obtenerClientePorNombre(String nombreCliente) {
		ClienteExample exCliente = new ClienteExample();
		exCliente.createCriteria().andNombreEqualTo(nombreCliente);
		 List<Cliente> clientes = clienteMapper.selectByExample(exCliente);
		 return clientes.size()>0?clientes.get(0):null;
	}
	
	public void insertarCliente(Cliente cliente)throws Exception{
		clienteTrim(cliente);
		cliente.setAlto("140");
		cliente.setAncho(320);
		clienteMapper.insert(cliente);
	}
	private void clienteTrim(Cliente cliente){
		cliente.setNombre(cliente.getNombre().trim().toUpperCase());
		cliente.setLogo(cliente.getLogo().trim().toLowerCase());
	}

	
}
