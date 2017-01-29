package com.sysdt.lock.view;

import java.io.IOException;
import java.io.Serializable;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sysdt.lock.dto.UserDTO;
import com.sysdt.lock.dto.UsuarioDTO;
import com.sysdt.lock.service.ClienteService;
import com.sysdt.lock.service.UsuarioService;
import com.sysdt.lock.util.MensajeGrowl;

@ManagedBean
@ViewScoped
public class ManejoSesionView implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@ManagedProperty("#{usuarioService}")
	private UsuarioService usuarioService;
	
//	@ManagedProperty("#{userDTO}")
//	private UserDTO userDTO;
	
	private UsuarioDTO usuarioDTO;
	
	
	@PostConstruct
	public void init(){
		obtenerUsuarioEnSesion();
		
		if(usuarioDTO == null || usuarioDTO.getIdCliente() == null){
			ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
		//	ec.invalidateSession();
			try {
				ec.redirect("login.xhtml");
				return;
			} catch (IOException e) {
				System.out.println("ERROR AL REDIRIGIR USERDTO");
			}
		}
	}

	public UsuarioDTO obtenerUsuarioEnSesion(){
		System.out.println("Entro a obtenerUsuarioEnSesion");
		ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
		if(!ec.isResponseCommitted()){
			try {
				Map<String, Object> session = ec.getSessionMap();
				usuarioDTO = (UsuarioDTO)session.get("usuario");
				if(usuarioDTO == null){
					ec.invalidateSession();
					ec.redirect("login.xhtml");
				}
			} catch (IOException e) {
				MensajeGrowl.mostrar("No se pudo obtener al usuario y ocurrió un error al redirigir", FacesMessage.SEVERITY_FATAL);
			}
		}
		return usuarioDTO;
	}
	
	public void cerrarSesionUsuario(){
		ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
		if(!ec.isResponseCommitted()){
			String client = usuarioDTO.getCliente().getNombre();
			client = !client.contentEquals("SYSDT")?"?c="+client:"";
			ec.invalidateSession();
			try {
				MensajeGrowl.mostrar("La sesión se cerró exitosamente", FacesMessage.SEVERITY_INFO);
				ec.getFlash().setKeepMessages(true);
				String url = "login.xhtml"+client;
				ec.redirect(url);
			} catch (IOException e) {
				MensajeGrowl.mostrar("Ocurrió un error al redirigir al Login", FacesMessage.SEVERITY_FATAL);
			}
		}
	}
	
	public void redirigir(int idRedirect){
		ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
		try {
			if(idRedirect == 1){
				context.redirect("codigos.xhtml");	
			}else if(idRedirect == 2){
				context.redirect("supervision.xhtml");
			}else if(idRedirect == 3){
				context.redirect("admin.xhtml");
			}else{
				context.redirect("login.xhtml");
			}
		} catch (IOException e) {
			MensajeGrowl.mostrar("Error al redirigir", FacesMessage.SEVERITY_FATAL);
		}
	}

	public UsuarioService getUsuarioService() {
		return usuarioService;
	}

	public void setUsuarioService(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}

	public UsuarioDTO getUsuarioDTO() {
		return usuarioDTO;
	}

	public void setUsuarioDTO(UsuarioDTO usuarioDTO) {
		this.usuarioDTO = usuarioDTO;
	}
	
}
