package com.sysdt.lock.util;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

public class MensajeGrowl {

	public static void mostrar(String mensaje, FacesMessage.Severity tipo){
		FacesContext context = FacesContext.getCurrentInstance();
		context.addMessage(null, new FacesMessage(tipo, mensaje, ""));
	}
	
}
