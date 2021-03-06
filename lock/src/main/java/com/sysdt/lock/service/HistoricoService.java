package com.sysdt.lock.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sysdt.lock.dao.HistoricoMapper;
import com.sysdt.lock.model.Historico;
import com.sysdt.lock.model.HistoricoExample;
import com.sysdt.lock.model.HistoricoExample.Criteria;
import com.sysdt.lock.util.Constantes;

@Service
@Transactional
public class HistoricoService {
	
	private final int FECHA_INICIO = 1;
	private final int FECHA_FIN = 2;
	
	@Autowired
	private HistoricoMapper historicoMapper;
	
	public List<Historico> obtenerHistoricoPorUsuarioFechaYTipo(String username, Date fechaIni, Date fechaFin, int tipo) throws Exception{
		HistoricoExample exHist = new HistoricoExample();
		Criteria criteria = exHist.createCriteria();
		criteria.andUsernameEqualTo(username);
		criteria.andFechaBetween(generarFecha(fechaIni, FECHA_INICIO), generarFecha(fechaFin, FECHA_FIN));
		if(tipo == Constantes.TipoEvento.GENERACION_CODIGO){
			criteria.andLatitudIsNull().andLongitudIsNull();
		}else if(tipo == Constantes.TipoEvento.APERTURA_CHAPA){
			criteria.andLatitudIsNotNull().andLongitudIsNotNull();
		}
		return historicoMapper.selectByExample(exHist);
	}
	
	public void insertarHistorico(String username, String placasEco, boolean estado) throws Exception{
		Historico historico = new Historico();
		historico.setFecha(new Date());
		historico.setUsername(username);
		historico.setPlacasEco(placasEco.toUpperCase());
		historico.setEstado(estado);
		historicoMapper.insert(historico);
	}
	
	public void insertarHistoricoConCoordenadas(Historico historico)throws Exception{
		historicoMapper.insert(historico);
	}
	
	private Date generarFecha(Date fecha, int tipoFecha){
		Calendar cal = Calendar.getInstance();
		cal.setTime(fecha);
		if(tipoFecha == FECHA_INICIO){
			cal.set(Calendar.HOUR, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 1);
		}else if(tipoFecha == FECHA_FIN){
			cal.set(Calendar.HOUR, 23);
			cal.set(Calendar.MINUTE, 59);
			cal.set(Calendar.SECOND, 59);
			cal.set(Calendar.MILLISECOND, 999);
		}
		return cal.getTime();
	}

}
