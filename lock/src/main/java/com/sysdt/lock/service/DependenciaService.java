package com.sysdt.lock.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sysdt.lock.dao.DependenciaMapper;
import com.sysdt.lock.model.Dependencia;
import com.sysdt.lock.model.DependenciaExample;
import com.sysdt.lock.model.DependenciaExample.Criteria;
import com.sysdt.lock.util.Constantes;

@Service
@Transactional
public class DependenciaService {

	@Autowired
	private DependenciaMapper dependenciaMapper;
	
	public void guardarDependencias(List<String> asociados, String username, int idTipoUsuario)throws Exception{
		eliminarDependencias(username, idTipoUsuario);
		if(idTipoUsuario == Constantes.TipoUsuario.SUPERVISOR){
			for(String asociado : asociados){
				Dependencia dependencia = new Dependencia();
				dependencia.setEncargado(username);
				dependencia.setDependiente(asociado);
				dependenciaMapper.insert(dependencia);
			}
		}else if(idTipoUsuario == Constantes.TipoUsuario.OPERADOR){
			for(String asociado : asociados){
				Dependencia dependencia = new Dependencia();
				dependencia.setEncargado(asociado);
				dependencia.setDependiente(username);
				dependenciaMapper.insert(dependencia);
			}
		}
	}
	
	private void eliminarDependencias(String username, int idTipoUsuario){
		DependenciaExample exDep = new DependenciaExample();
		Criteria criteria = exDep.createCriteria();
		if(idTipoUsuario == Constantes.TipoUsuario.SUPERVISOR){
			criteria.andEncargadoEqualTo(username);
		}else if(idTipoUsuario == Constantes.TipoUsuario.OPERADOR){
			criteria.andDependienteEqualTo(username);
		}
		dependenciaMapper.deleteByExample(exDep);
	}
	
	public List<String> obtenerDependenciasPorUsuario(String username, int idTipoUsuario){
		DependenciaExample exDep = new DependenciaExample();
		Criteria criteria = exDep.createCriteria();
		if(idTipoUsuario == Constantes.TipoUsuario.SUPERVISOR){
			criteria.andEncargadoEqualTo(username);
		}else if(idTipoUsuario == Constantes.TipoUsuario.OPERADOR){
			criteria.andDependienteEqualTo(username);
		}
		List<Dependencia> dependencias = dependenciaMapper.selectByExample(exDep);
		return convertirAlistaString(dependencias, idTipoUsuario);
	}
	
	private List<String> convertirAlistaString(List<Dependencia> dependencias, int idTipoUsuario){
		List<String> deps = new ArrayList<String>();
		if(idTipoUsuario == Constantes.TipoUsuario.SUPERVISOR){
			for (Dependencia d : dependencias) {
				deps.add(d.getDependiente());
			}
		}else if(idTipoUsuario == Constantes.TipoUsuario.OPERADOR){
			for (Dependencia d : dependencias) {
				deps.add(d.getEncargado());
			}
		}
		return deps;
	}
	
}
