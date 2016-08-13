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

@Service
@Transactional
public class HistoricoService {
	
	private static final int FECHA_INICIO = 1;
	private static final int FECHA_FIN = 2;
	
	@Autowired
	private HistoricoMapper historicoMapper;
	
	public List<Historico> obtenerHistoricoPorUsuarioYFecha(String username, int mes, int anio) throws Exception{
		HistoricoExample exHist = new HistoricoExample();
		exHist.createCriteria().andUsernameEqualTo(username)
			.andFechaBetween(generarFecha(mes, anio, FECHA_INICIO), generarFecha(mes, anio, FECHA_FIN));
		return historicoMapper.selectByExample(exHist);
	}
	
	public void insertarHistorico(String username) throws Exception{
		Historico historico = new Historico();
		historico.setFecha(new Date());
		historico.setUsername(username);
		historicoMapper.insert(historico);
	}
	
	private Date generarFecha(int mes, int anio, int tipoFecha){
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, anio);
		cal.set(Calendar.MONTH, mes-1);
		if(tipoFecha == FECHA_INICIO){
			cal.set(Calendar.DAY_OF_MONTH, 1);
			cal.set(Calendar.HOUR, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 1);
		}else{
			cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
			cal.set(Calendar.HOUR, 23);
			cal.set(Calendar.MINUTE, 59);
			cal.set(Calendar.SECOND, 59);
		}
		return cal.getTime();
	}

}
