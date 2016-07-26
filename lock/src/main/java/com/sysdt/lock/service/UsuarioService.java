package com.sysdt.lock.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sysdt.lock.dao.ClienteMapper;
import com.sysdt.lock.dao.UsuarioMapper;
import com.sysdt.lock.dto.UsuarioDTO;
import com.sysdt.lock.model.Cliente;
import com.sysdt.lock.model.Usuario;
import com.sysdt.lock.model.UsuarioExample;

@Service
@Transactional
public class UsuarioService {

	@Autowired
	private UsuarioMapper usuarioMapper;
	@Autowired
	private ClienteService clienteService;
	
	public UsuarioDTO obtenerYvalidarUsuario(String username, String password, String nombreCliente) throws Exception{
		Usuario usuario = obtenerUsuarioPorUsername(username);
		if(usuario == null){
			throw new Exception("Usuario inválido");
		}else if(!usuario.getEnabled()){
			throw new Exception("Su cuenta está deshabilitada. Por favor consulte a su proveedor");
		}else if(!usuario.getPassword().contentEquals(password)){
			throw new Exception("Password incorrecto");
		}
		Cliente cliente = clienteService.obtenerClientePorId(usuario.getIdCliente());
		if(!cliente.getNombre().contentEquals(nombreCliente.toUpperCase())){
			throw new Exception("Usuario no coincide con url");
		}
		return copiarUsuario(usuario, cliente);
	}
	
	public boolean guardarUsuario(Usuario usuario)throws Exception{
		usuarioTrim(usuario);
		Usuario user = obtenerUsuarioPorUsername(usuario.getUsername());
		if(user == null){
			insertarUsuario(usuario);
			return true;
		}
		return false;
	}
	
	public void deshabilitarCuentasDeUsuario(int idCliente) throws Exception{
		UsuarioExample exUser = new UsuarioExample();
		exUser.createCriteria().andIdClienteEqualTo(idCliente);
		Usuario user = new Usuario();
		user.setEnabled(false);
		usuarioMapper.updateByExampleSelective(user, exUser);
	}
	
	public List<Usuario> obtenerUsuariosPorIdClienteSinPass(int idCliente){
		List<Usuario> usuarios = obtenerUsuariosPorIdCliente(idCliente);
		for (Usuario usuario : usuarios) {
			usuario.setPassword("");
		}
		return usuarios;
	}
	
	
	//********* METODOS SIMPLES ******///
	
	private void insertarUsuario(Usuario usuario) throws Exception{
		usuario.setEnabled(true);
		usuarioMapper.insert(usuario);
	}
	
	public void actualizarUsuario(Usuario usuario) throws Exception{
		usuarioMapper.updateByPrimaryKey(usuario);
	}
	
	public void eliminarUsuarioPorId(String username) throws Exception{
		usuarioMapper.deleteByPrimaryKey(username);
	}
	
	public List<Usuario> obtenerUsuariosPorIdCliente(int idCliente){
		UsuarioExample exUser = new UsuarioExample();
		exUser.createCriteria().andIdClienteEqualTo(idCliente);
	//	exUser.setOrderByClause("username ASC");
		return usuarioMapper.selectByExample(exUser);
	}
	
	private Usuario obtenerUsuarioPorUsername(String username){
		return usuarioMapper.selectByPrimaryKey(username);
	}	
	
	private UsuarioDTO copiarUsuario(Usuario usuario, Cliente cliente){
		UsuarioDTO usuarioDTO =new UsuarioDTO();
		usuarioDTO.setIdCliente(usuario.getIdCliente());
		usuarioDTO.setEnabled(usuario.getEnabled());
		usuarioDTO.setIdTipousuario(usuario.getIdTipousuario());
		usuarioDTO.setUsername(usuario.getUsername());
		usuarioDTO.setCliente(cliente);
		return usuarioDTO;
	}
	
	private void usuarioTrim(Usuario usuario){
		usuario.setUsername(usuario.getUsername().trim().toLowerCase());
		usuario.setPassword(usuario.getPassword().trim().toLowerCase());
	}

	public String generarCodigo(String clave1, String clave2)throws Exception {
		return "C85WER6";
	}
	
}
