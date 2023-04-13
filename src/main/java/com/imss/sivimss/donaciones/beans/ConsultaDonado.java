package com.imss.sivimss.donaciones.beans;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.DatatypeConverter;

import com.imss.sivimss.donaciones.util.SelectQueryUtil;
import com.imss.sivimss.donaciones.model.request.ConsultaDonadoRequest;
import com.imss.sivimss.donaciones.model.request.ReporteDto;
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
	private String nomDonador;
	private Integer idVelatorio;
	private Integer idDelegacion;
	private String fechaInicio;
	private String fechaFin;
	private Integer desDelegacion;
	private String formatoFecha;
	
	private static final String CAMPO_ID_INVENTARIO = "si.ID_INVENTARIO AS idInventario";
	private static final String CAMPO_NOMBRE_VELATORIO = "sv.NOM_VELATORIO AS velatorio";
	private static final String CAMPO_TIPO = "stm.DES_TIPO_MATERIAL AS tipo";
	private static final String CAMPO_MODELO_ATAUD = "sa.DES_MODELO_ARTICULO AS modeloAtaud";
	private static final String CAMPO_NUMERO_INVENTARIO = "si.NUM_INVENTARIO AS numInventario";
	private static final String CAMPO_FECHA_DONACION_SALIDA = "date_format(ssd.FEC_SOLICITUD,'"; 
	private static final String CAMPO_FECHA_DONACION_SALIDA_COMP = "')  AS fecDonacion";
	private static final String CAMPO_DONADO_POR_SALIDA = " 'ODS' AS donadoPor";
	private static final String CAMPO_NOMBRE_DONADOR = " sp.NOM_PERSONA AS nomDonador";
	private static final String CAMPO_FECHA_DONACION_ENTRADA = "date_format(sd.FEC_ALTA ,'" ;
	private static final String CAMPO_FECHA_DONACION_ENTRADA_COMP = "')  AS fecDonacion";
	private static final String CAMPO_DONADO_POR_ENTRADA = "'Instituto' AS donadoPor";
	private static final String TABLA_SVC_VELATORIO_SV = "svc_velatorio sv";
	private static final String TABLA_SVC_DELEGACION_SD = "svc_delegacion sd";
	private static final String PARAM_SV_DELEGACION_IDDEL = "sv.ID_DELEGACION = :idDel";
	private static final String PARAM_FECHA_INICIO = "fecIni";
	private static final String PARAM_FECHA_FIN = "fecFin";
	private static final String PARAM_IDVELATORIO = "idVel";
	private static final String PARAM_IDDELEGACION = "idDel";
	
	
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

	public DatosRequest consultarFiltroDonadosSalida(DatosRequest request, String formatoFecha) {
		SelectQueryUtil queryUtil = new SelectQueryUtil();
		queryUtil
				.select(CAMPO_ID_INVENTARIO, CAMPO_NOMBRE_VELATORIO, CAMPO_TIPO, CAMPO_MODELO_ATAUD, CAMPO_NUMERO_INVENTARIO
						, CAMPO_FECHA_DONACION_SALIDA + formatoFecha + CAMPO_FECHA_DONACION_SALIDA_COMP, CAMPO_DONADO_POR_SALIDA, CAMPO_NOMBRE_DONADOR)
				.from("svc_inventario si ").innerJoin("svt_articulo sa", "sa.ID_ARTICULO = si.ID_ARTICULO")
				.innerJoin(TABLA_SVC_VELATORIO_SV, "sv.ID_VELATORIO = si.ID_VELATORIO")
				.innerJoin("svc_tipo_material stm", "stm.ID_TIPO_MATERIAL = sa.ID_TIPO_MATERIAL")
				.innerJoin("svc_salida_donacion_ataudes ssda", "ssda.ID_ARTICULO = sa.ID_ARTICULO")
				.innerJoin("svc_salida_donacion ssd", "ssd.ID_SALIDA_DONACION  = ssda.ID_SALIDA_DONACION")
				.innerJoin("svc_contratante sc", "sc.ID_CONTRATANTE = ssd.ID_CONTRATANTE")
				.innerJoin("svc_persona sp", "sp.ID_PERSONA = sc.ID_PERSONA")
				.innerJoin(TABLA_SVC_DELEGACION_SD, "sd.ID_DELEGACION = sv.ID_DELEGACION").where("si.ID_VELATORIO = :idVel")
				.setParameter(PARAM_IDVELATORIO, this.idVelatorio).and(PARAM_SV_DELEGACION_IDDEL)
				.setParameter(PARAM_IDDELEGACION, this.idDelegacion);
		if (this.fechaInicio != null && this.fechaFin != null) {
			queryUtil.and("ssd.FEC_SOLICITUD >= :fecIni").setParameter(PARAM_FECHA_INICIO, this.fechaInicio)
					.and("ssd.FEC_SOLICITUD <= :fecFin").setParameter(PARAM_FECHA_FIN, this.fechaFin);
		}
		final String query = queryUtil.build();
		String encoded = DatatypeConverter.printBase64Binary(query.getBytes());
		request.getDatos().put(AppConstantes.QUERY, encoded);

		return request;
	}

	public DatosRequest obtenerDelegaciones(DatosRequest request) {

		SelectQueryUtil queryUtil = new SelectQueryUtil();
		queryUtil.select("sd.ID_DELEGACION, sd.DES_DELEGACION").from(TABLA_SVC_DELEGACION_SD);
		final String query = queryUtil.build();
		String encoded = DatatypeConverter.printBase64Binary(query.getBytes());
		request.getDatos().put(AppConstantes.QUERY, encoded);
		return request;
	}

	public DatosRequest obtenerVelatorio(DatosRequest request) {

		SelectQueryUtil queryUtil = new SelectQueryUtil();
		queryUtil.select("sv.ID_VELATORIO", "sv.NOM_VELATORIO").from(TABLA_SVC_VELATORIO_SV);
		final String query = queryUtil.build();
		String encoded = DatatypeConverter.printBase64Binary(query.getBytes());
		request.getDatos().put(AppConstantes.QUERY, encoded);
		return request;
	}

	public DatosRequest obtenerNivel(DatosRequest request) {

		SelectQueryUtil queryUtil = new SelectQueryUtil();
		queryUtil.select("sno.ID_OFICINA", "sno.DES_NIVELOFICINA").from("svc_nivel_oficina sno ");
		final String query = queryUtil.build();
		String encoded = DatatypeConverter.printBase64Binary(query.getBytes());
		request.getDatos().put(AppConstantes.QUERY, encoded);
		return request;
	}

	public DatosRequest consultarFiltroDonadosEntrada(DatosRequest request, String formatoFecha) {
		SelectQueryUtil queryUtil = new SelectQueryUtil();
		queryUtil
				.select(CAMPO_ID_INVENTARIO, CAMPO_NOMBRE_VELATORIO,
						CAMPO_TIPO, CAMPO_MODELO_ATAUD,
						CAMPO_NUMERO_INVENTARIO,
						CAMPO_FECHA_DONACION_ENTRADA + formatoFecha + CAMPO_FECHA_DONACION_ENTRADA_COMP, CAMPO_DONADO_POR_ENTRADA,
						"sp.NOM_PERSONA AS nomDonador")
				.from("svc_donacion sd")
				.join("svc_donacion_orden_servicio sdos", "sdos.ID_ORDEN_SERVICIO = sd.ID_ORDEN_SERVICIO")
				.join(" svc_orden_servicio sos ", " sos.ID_ORDEN_SERVICIO = sdos.ID_ORDEN_SERVICIO")
				.join(" svc_contratante sc ", " sc.ID_CONTRATANTE = sos.ID_CONTRATANTE")
				.join(" svc_persona sp ", " sp.ID_PERSONA =sc.ID_PERSONA ")
				.join(" svt_articulo sa ", " sa.ID_ARTICULO = sdos.ID_ARTICULO")
				.join(" svc_tipo_articulo sta ", " sta.ID_TIPO_ARTICULO = sa.ID_TIPO_ARTICULO ")
				.join(" svc_tipo_material stm ", " stm.ID_TIPO_MATERIAL = sa.ID_TIPO_MATERIAL")
				.join(" svc_inventario si ", " si.ID_ARTICULO = sa.ID_ARTICULO ")
				.join(" svc_velatorio sv ", " sv.ID_VELATORIO = si.ID_VELATORIO").where("sv.ID_VELATORIO = :idVel")
				.setParameter(PARAM_IDVELATORIO, this.idVelatorio).and(PARAM_SV_DELEGACION_IDDEL)
				.setParameter(PARAM_IDDELEGACION, this.idDelegacion);
		if (this.fechaInicio != null && this.fechaFin != null) {
			queryUtil.and("date_format(sd.FEC_ALTA,'" + formatoFecha + "') >= :fecIni")
					.setParameter(PARAM_FECHA_INICIO, this.fechaInicio)
					.and(CAMPO_FECHA_DONACION_ENTRADA+ formatoFecha + "') <= :fecFin")
					.setParameter(PARAM_FECHA_FIN, this.fechaFin);
		}
		final String query = queryUtil.build();
		String encoded = DatatypeConverter.printBase64Binary(query.getBytes());
		request.getDatos().put(AppConstantes.QUERY, encoded);

		return request;
	}

	public DatosRequest consultarDonados(DatosRequest request, String formatoFecha) {
		SelectQueryUtil primerQuery = new SelectQueryUtil();
		SelectQueryUtil segundoQuery = new SelectQueryUtil();
		primerQuery.select(CAMPO_ID_INVENTARIO, CAMPO_NOMBRE_VELATORIO, CAMPO_TIPO, CAMPO_MODELO_ATAUD,
				CAMPO_NUMERO_INVENTARIO,
				CAMPO_FECHA_DONACION_SALIDA + formatoFecha + CAMPO_FECHA_DONACION_SALIDA_COMP,
				CAMPO_DONADO_POR_SALIDA, CAMPO_NOMBRE_DONADOR).from("svc_inventario si ")
				.innerJoin("svt_articulo sa", "sa.ID_ARTICULO = si.ID_ARTICULO")
				.innerJoin(TABLA_SVC_VELATORIO_SV, "sv.ID_VELATORIO = si.ID_VELATORIO")
				.innerJoin("svc_tipo_material stm", "stm.ID_TIPO_MATERIAL = sa.ID_TIPO_MATERIAL")
				.innerJoin("svc_salida_donacion_ataudes ssda", "ssda.ID_ARTICULO = sa.ID_ARTICULO")
				.innerJoin("svc_salida_donacion ssd", "ssd.ID_SALIDA_DONACION  = ssda.ID_SALIDA_DONACION")
				.innerJoin("svc_contratante sc", "sc.ID_CONTRATANTE = ssd.ID_CONTRATANTE")
				.innerJoin("svc_persona sp", "sp.ID_PERSONA = sc.ID_PERSONA")
				.innerJoin(TABLA_SVC_DELEGACION_SD, "sd.ID_DELEGACION = sv.ID_DELEGACION").where("si.ID_VELATORIO = :idVel")
				.setParameter(PARAM_IDVELATORIO, this.idVelatorio).and(PARAM_SV_DELEGACION_IDDEL)
				.setParameter(PARAM_IDDELEGACION, this.idDelegacion);
		if (this.fechaInicio != null && this.fechaFin != null) {
			primerQuery.and("ssd.FEC_SOLICITUD >= :fecIni").setParameter(PARAM_FECHA_INICIO, this.fechaInicio)
			.and("ssd.FEC_SOLICITUD <= :fecFin").setParameter(PARAM_FECHA_FIN, this.fechaFin);
		}
		segundoQuery.select(CAMPO_ID_INVENTARIO, CAMPO_NOMBRE_VELATORIO,
				CAMPO_TIPO, CAMPO_MODELO_ATAUD,
				CAMPO_NUMERO_INVENTARIO,
				CAMPO_FECHA_DONACION_ENTRADA + formatoFecha + CAMPO_FECHA_DONACION_ENTRADA_COMP, CAMPO_DONADO_POR_ENTRADA,
				"sp.NOM_PERSONA AS nomDonador").from("svc_donacion sd")
				.join("svc_donacion_orden_servicio sdos", "sdos.ID_ORDEN_SERVICIO = sd.ID_ORDEN_SERVICIO")
				.join(" svc_orden_servicio sos ", " sos.ID_ORDEN_SERVICIO = sdos.ID_ORDEN_SERVICIO")
				.join(" svc_contratante sc ", " sc.ID_CONTRATANTE = sos.ID_CONTRATANTE")
				.join(" svc_persona sp ", " sp.ID_PERSONA =sc.ID_PERSONA ")
				.join(" svt_articulo sa ", " sa.ID_ARTICULO = sdos.ID_ARTICULO")
				.join(" svc_tipo_articulo sta ", " sta.ID_TIPO_ARTICULO = sa.ID_TIPO_ARTICULO ")
				.join(" svc_tipo_material stm ", " stm.ID_TIPO_MATERIAL = sa.ID_TIPO_MATERIAL")
				.join(" svc_inventario si ", " si.ID_ARTICULO = sa.ID_ARTICULO ")
				.join(" svc_velatorio sv ", " sv.ID_VELATORIO = si.ID_VELATORIO").where("sv.ID_VELATORIO = :idVel")
				.setParameter(PARAM_IDVELATORIO, this.idVelatorio).and(PARAM_SV_DELEGACION_IDDEL)
				.setParameter(PARAM_IDDELEGACION, this.idDelegacion);
		if (this.fechaInicio != null && this.fechaFin != null) {
			segundoQuery.and("date_format(sd.FEC_ALTA,'" + formatoFecha + "') >= :fecIni")
					.setParameter(PARAM_FECHA_INICIO, this.fechaInicio)
					.and(CAMPO_FECHA_DONACION_ENTRADA+ formatoFecha + "') <= :fecFin")
					.setParameter(PARAM_FECHA_FIN, this.fechaFin);
		}
		
		  final String query = primerQuery.union(segundoQuery);
		  String encoded =DatatypeConverter.printBase64Binary(query.getBytes());
		  request.getDatos().put(AppConstantes.QUERY, encoded);
		  
		  return request; 
	  }
	public Map<String, Object> generarReportePDF(ReporteDto reporteDto, String nombrePdfReportes) {
		Map<String, Object> envioDatos = new HashMap<>();
		String condicion = " ";
		String condicion1 = " ";
		if (this.idVelatorio != null && this.idDelegacion != null) {
			condicion = condicion + " AND si.ID_VELATORIO = " + this.idVelatorio + "  AND sv.ID_DELEGACION = "
					+ this.idDelegacion;
			condicion1 = condicion1 + " AND sv.ID_VELATORIO = " + this.idVelatorio + "  AND sv.ID_DELEGACION = "
					+ this.idDelegacion;
		}
		if (this.fechaInicio != null && this.fechaFin != null) {
			condicion = condicion + " AND date_format(ssd.FEC_SOLICITUD,'%Y-%m-%d') >= '" + this.fechaInicio + "'"
					+ " AND date_format(ssd.FEC_SOLICITUD,'%Y-%m-%d') <= '" + this.fechaFin + "'";
			condicion1 = condicion1 + " AND date_format(sd.FEC_ALTA ,'%Y-%m-%d') >= '" + this.fechaInicio + "'"
					+ " AND date_format(sd.FEC_ALTA ,'%Y-%m-%d') <= '" + this.fechaFin + "'";
		}
		envioDatos.put("condicion", condicion);
		envioDatos.put("condicion1", condicion1);
		envioDatos.put("tipoReporte", reporteDto.getTipoReporte());
		envioDatos.put("rutaNombreReporte", nombrePdfReportes);

		return envioDatos;
	}

}
