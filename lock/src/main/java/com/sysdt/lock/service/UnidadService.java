package com.sysdt.lock.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sysdt.lock.dao.UnidadMapper;
import com.sysdt.lock.model.Unidad;
import com.sysdt.lock.model.UnidadExample;

@Service
@Transactional
public class UnidadService {

	@Autowired
	private UnidadMapper unidadMapper;
	
	public List<Unidad> obtenerUnidadesPorIdCliente(int idCliente){
		UnidadExample exUnit = new UnidadExample();
		exUnit.createCriteria().andIdclienteEqualTo(idCliente);
		exUnit.setOrderByClause("eco ASC");
		return unidadMapper.selectByExample(exUnit);
	}
	
	public Unidad obtenerUnidadPorEcoYidCliente(String eco, int idCliente){
		UnidadExample exUnit = new UnidadExample();
		exUnit.createCriteria().andEcoEqualTo(eco).andIdclienteEqualTo(idCliente);
		 List<Unidad> unidades = unidadMapper.selectByExample(exUnit);
		 return unidades.size() > 0 ? unidades.get(0):null;
	}
	
	public boolean guardarNuevaUnidad(Unidad unidad){
		boolean ecoDuplicado = ecoDuplicado(unidad);
		if(!ecoDuplicado){
			return insertarUnidad(unidad);
		}
		return false;
	}
	
	private boolean insertarUnidad(Unidad unidad){
		unidad.setEco(unidad.getEco().trim().toUpperCase());
		unidadMapper.insert(unidad);
		return true;
	}
	
	public boolean actualizarUnidad(Unidad unidad){
		unidad.setEco(unidad.getEco().trim().toUpperCase());
		return unidadMapper.updateByPrimaryKeySelective(unidad) > 0;
	}
	
	public boolean actualizarToken(int idUnidad, String token){
		Unidad unidad = new Unidad();
		unidad.setId(idUnidad);
		unidad.setToken(token);
		return unidadMapper.updateByPrimaryKeySelective(unidad) == 1;
	}
	
	public boolean eliminarUnidad(int idUnidad){
		return unidadMapper.deleteByPrimaryKey(idUnidad) > 0;
	}
	
	public boolean ecoDuplicado(Unidad unidad){
		Unidad unit = obtenerUnidadPorEcoYidCliente(unidad.getEco(), unidad.getIdcliente());
		return unit != null;
	}
	
}
