package com.sysdt.lock.dto;

import java.io.Serializable;

public class SolicitudDTO implements Serializable{

	private static final long serialVersionUID = 4357669810836595990L;
	private int idUnidad;
	private String latitud;
	private String longitud;
	
	public int getIdUnidad() {
		return idUnidad;
	}
	public void setIdUnidad(int idUnidad) {
		this.idUnidad = idUnidad;
	}
	public String getLatitud() {
		return latitud;
	}
	public void setLatitud(String latitud) {
		this.latitud = latitud;
	}
	public String getLongitud() {
		return longitud;
	}
	public void setLongitud(String longitud) {
		this.longitud = longitud;
	}
	
}
