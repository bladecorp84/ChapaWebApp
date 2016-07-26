package com.sysdt.lock.view;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import com.sysdt.lock.dto.UsuarioDTO;
import com.sysdt.lock.enums.TipoUsuarioEnum;
import com.sysdt.lock.model.Cliente;
import com.sysdt.lock.model.TipoUsuario;
import com.sysdt.lock.model.Usuario;
import com.sysdt.lock.service.CatalogoService;
import com.sysdt.lock.service.ClienteService;
import com.sysdt.lock.service.UsuarioService;
import com.sysdt.lock.util.MensajeGrowl;

@ManagedBean
@ViewScoped
public class AdminView implements Serializable {

	private static final long serialVersionUID = 1L;
	@ManagedProperty("#{manejoSesionView}")
	private ManejoSesionView manejoSesionView;
	@ManagedProperty("#{clienteService}")
	private ClienteService clienteService;
	@ManagedProperty("#{usuarioService}")
	private UsuarioService usuarioService;
	@ManagedProperty("#{catalogoService}")
	private CatalogoService catalogoService;
	
	private List<Cliente> clientes;
	private List<TipoUsuario> tiposUsuario;
	private List<Usuario> usuarios;
	private Cliente cliente;
	private Usuario usuario;
	private Usuario usuarioSel;
	private int clienteSel;
	
	@PostConstruct
	public void init(){
		UsuarioDTO userDTO = manejoSesionView.obtenerUsuarioEnSesion();
		if(userDTO == null || userDTO.getIdTipousuario() != TipoUsuarioEnum.ADMINISTRADOR.getId()){
			manejoSesionView.cerrarSesionUsuario();
		}else{
			cliente = new Cliente();
			usuario = new Usuario();
			resetUsuarioSel();
			clientes = clienteService.obtenerClientes();
			tiposUsuario = catalogoService.obtenerTiposUsuario();
			clienteSel = clientes.get(0).getId();
			usuarios = usuarioService.obtenerUsuariosPorIdCliente(clienteSel);
		}
	}
	
	public void guardarUsuario(){
		if(validarUsuario()){
			try {
				boolean exito = usuarioService.guardarUsuario(usuario);
				if(exito){
					usuario = new Usuario();
					usuarios = usuarioService.obtenerUsuariosPorIdCliente(clienteSel);
					MensajeGrowl.mostrar("El usuario se guardó exitosamente", FacesMessage.SEVERITY_INFO);
				}else{
					MensajeGrowl.mostrar("El nombre de usuario ya existe", FacesMessage.SEVERITY_ERROR);
				}
			} catch (Exception e) {
				MensajeGrowl.mostrar("Error al guardar usuario", FacesMessage.SEVERITY_FATAL);
			}
		}
	}

	
	public void guardarCliente(){
		if(validarCliente()){
			try {
				clienteService.insertarCliente(cliente);
				clientes = clienteService.obtenerClientes();
				cliente = new Cliente();
				MensajeGrowl.mostrar("El cliente se guardó exitosamente", FacesMessage.SEVERITY_INFO);;
			} catch (Exception e) {
				MensajeGrowl.mostrar("Error al guardar cliente", FacesMessage.SEVERITY_FATAL);
			}
		}
	}
	
	public void actualizarUsuario(){
		if(validarUsuarioSel()){
			try {
				usuarioService.actualizarUsuario(usuarioSel);
				usuarios = usuarioService.obtenerUsuariosPorIdCliente(clienteSel);
				MensajeGrowl.mostrar("Usuario actualizado exitosamente", FacesMessage.SEVERITY_INFO);
			} catch (Exception e) {
				MensajeGrowl.mostrar("Error al actualizar usuario", FacesMessage.SEVERITY_FATAL);
			}
		}
	}
	
	public void eliminarUsuario(){
		if(validarUsuarioSel()){
			try {
				usuarioService.eliminarUsuarioPorId(usuarioSel.getUsername());
				usuarios = usuarioService.obtenerUsuariosPorIdCliente(clienteSel);
				resetUsuarioSel();
				MensajeGrowl.mostrar("Usuario eliminado exitosamente", FacesMessage.SEVERITY_INFO);
			} catch (Exception e) {
				MensajeGrowl.mostrar("Error al eliminar usuario", FacesMessage.SEVERITY_FATAL);
			}
		}
	}
	
	public void deshabilitarCuentas(){
		try {
			usuarioService.deshabilitarCuentasDeUsuario(clienteSel);
			usuarios = usuarioService.obtenerUsuariosPorIdCliente(clienteSel);
			MensajeGrowl.mostrar("Todas las cuentas han sido deshabilitadas", FacesMessage.SEVERITY_INFO);
		} catch (Exception e) {
			MensajeGrowl.mostrar("Error al deshabilitar cuentas de usuario", FacesMessage.SEVERITY_FATAL);
		}
	}
	
	public void cambioCliente(){
		usuarios = usuarioService.obtenerUsuariosPorIdCliente(clienteSel);
		resetUsuarioSel();
	}
	
	public boolean validarUsuario(){
		if(usuario.getUsername().trim().isEmpty()){
			MensajeGrowl.mostrar("Debe indicar el nombre de usuario", FacesMessage.SEVERITY_ERROR);
			return false;
		}
		if(usuario.getPassword().trim().isEmpty()){
			MensajeGrowl.mostrar("Debe indicar el password", FacesMessage.SEVERITY_ERROR);
			return false;
		}
		if(usuario.getIdCliente() == null){
			MensajeGrowl.mostrar("Debe indicar el cliente", FacesMessage.SEVERITY_ERROR);
			return false;
		}
		if(usuario.getIdTipousuario() == null){
			MensajeGrowl.mostrar("Debe indicar el tipo de usuario", FacesMessage.SEVERITY_ERROR);
			return false;
		}
		return true;
	}
	
	public boolean validarUsuarioSel(){
		if(usuarioSel.getIdCliente() == null){
			MensajeGrowl.mostrar("Debe seleccionar un usuario", FacesMessage.SEVERITY_ERROR);
			return false;
		}
		if(usuarioSel.getUsername().trim().isEmpty()){
			MensajeGrowl.mostrar("Debe indicar el nombre de usuario", FacesMessage.SEVERITY_ERROR);
			return false;
		}
		if(usuarioSel.getPassword().trim().isEmpty()){
			MensajeGrowl.mostrar("Debe indicar el password", FacesMessage.SEVERITY_ERROR);
			return false;
		}
		if(usuarioSel.getIdTipousuario() == null){
			MensajeGrowl.mostrar("Debe indicar el tipo de usuario", FacesMessage.SEVERITY_ERROR);
			return false;
		}
		return true;
	}
	
	private boolean validarCliente(){
		if(cliente.getNombre().trim().isEmpty() || cliente.getLogo().trim().isEmpty()){
			MensajeGrowl.mostrar("Debe escribir el nombre del cliente y de la imagen", FacesMessage.SEVERITY_ERROR);
			return false;
		}
		return true;
	}
	
	private void resetUsuarioSel(){
		usuarioSel = new Usuario();
		usuarioSel.setUsername("");
		usuarioSel.setPassword("");
	}

	public ManejoSesionView getManejoSesionView() {
		return manejoSesionView;
	}

	public void setManejoSesionView(ManejoSesionView manejoSesionView) {
		this.manejoSesionView = manejoSesionView;
	}

	public ClienteService getClienteService() {
		return clienteService;
	}

	public void setClienteService(ClienteService clienteService) {
		this.clienteService = clienteService;
	}

	public UsuarioService getUsuarioService() {
		return usuarioService;
	}

	public void setUsuarioService(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}

	public CatalogoService getCatalogoService() {
		return catalogoService;
	}

	public void setCatalogoService(CatalogoService catalogoService) {
		this.catalogoService = catalogoService;
	}

	public List<Cliente> getClientes() {
		return clientes;
	}

	public void setClientes(List<Cliente> clientes) {
		this.clientes = clientes;
	}

	public List<TipoUsuario> getTiposUsuario() {
		return tiposUsuario;
	}

	public void setTiposUsuario(List<TipoUsuario> tiposUsuario) {
		this.tiposUsuario = tiposUsuario;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public int getClienteSel() {
		return clienteSel;
	}

	public void setClienteSel(int clienteSel) {
		this.clienteSel = clienteSel;
	}

	public List<Usuario> getUsuarios() {
		return usuarios;
	}

	public void setUsuarios(List<Usuario> usuarios) {
		this.usuarios = usuarios;
	}

	public Usuario getUsuarioSel() {
		return usuarioSel;
	}

	public void setUsuarioSel(Usuario usuarioSel) {
		this.usuarioSel = usuarioSel;
	}
	

}
