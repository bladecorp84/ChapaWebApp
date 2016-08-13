package com.sysdt.lock.view;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.context.RequestContext;

import com.sysdt.lock.dto.UsuarioDTO;
import com.sysdt.lock.service.UsuarioService;
import com.sysdt.lock.util.MensajeGrowl;

@ManagedBean
@ViewScoped
public class UserView implements Serializable{

	private static final long serialVersionUID = 1L;

	@ManagedProperty("#{usuarioService}")
	private UsuarioService usuarioService;
	
	@ManagedProperty("#{manejoSesionView}")
	private ManejoSesionView manejoSesionView;
	
	private String clave1;
	private String clave2;
	private String codigo;
	private UsuarioDTO usuarioDTO;
	
	@PostConstruct
	public void init(){
		usuarioDTO = manejoSesionView.obtenerUsuarioEnSesion();
	}
	
	public void generarCodigo(){
		if(validarClaves(clave1, clave2)){
			try {
				codigo = usuarioService.generarCodigo(clave1, clave2, usuarioDTO.getUsername());
				RequestContext.getCurrentInstance().execute("PF('dlg').show();");
			} catch (Exception e) {
				MensajeGrowl.mostrar("Ocurrió un error al generar el código: "+e.getMessage(), FacesMessage.SEVERITY_FATAL);
			}
		}
	}
	
	private boolean validarClaves(String c1, String c2){
		if(c1.trim().isEmpty() || c2.trim().isEmpty()){
			MensajeGrowl.mostrar("Debe ingresar las dos claves", FacesMessage.SEVERITY_ERROR);
			return false;
		}
		return true;
		//ESTE FUE UN CAMBIO
	}
	

	public UsuarioService getUsuarioService() {
		return usuarioService;
	}

	public void setUsuarioService(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}

	public String getClave1() {
		return clave1;
	}

	public void setClave1(String clave1) {
		this.clave1 = clave1;
	}

	public String getClave2() {
		return clave2;
	}

	public void setClave2(String clave2) {
		this.clave2 = clave2;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public ManejoSesionView getManejoSesionView() {
		return manejoSesionView;
	}

	public void setManejoSesionView(ManejoSesionView manejoSesionView) {
		this.manejoSesionView = manejoSesionView;
	}

	public UsuarioDTO getUsuarioDTO() {
		return usuarioDTO;
	}

	public void setUsuarioDTO(UsuarioDTO usuarioDTO) {
		this.usuarioDTO = usuarioDTO;
	}

	
}
