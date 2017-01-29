package com.sysdt.lock.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.context.RequestContext;
import org.springframework.beans.factory.annotation.Autowired;

import com.sun.faces.util.MostlySingletonSet;
import com.sysdt.lock.dto.UserDTO;
import com.sysdt.lock.dto.UsuarioDTO;
import com.sysdt.lock.model.Cliente;
import com.sysdt.lock.model.TipoUsuario;
import com.sysdt.lock.model.Unidad;
import com.sysdt.lock.model.Usuario;
import com.sysdt.lock.service.CatalogoService;
import com.sysdt.lock.service.ClienteService;
import com.sysdt.lock.service.DependenciaService;
import com.sysdt.lock.service.UnidadService;
import com.sysdt.lock.service.UsuarioService;
import com.sysdt.lock.util.Constantes;
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
	@ManagedProperty("#{dependenciaService}")
	private DependenciaService dependenciaService;
	@ManagedProperty("#{unidadService}")
	private UnidadService unidadService;
//	@ManagedProperty("#{userDTO}")
//	private UserDTO userDTO;
	
	private List<Cliente> clientes;
	private List<TipoUsuario> tiposUsuario;
	private List<Usuario> usuarios;
	private Cliente cliente;
	private int idCliente;
	private List<Unidad> unidades;
	private int unidadSel;
	private String eco;
	private Usuario usuario;
	private Usuario usuarioSel;
	private int clienteSel;
	
	//SECCION ASOCIADOS
	private String nuevoAsociado;
	private String asociado;
	private List<String> listaAsociados;
	private List<String> posiblesAsociados;
	private boolean disableAsociado;
	
	@PostConstruct
	public void init(){
		UsuarioDTO userDTO = manejoSesionView.obtenerUsuarioEnSesion();
		if(userDTO == null || userDTO.getIdTipousuario() != Constantes.TipoUsuario.ADMINISTRADOR){
			manejoSesionView.cerrarSesionUsuario();
		}else{
			cliente = new Cliente();
			usuario = new Usuario();
			resetUsuarioSel();
			clientes = clienteService.obtenerClientes();
			tiposUsuario = catalogoService.obtenerTiposUsuario();
			clienteSel = clientes.get(0).getId();
			usuarios = usuarioService.obtenerUsuariosPorIdCliente(clienteSel);
			disableAsociado = true;
			posiblesAsociados = new ArrayList<String>();
			listaAsociados = new ArrayList<String>();
			unidades = new ArrayList<Unidad>();
			idCliente = clientes.size() > 0 ? clientes.get(0).getId():0;
			cargarUnidadesDelCliente();
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
	
	public void cambiarEstatusCuentas(boolean estado){
		String msj = estado?"habilitadas":"deshabilitadas";
		String msj2 = estado?"habilitar":"deshabilitar";
		try {
			usuarioService.cambiarEstadoCuentasDeUsuario(clienteSel, estado);
			usuarios = usuarioService.obtenerUsuariosPorIdCliente(clienteSel);
			MensajeGrowl.mostrar("Todas las cuentas han sido "+msj, FacesMessage.SEVERITY_INFO);
		} catch (Exception e) {
			MensajeGrowl.mostrar("Error al "+msj2+" las cuentas de usuario", FacesMessage.SEVERITY_FATAL);
		}
	}
	
	public void cambioCliente(){
		usuarios = usuarioService.obtenerUsuariosPorIdCliente(clienteSel);
		resetUsuarioSel();
		listaAsociados.clear();
		posiblesAsociados.clear();
		disableAsociado = true;
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
	
	public void agregarAsociado(){
		String nuevoAs = nuevoAsociado;
		boolean repetido = false;
		for(String aso : listaAsociados){
			if(aso.contentEquals(nuevoAs)){
				repetido = true;
				break;
			}
		}
		if(!repetido){
			listaAsociados.add(nuevoAs);
		}else{
			MensajeGrowl.mostrar("El asociado ya se encuentra en la lista", FacesMessage.SEVERITY_ERROR);
		}
		
	}
	
	public void eliminarAsociado(){
		for(String as : listaAsociados){
			if(asociado.contentEquals(as)){
				listaAsociados.remove(as);
				break;
			}
		}
	}
	
	public void guardarListaAsociados(){
		try {
			dependenciaService.guardarDependencias(listaAsociados, usuarioSel.getUsername(), usuarioSel.getIdTipousuario());
			MensajeGrowl.mostrar("Asociados guardados correctamente", FacesMessage.SEVERITY_INFO);
		} catch (Exception e) {
			MensajeGrowl.mostrar("Ocurrió una excepción al guardar asociados", FacesMessage.SEVERITY_FATAL);
		}
	}
	
	public void obtenerDependientes(){
		if(usuarioSel.getIdTipousuario() == Constantes.TipoUsuario.OPERADOR || 
				usuarioSel.getIdTipousuario() == Constantes.TipoUsuario.SUPERVISOR){
			disableAsociado = false;
			posiblesAsociados = usuarioService.obtenerListaDependientesPorTipo(clienteSel, usuarioSel.getIdTipousuario());
			listaAsociados = dependenciaService.obtenerDependenciasPorUsuario(usuarioSel.getUsername(), usuarioSel.getIdTipousuario());
		}else{
			disableAsociado = true;
			posiblesAsociados.clear();
			listaAsociados.clear();
		}
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
	
	public void guardarUnidad(){
		if(idCliente == 0){
			MensajeGrowl.mostrar("Debe seleccionar un cliente", FacesMessage.SEVERITY_WARN);
			return;
		}
		if(eco == null || eco.trim().isEmpty()){
			MensajeGrowl.mostrar("Debe escribir el número económico o las placas de la unidad", FacesMessage.SEVERITY_WARN);
			return;
		}
		Unidad unidad = new Unidad();
		unidad.setId(0);
		unidad.setEco(eco);
		unidad.setIdcliente(idCliente);
		try{
			boolean exito = unidadService.guardarNuevaUnidad(unidad);
			if(exito){
				cargarUnidadesDelCliente();
				MensajeGrowl.mostrar("La unidad fue agregada exitosamente", FacesMessage.SEVERITY_INFO);
			}else{
				MensajeGrowl.mostrar("El cliente ya tiene registrada esa unidad", FacesMessage.SEVERITY_ERROR);
			}
		}catch(Exception e){
			MensajeGrowl.mostrar("Ocurrió un error al guardar la unidad", FacesMessage.SEVERITY_FATAL);
		}
	}
	
	public void actualizarUnidad(){
		if(idCliente == 0){
			MensajeGrowl.mostrar("Debe seleccionar un cliente", FacesMessage.SEVERITY_WARN);
			return;
		}
		if(unidadSel == 0){
			MensajeGrowl.mostrar("Debe seleccionar una unidad", FacesMessage.SEVERITY_WARN);
			return;
		}
		if(unidadSel == Constantes.LISTA_UNIDADES_VACIA){
			MensajeGrowl.mostrar("No hay unidades vinculadas a la cuenta", FacesMessage.SEVERITY_WARN);
			return;
		}
		if(eco == null || eco.trim().isEmpty()){
			MensajeGrowl.mostrar("Debe escribir el nuevo número económico o las placas de la unidad", FacesMessage.SEVERITY_WARN);
			return;
		}
		
		try{
			Unidad unidad = new Unidad();
			unidad.setId(unidadSel);
			unidad.setEco(eco.trim());
			unidad.setIdcliente(idCliente);
			boolean exito = unidadService.ecoDuplicado(unidad);
			if(exito){
				MensajeGrowl.mostrar("El cliente ya tiene registrada esa unidad", FacesMessage.SEVERITY_ERROR);
				return;
			}
			exito = unidadService.actualizarUnidad(unidad);
			if(exito){
				cargarUnidadesDelCliente();
				MensajeGrowl.mostrar("La unidad fue actualizada exitosamente", FacesMessage.SEVERITY_INFO);
			}else{
				MensajeGrowl.mostrar("No se pudo actualizar porque no se encontró el registro de la unidad", FacesMessage.SEVERITY_ERROR);
			}
			
		}catch(Exception e){
			MensajeGrowl.mostrar("Ocurrió un error al guardar la unidad", FacesMessage.SEVERITY_FATAL);
		}
		
	}
	
	public void eliminarUnidad(){
		if(unidadSel == 0){
			MensajeGrowl.mostrar("Debe seleccionar una unidad", FacesMessage.SEVERITY_WARN);
			return;
		}
		if(unidadSel == Constantes.LISTA_UNIDADES_VACIA){
			MensajeGrowl.mostrar("No hay unidades vinculadas a la cuenta", FacesMessage.SEVERITY_WARN);
			return;
		}
		try{
			boolean exito = unidadService.eliminarUnidad(unidadSel);
			if(exito){
				cargarUnidadesDelCliente();
				MensajeGrowl.mostrar("La unidad fue eliminada exitosamente", FacesMessage.SEVERITY_INFO);
			}else{
				MensajeGrowl.mostrar("No se pudo eliminar porque no se encontró el registro de la unidad", FacesMessage.SEVERITY_FATAL);
			}
		}catch(Exception e){
			MensajeGrowl.mostrar("Ocurrió un error al eliminar la unidad", FacesMessage.SEVERITY_FATAL);
		}
	}
	
	public void cargarUnidadesDelCliente(){
		unidades.clear();
		eco = "";
		if(idCliente != 0){
			unidades = unidadService.obtenerUnidadesPorIdCliente(idCliente);
			if(unidades.isEmpty()){
				unidades.add(generarUnidadVacia());
			}
		}
	}
	
	private Unidad generarUnidadVacia(){
		Unidad unidad = new Unidad();
		unidad.setId(Constantes.LISTA_UNIDADES_VACIA);
		unidad.setEco("No hay unidades disponibles");
		return unidad;
	}
	
	private void ocultarLoading(){
		RequestContext.getCurrentInstance().execute("PF('statusDialog').hide();");
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

	public DependenciaService getDependenciaService() {
		return dependenciaService;
	}

	public void setDependenciaService(DependenciaService dependenciaService) {
		this.dependenciaService = dependenciaService;
	}

	public String getNuevoAsociado() {
		return nuevoAsociado;
	}

	public void setNuevoAsociado(String nuevoAsociado) {
		this.nuevoAsociado = nuevoAsociado;
	}

	public List<String> getListaAsociados() {
		return listaAsociados;
	}

	public void setListaAsociados(List<String> listaAsociados) {
		this.listaAsociados = listaAsociados;
	}

	public List<String> getPosiblesAsociados() {
		return posiblesAsociados;
	}

	public void setPosiblesAsociados(List<String> posiblesAsociados) {
		this.posiblesAsociados = posiblesAsociados;
	}

	public String getAsociado() {
		return asociado;
	}

	public void setAsociado(String asociado) {
		this.asociado = asociado;
	}

	public boolean isDisableAsociado() {
		return disableAsociado;
	}

	public void setDisableAsociado(boolean disableAsociado) {
		this.disableAsociado = disableAsociado;
	}

	public UnidadService getUnidadService() {
		return unidadService;
	}

	public void setUnidadService(UnidadService unidadService) {
		this.unidadService = unidadService;
	}

	public List<Unidad> getUnidades() {
		return unidades;
	}

	public void setUnidades(List<Unidad> unidades) {
		this.unidades = unidades;
	}

	public int getIdCliente() {
		return idCliente;
	}

	public void setIdCliente(int idCliente) {
		this.idCliente = idCliente;
	}

	public int getUnidadSel() {
		return unidadSel;
	}

	public void setUnidadSel(int unidadSel) {
		this.unidadSel = unidadSel;
	}

	public String getEco() {
		return eco;
	}

	public void setEco(String eco) {
		this.eco = eco;
	}
	

}
