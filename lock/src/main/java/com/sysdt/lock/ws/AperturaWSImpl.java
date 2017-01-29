package com.sysdt.lock.ws;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sysdt.lock.dto.RespuestaDTO;
import com.sysdt.lock.dto.SolicitudDTO;
import com.sysdt.lock.service.AperturaService;

@Service
public class AperturaWSImpl implements AperturaWS{

	@Autowired
	private AperturaService aperturaService;
	
	@Override
	public RespuestaDTO consultarApertura(SolicitudDTO solicitud) {
		return aperturaService.consultarApertura(solicitud);
	}

	@Override
	public RespuestaDTO registrarToken(int idUnidad, String token) {
		return aperturaService.registrarToken(idUnidad, token);
	}

}
