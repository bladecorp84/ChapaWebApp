package com.sysdt.lock.view;

import java.awt.Color;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import com.lowagie.text.Document;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.sysdt.lock.dto.UsuarioDTO;
import com.sysdt.lock.enums.TipoUsuarioEnum;
import com.sysdt.lock.model.Historico;
import com.sysdt.lock.model.Usuario;
import com.sysdt.lock.service.HistoricoService;
import com.sysdt.lock.service.UsuarioService;
import com.sysdt.lock.util.MensajeGrowl;

@ManagedBean
@ViewScoped
public class SuperView implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@ManagedProperty("#{manejoSesionView}")
	private ManejoSesionView manejoSesionView;
	@ManagedProperty("#{usuarioService}")
	private UsuarioService usuarioService;
	@ManagedProperty("#{historicoService}")
	private HistoricoService historicoService;
	
	private List<Usuario> usuarios;
	private List<Historico> historicos;
	private Usuario usuario;
	private UsuarioDTO usuarioDTO;
	
	private Map<Integer, String> meses; 
	private List<Integer> anios;
	private int aniosSel;
	private int mesSel;
	private int registros;
	private String titulo;
	
	@PostConstruct
	public void init(){
		usuarioDTO = manejoSesionView.obtenerUsuarioEnSesion();
		if(usuarioDTO == null || usuarioDTO.getIdTipousuario() == TipoUsuarioEnum.USUARIO.getId()){
			manejoSesionView.cerrarSesionUsuario();
		}else{
			generarMesesYanios();
			usuarios = usuarioService.obtenerUsuariosPorIdClienteSinPass(usuarioDTO.getIdCliente());
			titulo = "";
			historicos = new ArrayList<Historico>();
		}
		
	}
	
	public void buscarHistorico(){
		if(usuario != null && usuario.getUsername() != null){
			historicos = historicoService.obtenerHistoricoPorUsuarioYFecha(usuario.getUsername(), mesSel, aniosSel);
			registros = historicos.size();
			titulo = meses.get(mesSel)+" "+aniosSel;
			if(registros == 0){
				MensajeGrowl.mostrar("No se encontraron registros en esa fecha", FacesMessage.SEVERITY_WARN);
			}
		}else{
			MensajeGrowl.mostrar("Primero debe seleccionar una cuenta de usuario", FacesMessage.SEVERITY_ERROR);
		}
	}
	
	public void cambioUsuario(){
		historicos.clear();
	}
	
	public void procesarPDF(Object documento){
		try{
			Document pdf = (Document)documento;
			pdf.open();
			pdf.setPageSize(PageSize.LETTER);
			pdf.add(new Paragraph("REPORTE "+meses.get(mesSel)+" "+aniosSel, FontFactory.getFont(FontFactory.TIMES_BOLD,16,Color.RED)));
			pdf.add(new Paragraph("."));
		}catch(Exception ex){
			MensajeGrowl.mostrar("Error al generar el reporte", FacesMessage.SEVERITY_FATAL);
		}
	}
	
	public String convertirFecha(Date fecha){
		Calendar cal = Calendar.getInstance();
		cal.setTime(fecha);
		int day = cal.get(Calendar.DAY_OF_MONTH);
		int month = cal.get(Calendar.MONTH)+1;
		return (day<10?"0"+day:day)+" / "+ (month<10?"0"+month:month) +" / "+cal.get(Calendar.YEAR);
	}
	
	public String convertirHora(Date fecha){
		Calendar cal = Calendar.getInstance();
		cal.setTime(fecha);
		int min = cal.get(Calendar.MINUTE);
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		int second = cal.get(Calendar.SECOND);
		return (hour<10?"0"+hour:hour)+":"+ (min<10?"0"+min:min)+ ":"+(second<10?"0"+second:second);
	}
	
	private void generarMesesYanios(){
		generarMeses();
		generarAnios();
	}
	
	private void generarMeses(){
		meses = new LinkedHashMap<Integer, String>();
		meses.put(1, "ENERO");meses.put(2, "FEBRERO");
		meses.put(3, "MARZO");meses.put(4, "ABRIL");
		meses.put(5, "MAYO");meses.put(6, "JUNIO");
		meses.put(7, "JULIO");meses.put(8, "AGOSTO");
		meses.put(9, "SEPTIEMBRE");meses.put(10, "OCTUBRE");
		meses.put(11, "NOVIEMBRE");meses.put(12, "DICIEMBRE");
		Calendar cal = Calendar.getInstance();
		mesSel = cal.get(Calendar.MONTH)+1;
	}
	
	private void generarAnios(){
		Calendar cal = Calendar.getInstance();
		int anioActual = cal.get(Calendar.YEAR);
		anios = new ArrayList<Integer>();
		anios.add(anioActual);
		anios.add(--anioActual);
	}
	
	public String nombreArchivo(){
		return "Codigos_"+meses.get(mesSel)+"_"+aniosSel+"_"+usuario.getUsername();
	}

	public ManejoSesionView getManejoSesionView() {
		return manejoSesionView;
	}

	public void setManejoSesionView(ManejoSesionView manejoSesionView) {
		this.manejoSesionView = manejoSesionView;
	}

	public UsuarioService getUsuarioService() {
		return usuarioService;
	}

	public void setUsuarioService(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}

	public HistoricoService getHistoricoService() {
		return historicoService;
	}

	public void setHistoricoService(HistoricoService historicoService) {
		this.historicoService = historicoService;
	}

	public List<Usuario> getUsuarios() {
		return usuarios;
	}

	public void setUsuarios(List<Usuario> usuarios) {
		this.usuarios = usuarios;
	}

	public List<Historico> getHistoricos() {
		return historicos;
	}

	public void setHistoricos(List<Historico> historicos) {
		this.historicos = historicos;
	}

	public UsuarioDTO getUsuarioDTO() {
		return usuarioDTO;
	}

	public void setUsuarioDTO(UsuarioDTO usuarioDTO) {
		this.usuarioDTO = usuarioDTO;
	}

	public Map<Integer, String> getMeses() {
		return meses;
	}

	public void setMeses(Map<Integer, String> meses) {
		this.meses = meses;
	}

	public List<Integer> getAnios() {
		return anios;
	}

	public void setAnios(List<Integer> anios) {
		this.anios = anios;
	}

	public int getAniosSel() {
		return aniosSel;
	}

	public void setAniosSel(int aniosSel) {
		this.aniosSel = aniosSel;
	}

	public int getMesSel() {
		return mesSel;
	}

	public void setMesSel(int mesSel) {
		this.mesSel = mesSel;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public int getRegistros() {
		return registros;
	}

	public void setRegistros(int registros) {
		this.registros = registros;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

}
