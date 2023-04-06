package com.imss.sivimss.donaciones.beans;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.DatatypeConverter;

import com.imss.sivimss.donaciones.util.SelectQueryUtil;
import com.imss.sivimss.donaciones.model.request.ConsultaDonadoRequest;
import com.imss.sivimss.donaciones.util.AppConstantes;
import com.imss.sivimss.donaciones.util.DatosRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
public class ConsultaDonado {

	private Integer idInventario;
	private String velatorio;
	private String tipo;
	private String modeloAtaud;
	private String numInventario;
	private Date fecDonacion;
	private String donadoPor;
	private String nomDonador ;
	private Integer idVelatorio; 
	private Integer idDelegacion; 
	private String fechaInicio;
	private String fechaFin;

	private Integer desDelegacion;

	public ConsultaDonado(ConsultaDonadoRequest consultaDonadoRequest) {
		this.idInventario = consultaDonadoRequest.getIdInventario();
		this.velatorio = consultaDonadoRequest.getVelatorio();
		this.tipo = consultaDonadoRequest.getTipo();
		this.modeloAtaud = consultaDonadoRequest.getModeloAtaud();
		this.numInventario = consultaDonadoRequest.getNumInventario();
		this.fecDonacion = consultaDonadoRequest.getFecDonacion();
		this.donadoPor = consultaDonadoRequest.getDonadoPor();
		this.nomDonador = consultaDonadoRequest.getNomDonador();
		this.idVelatorio = consultaDonadoRequest.getIdVelatorio(); 
		this.idDelegacion = consultaDonadoRequest.getIdDelegacion(); 
		this.fechaInicio = consultaDonadoRequest.getFechaInicio();
		this.fechaFin = consultaDonadoRequest.getFechaFin();
		this.desDelegacion = consultaDonadoRequest.getDesDelegacion();
	}

	public DatosRequest consultaDonado(DatosRequest request) {

        SelectQueryUtil queryUtil = new SelectQueryUtil();
        queryUtil.select("si.ID_INVENTARIO AS idInventario", "sv.NOM_VELATORIO AS velatorio","stm.DES_TIPO_MATERIAL AS tipo"
        		,"sa.DES_MODELO_ARTICULO AS modeloAtaud", "si.NUM_INVENTARIO AS numInventario","ssd.FEC_SOLICITUD AS fecDonacion"
        		,"ssd.NOMBRE_INSTITUCION AS donadoPor","sp.NOM_PERSONA AS nomDonador")
        .from("svc_inventario si")
        .innerJoin("svt_articulo sa", "sa.ID_ARTICULO = si.ID_ARTICULO")
        .innerJoin("svc_velatorio sv", "sv.ID_VELATORIO = si.ID_VELATORIO")
        .innerJoin("svc_tipo_material stm", "stm.ID_TIPO_MATERIAL = sa.ID_TIPO_MATERIAL")
        .innerJoin("svc_salida_donacion_ataudes ssda","ssda.ID_ARTICULO = sa.ID_ARTICULO")
		.innerJoin("svc_salida_donacion ssd","ssd.ID_SALIDA_DONACION  = ssda.ID_SALIDA_DONACION")
		.innerJoin("svc_contratante sc","sc.ID_CONTRATANTE = ssd.ID_CONTRATANTE")
		.innerJoin("svc_persona sp","sp.ID_PERSONA = sc.ID_PERSONA ");
        final String query = queryUtil.build();
		String encoded = DatatypeConverter.printBase64Binary(query.getBytes());
		request.getDatos().put(AppConstantes.QUERY, encoded);

		return request;
	}

	public DatosRequest consultarFiltroDonados(DatosRequest request)  {
        SelectQueryUtil queryUtil = new SelectQueryUtil();
		queryUtil.select("si.ID_INVENTARIO AS idInventario"," sv.NOM_VELATORIO AS velatorio","stm.DES_TIPO_MATERIAL AS tipo"
	    		," sa.DES_MODELO_ARTICULO AS modeloAtaud "," si.NUM_INVENTARIO AS numInventario "
	    		," ssd.FEC_SOLICITUD fecDonacion "," ssd.NOMBRE_INSTITUCION AS donadoPor"," sp.NOM_PERSONA AS nomDonador")
				.from("svc_inventario si ")
				.innerJoin("svt_articulo sa","sa.ID_ARTICULO = si.ID_ARTICULO")
				.innerJoin("svc_velatorio sv","sv.ID_VELATORIO = si.ID_VELATORIO")
				.innerJoin("svc_tipo_material stm","stm.ID_TIPO_MATERIAL = sa.ID_TIPO_MATERIAL")
				.innerJoin("svc_salida_donacion_ataudes ssda","ssda.ID_ARTICULO = sa.ID_ARTICULO")
				.innerJoin("svc_salida_donacion ssd","ssd.ID_SALIDA_DONACION  = ssda.ID_SALIDA_DONACION")
				.innerJoin("svc_contratante sc","sc.ID_CONTRATANTE = ssd.ID_CONTRATANTE")
				.innerJoin("svc_persona sp","sp.ID_PERSONA = sc.ID_PERSONA")
				.innerJoin("svc_delegacion sd","sd.ID_DELEGACION = sv.ID_DELEGACION")
				.where("si.ID_VELATORIO = :idVel")
				.setParameter("idVel", this.idVelatorio)
				.and("sv.ID_DELEGACION = :idDel")
				.setParameter("idDel", this.idDelegacion) 
				.and("ssd.FEC_SOLICITUD >= :fecIni")
				.setParameter("fecIni", this.fechaInicio)
				.and("ssd.FEC_SOLICITUD <= :fecFin")
				.setParameter("fecFin",this.fechaFin);
        final String query = queryUtil.build();
		String encoded = DatatypeConverter.printBase64Binary(query.getBytes());
		request.getDatos().put(AppConstantes.QUERY, encoded);

		return request;
	}
	public DatosRequest obtenerDelegaciones(DatosRequest request) {

        SelectQueryUtil queryUtil = new SelectQueryUtil();
		queryUtil.select("sd.ID_DELEGACION, sd.DES_DELEGACION").from("svc_delegacion sd");
        final String query = queryUtil.build();
		String encoded = DatatypeConverter.printBase64Binary(query.getBytes());
		request.getDatos().put(AppConstantes.QUERY, encoded);
		return request;
	}

	public DatosRequest obtenerVelatorio(DatosRequest request) {

        SelectQueryUtil queryUtil = new SelectQueryUtil();
        queryUtil.select("sv.ID_VELATORIO","sv.NOM_VELATORIO").from("svc_velatorio sv");
        final String query = queryUtil.build();
		String encoded = DatatypeConverter.printBase64Binary(query.getBytes());
		request.getDatos().put(AppConstantes.QUERY, encoded);
		return request;
	}

	public DatosRequest obtenerNivel(DatosRequest request) {

        SelectQueryUtil queryUtil = new SelectQueryUtil();
        queryUtil.select("sno.ID_OFICINA","sno.DES_NIVELOFICINA")
        .from("svc_nivel_oficina sno ");
        final String query = queryUtil.build();
		String encoded = DatatypeConverter.printBase64Binary(query.getBytes());
		request.getDatos().put(AppConstantes.QUERY, encoded);
		return request;
	}
}
