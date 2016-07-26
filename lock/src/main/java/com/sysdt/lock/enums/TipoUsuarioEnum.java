package com.sysdt.lock.enums;

public enum TipoUsuarioEnum {

	ADMINISTRADOR(1),
	SUPERVISOR(2),
	USUARIO(3);
	
	private int id;

	private TipoUsuarioEnum(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	
}
