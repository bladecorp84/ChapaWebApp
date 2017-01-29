package com.sysdt.lock.dto;

import java.io.Serializable;

public class AperturaDTO implements Serializable{

	private static final long serialVersionUID = 5474728287541454227L;
	private String usuario;
	private String eco;
	private long tiempo;
	private boolean isWialon;
	
	public String getUsuario() {
		return usuario;
	}
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	public String getEco() {
		return eco;
	}
	public void setEco(String eco) {
		this.eco = eco;
	}
	public long getTiempo() {
		return tiempo;
	}
	public void setTiempo(long tiempo) {
		this.tiempo = tiempo;
	}
	public boolean isWialon() {
		return isWialon;
	}
	public void setWialon(boolean isWialon) {
		this.isWialon = isWialon;
	}
	
	
	
}
