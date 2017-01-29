package com.sysdt.lock.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.concurrent.ConcurrentSkipListMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Document;

import com.sysdt.lock.dto.AperturaDTO;
import com.sysdt.lock.dto.PushBotsDTO;
import com.sysdt.lock.dto.RespuestaDTO;
import com.sysdt.lock.dto.SolicitudDTO;
import com.sysdt.lock.model.Historico;
import com.sysdt.lock.model.Unidad;
import com.sysdt.lock.util.Constantes;
import com.sysdt.lock.ws.AperturaWS;

@Service
@Transactional
public class AperturaService{

	private ConcurrentSkipListMap<Integer, AperturaDTO> aperturas = new ConcurrentSkipListMap<Integer, AperturaDTO>();
	public static final PushBotsDTO PUSHBOTS = obtenerParametrosPushBots();
	
	@Autowired
	private HistoricoService historicoService;
	
	@Autowired
	private UnidadService unidadService;
	
	public void enviarSolicitudDeApertura(Unidad unidad, String usuario, boolean isWialon)throws Exception{
		int resp = EnviarHttpPush(unidad.getToken(), unidad.getId());
		if(resp != HttpStatus.SC_OK){
			throw new Exception("Error al enviar mensaje. HTTP CODE: "+resp);
		}
		agregarAperturaAlistaDeEspera(unidad.getId(), unidad.getEco(), usuario, isWialon);
	}
	
	private void agregarAperturaAlistaDeEspera(int idUnidad, String eco, String usuario, boolean isWialon)throws Exception{
		AperturaDTO apertura = new AperturaDTO();
		apertura.setUsuario(usuario);
		apertura.setEco(eco);
		apertura.setWialon(isWialon);
		apertura.setTiempo(System.currentTimeMillis());
		aperturas.put(idUnidad, apertura);
	}
	
	private int EnviarHttpPush(String token, int idUnidad)throws Exception{
		String url = "https://api.pushbots.com/push/one";
		HttpClient client = HttpClientBuilder.create().build();
		HttpPost post = new HttpPost(url);
		post.setHeader("x-pushbots-appid", PUSHBOTS.getAppId());
		post.setHeader("x-pushbots-secret", PUSHBOTS.getAppSecret());
		StringEntity entity = new StringEntity("{\"platform\":\"1\","
												+ "\"token\":\""+token+"\","
												+ "\"msg\":\""+PUSHBOTS.getMensaje()+"\","
												+ "\"payload\":{\"idUnidad\":\""+idUnidad+"\"},"
												+ "\"sound\":\"\"}");   
		entity.setContentType("application/json");
		post.setEntity(entity);
		HttpResponse response = client.execute(post);
		return response.getStatusLine().getStatusCode(); 
	}
	
	public RespuestaDTO registrarToken(int idUnidad, String token) {
		RespuestaDTO respuesta = new RespuestaDTO();
		respuesta.setAutorizacion(false);
		try{
			if(idUnidad == 0){
				respuesta.setMensaje("El id de la unidad llego vacio");
				return respuesta;
			}
			if(token == null || token.trim().isEmpty()){
				respuesta.setMensaje("El token llego vacio");
				return respuesta;
			}
			
			boolean exito = unidadService.actualizarToken(idUnidad, token);
			if(exito){
				respuesta.setAutorizacion(true);
				respuesta.setMensaje("Token actualizado correctamente");
			}else{
				respuesta.setMensaje("No se encontro el registro de la unidad");
			}
		}catch(Exception e){
			respuesta.setMensaje("Ocurrio una excepcion: "+e.getMessage());
		}
		return respuesta;
	}

	public RespuestaDTO consultarApertura(SolicitudDTO solicitud) {
		RespuestaDTO respuesta = new RespuestaDTO();
		respuesta.setAutorizacion(false);
		try{
			AperturaDTO aperturaDTO = aperturas.get(solicitud.getIdUnidad());
			if(aperturaDTO == null){
				respuesta.setMensaje("La unidad no tiene aperturas pendientes");
				return respuesta;
			}
			
			if(!validarSolicitudApertura(solicitud, respuesta)){
				return respuesta;
			}
			
			long tiempoActual = System.currentTimeMillis();
			if( ((tiempoActual - aperturaDTO.getTiempo())/1000) >  Constantes.TIEMPO_MAXIMO_ESPERA_EN_SEGUNDOS){
				aperturas.remove(solicitud.getIdUnidad());
				respuesta.setMensaje("Tiempo de espera agotado");
				return respuesta;
			} 
		
			aperturas.remove(solicitud.getIdUnidad());
			if(!aperturaDTO.isWialon()){
				Historico historico = new Historico();
				historico.setUsername(aperturaDTO.getUsuario());
				historico.setPlacasEco(aperturaDTO.getEco());
				historico.setFecha(new Date());
				historico.setEstado(true);
				historico.setLatitud(solicitud.getLatitud());
				historico.setLongitud(solicitud.getLongitud());
				historicoService.insertarHistoricoConCoordenadas(historico);
			}
			
			respuesta.setAutorizacion(true);
			respuesta.setMensaje("Se autoriza apertura de chapa");
		}catch(Exception e){
			respuesta.setMensaje("Ocurrio una excepcion: "+e.getMessage());
		}
		return respuesta;
	}
	
	private boolean validarSolicitudApertura(SolicitudDTO solicitud, RespuestaDTO respuesta){
		
		if(solicitud.getIdUnidad() == 0){
			respuesta.setMensaje("La unidad es invalida. Valor: 0");
			return false;
		}
		
		if(solicitud.getLatitud() == null || solicitud.getLatitud().trim().isEmpty()){
			respuesta.setMensaje("La latitud llego vacia");
			return false;
		}
		
		try{
			BigDecimal lat = new BigDecimal(solicitud.getLatitud());
		}catch(NumberFormatException e){
			respuesta.setMensaje("La valor de latitud es invalido");
			return false;
		}
		
		if(solicitud.getLongitud() == null || solicitud.getLongitud().trim().isEmpty()){
			respuesta.setMensaje("La longitud llego vacia");
			return false;
		}
		
		try{
			BigDecimal lon = new BigDecimal(solicitud.getLongitud());
		}catch(NumberFormatException e){
			respuesta.setMensaje("La valor de longitud es invalido");
			return false;
		}
		
		return true;
	}

	private static PushBotsDTO obtenerParametrosPushBots(){
		PushBotsDTO pushBotsDTO = new PushBotsDTO();
		try{
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = factory.newDocumentBuilder();
			Document document = documentBuilder.parse(AperturaService.class.getClassLoader().getResourceAsStream("parametrosPushBots.xml"));
			pushBotsDTO.setAppId(document.getElementsByTagName("AppId").item(0).getTextContent());
			pushBotsDTO.setAppSecret(document.getElementsByTagName("AppSecret").item(0).getTextContent());
			pushBotsDTO.setMensaje(document.getElementsByTagName("Mensaje").item(0).getTextContent());
		}catch(Exception e){
			System.out.println("*** ERROR AL CARGAR VARIABLES DE PUSHBOTS *** : "+e.getMessage());
			pushBotsDTO.setAppId("");
			pushBotsDTO.setAppSecret("");
			pushBotsDTO.setMensaje("");
		}
		return pushBotsDTO;
	}
	
}
