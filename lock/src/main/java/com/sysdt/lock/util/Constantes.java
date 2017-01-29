package com.sysdt.lock.util;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;

import com.sysdt.lock.dto.PushBotsDTO;
import com.sysdt.lock.service.AperturaService;


public class Constantes {
	
	public static final int LISTA_UNIDADES_VACIA = -1;
	public static final int TIEMPO_MAXIMO_ESPERA_EN_SEGUNDOS = 30;
	
	public class TipoUsuario {
		public static final int ADMINISTRADOR = 1;
		public static final int SUPERVISOR = 2;
		public static final int OPERADOR = 3;
		public static final int MASTER = 4;
	}
	
	public class TipoEvento {
		public static final int TODO = 0;
		public static final int GENERACION_CODIGO = 1;
		public static final int APERTURA_CHAPA = 2;
	}
	
	public class Coordenadas {
		public static final String LATITUD_INICIAL = "19.432700";
		public static final String LONGITUD_INICIAL = "-99.133386";
	}
	
	

}
