package com.sysdt.lock.dto;

public class RespuestaDTO {

	private boolean autorizacion;
	private String mensaje;
	
	public String getMensaje() {
		return mensaje;
	}
	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}
	public boolean isAutorizacion() {
		return autorizacion;
	}
	public void setAutorizacion(boolean autorizacion) {
		this.autorizacion = autorizacion;
	}
	
}
