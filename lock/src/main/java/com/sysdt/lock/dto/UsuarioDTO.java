package com.sysdt.lock.dto;

import com.sysdt.lock.model.Cliente;

public class UsuarioDTO{

	private String username;

    private Boolean enabled;

    private Integer idCliente;

    private Integer idTipousuario;
    
    private Cliente cliente;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public Integer getIdCliente() {
		return idCliente;
	}

	public void setIdCliente(Integer idCliente) {
		this.idCliente = idCliente;
	}

	public Integer getIdTipousuario() {
		return idTipousuario;
	}

	public void setIdTipousuario(Integer idTipousuario) {
		this.idTipousuario = idTipousuario;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}
    
    
    
}
