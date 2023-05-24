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
	
	private static final String CAMPO_SIA_FOLIO_ARTICULO = "sia.FOLIO_ARTICULO AS numInventario";
	private static final String CAMPO_NOMBRE_VELATORIO = "sv.NOM_VELATORIO AS velatorio";
	private static final String CAMPO_STM_DES_TIPO_MATERIAL = "stm.DES_TIPO_MATERIAL AS tipoMaterial";
	private static final String CAMPO_MODELO_ATAUD = "sa.DES_MODELO_ARTICULO AS modeloAtaud";
	private static final String CAMPO_ODS_DONADO_POR = "'ODS' AS donadoPor";
	private static final String CAMPO_FECHA_DONACION_ENTRADA = "date_format(sd.FEC_ALTA ,'" ;
	
	private static final String TABLA_SVC_VELATORIO_SV = "SVC_VELATORIO sv";
	private static final String TABLA_SVC_DELEGACION_SD = "SVC_DELEGACION sd";
	private static final String TABLA_SVC_TIPO_MATERIAL_STM = "SVC_TIPO_MATERIAL stm";
	private static final String TABLA_SVC_SALIDA_DONACION_ATAUDES_SSDA = "SVC_SALIDA_DONACION_ATAUDES ssda";
	private static final String TABLA_SVC_SALIDA_DONACION_SSD = "SVC_SALIDA_DONACION ssd";
	private static final String TABLA_SVC_CONTRANTANTE_SC = "SVC_CONTRATANTE sc";
	private static final String TABLA_SVC_PERSONA_SP = "SVC_PERSONA sp";
	private static final String TABLA_SVT_INVENTARIO_ARTICULO_SIA = "SVT_INVENTARIO_ARTICULO sia";
	private static final String TABLA_SVT_ARTICULO_SA = "SVT_ARTICULO sa";
	private static final String TABLA_SVC_DONACION_ORDEN_SERVICIO_SDOS = "SVC_DONACION_ORDEN_SERVICIO sdos";
	private static final String TABLA_SVC_ORDEN_SERVICIO_SOS = "SVC_ORDEN_SERVICIO sos";
	private static final String TABLA_SVC_FINADO_SF = "svc_finado sf";
	
	private static final String PARAM_SV_DELEGACION_IDDEL = "sv.ID_DELEGACION = :idDel";
	private static final String PARAM_FECHA_INICIO = "fecIni";
	private static final String PARAM_FECHA_FIN = "fecFin";
	private static final String PARAM_IDVELATORIO = "idVel";
	private static final String PARAM_IDDELEGACION = "idDel";
	private static final String PARAM_SF_ID_VELATORIO_IDVEL = "sf.ID_VELATORIO = :idVel";
	private static final String PARAM_SF_ID_ORDEN_SERVICIO_SOS = "sf.ID_ORDEN_SERVICIO = sos.ID_ORDEN_SERVICIO "; 
	
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
				.select(CAMPO_NOMBRE_VELATORIO,CAMPO_STM_DES_TIPO_MATERIAL
						,CAMPO_MODELO_ATAUD,CAMPO_SIA_FOLIO_ARTICULO
						,"date_format(ssd.FEC_SOLICITUD,'" + formatoFecha + "') AS fecDonacion"
						,CAMPO_ODS_DONADO_POR,"CONCAT(sp.NOM_PERSONA,' ', sp.NOM_PRIMER_APELLIDO, ' ', sp.NOM_SEGUNDO_APELLIDO) AS nomDonador")
				.from(TABLA_SVT_INVENTARIO_ARTICULO_SIA)
				.innerJoin(TABLA_SVT_ARTICULO_SA,"sa.ID_ARTICULO = sia.ID_ARTICULO")  
				.innerJoin(TABLA_SVC_TIPO_MATERIAL_STM," stm.ID_TIPO_MATERIAL = sa.ID_TIPO_MATERIAL")
				.innerJoin(TABLA_SVC_DONACION_ORDEN_SERVICIO_SDOS,"sdos.ID_INVE_ARTICULO = sia.ID_INVE_ARTICULO")  
				.innerJoin(TABLA_SVC_ORDEN_SERVICIO_SOS,"sos.ID_ORDEN_SERVICIO = sdos.ID_ORDEN_SERVICIO ")
				.innerJoin(TABLA_SVC_FINADO_SF,PARAM_SF_ID_ORDEN_SERVICIO_SOS)
				.innerJoin(TABLA_SVC_VELATORIO_SV,"sv.ID_VELATORIO = sf.ID_VELATORIO")
				.innerJoin(TABLA_SVC_SALIDA_DONACION_ATAUDES_SSDA,"ssda.ID_ARTICULO = sa.ID_ARTICULO")  
				.innerJoin(TABLA_SVC_SALIDA_DONACION_SSD,"ssd.ID_SALIDA_DONACION  = ssda.ID_SALIDA_DONACION")  
				.innerJoin(TABLA_SVC_CONTRANTANTE_SC,"sc.ID_CONTRATANTE = ssd.ID_CONTRATANTE ")
				.innerJoin(TABLA_SVC_PERSONA_SP,"sp.ID_PERSONA = sc.ID_PERSONA")
				.where(PARAM_SF_ID_VELATORIO_IDVEL)
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
				.select(CAMPO_NOMBRE_VELATORIO,CAMPO_STM_DES_TIPO_MATERIAL
						,CAMPO_MODELO_ATAUD,"si.NUM_INVENTARIO AS numInventario"
						,"date_format(sd.FEC_ALTA ,'" + formatoFecha + "')  AS fecDonacion","'Instituto' AS donadoPor"
						,"CONCAT(sp.NOM_PERSONA,' ', sp.NOM_PRIMER_APELLIDO, ' ', sp.NOM_SEGUNDO_APELLIDO) AS nomDonador ")
						.from("SVC_DONACION sd ") 
						.innerJoin(TABLA_SVC_DONACION_ORDEN_SERVICIO_SDOS,"sdos.ID_ORDEN_SERVICIO = sd.ID_ORDEN_SERVICIO ")
						.innerJoin(TABLA_SVC_ORDEN_SERVICIO_SOS,"sos.ID_ORDEN_SERVICIO = sdos.ID_ORDEN_SERVICIO  ")
						.innerJoin(TABLA_SVC_CONTRANTANTE_SC,"sc.ID_CONTRATANTE = sos.ID_CONTRATANTE ")
						.innerJoin(TABLA_SVC_PERSONA_SP,"sp.ID_PERSONA =sc.ID_PERSONA  ")
						.innerJoin(TABLA_SVT_INVENTARIO_ARTICULO_SIA,"sia.ID_INVE_ARTICULO = sdos.ID_INVE_ARTICULO")
						.innerJoin("svt_inventario si","si.ID_INVENTARIO = sia.ID_ARTICULO ")
						.innerJoin(TABLA_SVT_ARTICULO_SA,"sa.ID_ARTICULO = sia.ID_ARTICULO  ")
						.innerJoin("SVC_TIPO_ARTICULO sta","sta.ID_TIPO_ARTICULO = sa.ID_TIPO_ARTICULO  ")
						.innerJoin(TABLA_SVC_TIPO_MATERIAL_STM,"stm.ID_TIPO_MATERIAL = sa.ID_TIPO_MATERIAL   ")
						.innerJoin(TABLA_SVC_FINADO_SF,PARAM_SF_ID_ORDEN_SERVICIO_SOS)
						 .innerJoin(TABLA_SVC_VELATORIO_SV,"sv.ID_VELATORIO = sf.ID_VELATORIO ")
				.where(PARAM_SF_ID_VELATORIO_IDVEL)
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
		primerQuery.select(CAMPO_NOMBRE_VELATORIO,CAMPO_STM_DES_TIPO_MATERIAL,CAMPO_MODELO_ATAUD
				,CAMPO_SIA_FOLIO_ARTICULO,"date_format(ssd.FEC_SOLICITUD,'" + formatoFecha + "')  AS fecDonacion",CAMPO_ODS_DONADO_POR
				,"CONCAT(sp.NOM_PERSONA,' ', sp.NOM_PRIMER_APELLIDO, ' ', sp.NOM_SEGUNDO_APELLIDO) AS nomDonador") 
				.from(TABLA_SVT_INVENTARIO_ARTICULO_SIA) 
				.innerJoin(TABLA_SVT_ARTICULO_SA,"sa.ID_ARTICULO = sia.ID_ARTICULO")  
				.innerJoin(TABLA_SVC_TIPO_MATERIAL_STM,"stm.ID_TIPO_MATERIAL = sa.ID_TIPO_MATERIAL")
				.innerJoin(TABLA_SVC_DONACION_ORDEN_SERVICIO_SDOS,"sdos.ID_INVE_ARTICULO = sia.ID_INVE_ARTICULO ")
				.innerJoin(TABLA_SVC_ORDEN_SERVICIO_SOS,"sos.ID_ORDEN_SERVICIO = sdos.ID_ORDEN_SERVICIO ")
				.innerJoin(TABLA_SVC_FINADO_SF,PARAM_SF_ID_ORDEN_SERVICIO_SOS)
				.innerJoin(TABLA_SVC_VELATORIO_SV,"sv.ID_VELATORIO = sf.ID_VELATORIO ")
				.innerJoin(TABLA_SVC_SALIDA_DONACION_ATAUDES_SSDA,"ssda.ID_ARTICULO = sa.ID_ARTICULO  ")
				.innerJoin(TABLA_SVC_SALIDA_DONACION_SSD,"ssd.ID_SALIDA_DONACION  = ssda.ID_SALIDA_DONACION ")
				.innerJoin(TABLA_SVC_CONTRANTANTE_SC,"sc.ID_CONTRATANTE = ssd.ID_CONTRATANTE  ")
				.innerJoin(TABLA_SVC_PERSONA_SP,"sp.ID_PERSONA = sc.ID_PERSONA ");
		if (this.idVelatorio != null && this.idDelegacion != null) {
			primerQuery.where(PARAM_SF_ID_VELATORIO_IDVEL)
				.setParameter(PARAM_IDVELATORIO, this.idVelatorio).and(PARAM_SV_DELEGACION_IDDEL)
				.setParameter(PARAM_IDDELEGACION, this.idDelegacion);
			if (this.fechaInicio != null && this.fechaFin != null) {
				primerQuery.and("ssd.FEC_SOLICITUD >= :fecIni").setParameter(PARAM_FECHA_INICIO, this.fechaInicio)
				.and("ssd.FEC_SOLICITUD <= :fecFin").setParameter(PARAM_FECHA_FIN, this.fechaFin);
			}
		}
		segundoQuery.select(CAMPO_NOMBRE_VELATORIO,CAMPO_STM_DES_TIPO_MATERIAL,CAMPO_MODELO_ATAUD
				,"si.NUM_INVENTARIO AS numInventario","date_format(sd.FEC_ALTA ,'%d/%m/%Y')  AS fecDonacion","'Instituto' AS donadoPor"
				,"CONCAT(sp.NOM_PERSONA,' ', sp.NOM_PRIMER_APELLIDO, ' ', sp.NOM_SEGUNDO_APELLIDO) AS nomDonador ")
				.from("SVC_DONACION sd ") 
				.innerJoin(TABLA_SVC_DONACION_ORDEN_SERVICIO_SDOS,"sdos.ID_ORDEN_SERVICIO = sd.ID_ORDEN_SERVICIO ")
				.innerJoin(TABLA_SVC_ORDEN_SERVICIO_SOS,"sos.ID_ORDEN_SERVICIO = sdos.ID_ORDEN_SERVICIO  ")
				.innerJoin(TABLA_SVC_CONTRANTANTE_SC,"sc.ID_CONTRATANTE = sos.ID_CONTRATANTE ")
				.innerJoin(TABLA_SVC_PERSONA_SP,"sp.ID_PERSONA =sc.ID_PERSONA  ")
				.innerJoin(TABLA_SVT_INVENTARIO_ARTICULO_SIA,"sia.ID_INVE_ARTICULO = sdos.ID_INVE_ARTICULO")
				.innerJoin("svt_inventario si","si.ID_INVENTARIO = sia.ID_ARTICULO ")
				.innerJoin(TABLA_SVT_ARTICULO_SA,"sa.ID_ARTICULO = sia.ID_ARTICULO  ")
				.innerJoin("SVC_TIPO_ARTICULO sta","sta.ID_TIPO_ARTICULO = sa.ID_TIPO_ARTICULO  ")
				.innerJoin(TABLA_SVC_TIPO_MATERIAL_STM,"stm.ID_TIPO_MATERIAL = sa.ID_TIPO_MATERIAL   ")
				.innerJoin(TABLA_SVC_FINADO_SF,PARAM_SF_ID_ORDEN_SERVICIO_SOS)
				 .innerJoin(TABLA_SVC_VELATORIO_SV,"sv.ID_VELATORIO = sf.ID_VELATORIO ");
		if (this.idVelatorio != null && this.idDelegacion != null) {
			segundoQuery.where(PARAM_SF_ID_VELATORIO_IDVEL)
			.setParameter(PARAM_IDVELATORIO, this.idVelatorio).and(PARAM_SV_DELEGACION_IDDEL)
			.setParameter(PARAM_IDDELEGACION, this.idDelegacion);
			if (this.fechaInicio != null && this.fechaFin != null) {
				segundoQuery.and("date_format(sd.FEC_ALTA,'" + formatoFecha + "') >= :fecIni")
					.setParameter(PARAM_FECHA_INICIO, this.fechaInicio)
					.and(CAMPO_FECHA_DONACION_ENTRADA+ formatoFecha + "') <= :fecFin")
					.setParameter(PARAM_FECHA_FIN, this.fechaFin);
			}
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
			condicion = condicion + " AND sf.ID_VELATORIO = " + this.idVelatorio + "  AND sv.ID_DELEGACION = "
					+ this.idDelegacion;
			condicion1 = condicion1 + " AND sf.ID_VELATORIO = " + this.idVelatorio + "  AND sv.ID_DELEGACION = "
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
