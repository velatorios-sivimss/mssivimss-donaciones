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

	private String velatorio;
	private String modeloAtaud;
	private String donadoPor;
	private Integer idVelatorio;
	private String fechaInicio;
	private String fechaFin;
	private Integer desDelegacion;
	private Integer idInventario;
	private String formatoFecha;
	private String tipo;
	private String numInventario;
	private Date fecDonacion;
	private String nomDonador;
	private Integer idDelegacion;


	private static final String CAMPO_FECHA_DONACION_ENTRADA = "date_format(sd.FEC_ALTA" ;
	private static final String CAMPO_FECHA_DONACION_SALIDA = "date_format(ssd.FEC_ALTA" ;
	private static final String CAMPO_SV_NOM_VELATORIO_AS_VELATORIO = "sv.DES_VELATORIO  AS velatorio";
	private static final String CAMPO_STM_DES_TIPO_MATERIAL_AS_TIPOMATERIAL = "stm.DES_TIPO_MATERIAL AS tipoMaterial";
	private static final String CAMPO_SA_DES_MODELO_ARTICULO_AS_MODELOARTICULO = "sa.DES_MODELO_ARTICULO AS modeloAtaud";
	private static final String CAMPO_SIA_FOLIO_ARTICULO_AS_NUM_INVENTARIO = "sia.FOLIO_ARTICULO AS numInventario";
	private static final String CAMPO_SSD_FEC_ALTA = "ssd.FEC_ALTA,'";
	private static final String CAMPO_SD_FEC_ALTA = "sd.FEC_ALTA,'";
	private static final String CAMPO_AS_NOMDONADOPOR_SALIDA = "IFNULL((CONCAT(sp.NOM_PERSONA , ' ', sp.NOM_PRIMER_APELLIDO , ' ', sp.NOM_SEGUNDO_APELLIDO)), ssd.DES_INSTITUCION) AS nomDonador";
	private static final String CAMPO_AS_NOMDONADOPOR_ENTRADA = "sp2.NOM_PROVEEDOR as nomDonador";

	private static final String CAMPO_ALIAS_AS_FECDONACION = "AS fecDonacion";
	private static final String CAMPO_ALIAS_AS_INSTITUTO_DONADO_POR = "'Instituto' AS donadoPor";
	private static final String CAMPO_ALIAS_AS_ODS_DONADO_POR = "'ODS' AS donadoPor";
	
	private static final String TABLA_SVC_VELATORIO_SV = "SVC_VELATORIO sv";
	private static final String TABLA_SVC_TIPO_MATERIAL_STM = "SVC_TIPO_MATERIAL stm";
	private static final String TABLA_SVC_SALIDA_DONACION_ATAUDES_SSDA = "SVC_SALIDA_DONACION_ATAUDES ssda";
	private static final String TABLA_SVC_SALIDA_DONACION_SSD = "SVC_SALIDA_DONACION ssd";
	private static final String TABLA_SVT_INVENTARIO_ARTICULO_SIA = "SVT_INVENTARIO_ARTICULO sia";
	private static final String TABLA_SVT_ARTICULO_SA = "SVT_ARTICULO sa";
	private static final String TABLA_SVC_DONACION_SD = "SVC_DONACION sd"; 
	private static final String TABLA_SVC_ATAUDES_DONADOS_SAD = "SVC_ATAUDES_DONADOS sad";
	private static final String TABLA_SVT_ORDEN_ENTRADA_SOE = "SVT_ORDEN_ENTRADA soe";
	private static final String TABLA_SVT_CONTRATO_SC2 = "SVT_CONTRATO sc2";
	private static final String TABLA_SVC_DELEGACION_SD2 = "SVC_DELEGACION sd2";
	private static final String TABLA_SVT_PROVEEDOR_SP2 = "SVT_PROVEEDOR sp2";
	private static final String TABLA_SVC_CONTRATANTE_SC = "SVC_CONTRATANTE sc";
	private static final String TABLA_SVC_PERSONA_SP = "SVC_PERSONA sp";
	
	
	private static final String PARAM_SV_DELEGACION_IDDEL = "sv.ID_DELEGACION = :idDel";
	private static final String PARAM_FECHA_INICIO = "fecIni";
	private static final String PARAM_FECHA_FIN = "fecFin";
	private static final String PARAM_IDVELATORIO = "idVel";
	private static final String PARAM_IDDELEGACION = "idDel";
	private static final String PARAM_SF_ID_VELATORIO_IDVEL = "sv.ID_VELATORIO = :idVel";
	private static final String PARAM_FECINI = ") >= :fecIni";
	private static final String PARAM_FECFIN = ") <= :fecFin";

	private static final String CAMPOS_VAL_STM_VS_SA_TIPO_MATERIAL = "stm.ID_TIPO_MATERIAL = sa.ID_TIPO_MATERIAL";
	private static final String VALIDACION_DATE_FORMAT = "date_format(";
	
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
		final String query = construirQuerySalida(formatoFecha).build();
		String encoded = DatatypeConverter.printBase64Binary(query.getBytes());
		request.getDatos().put(AppConstantes.QUERY, encoded);

		return request;
	}


	public DatosRequest consultarFiltroDonadosEntrada(DatosRequest request, String formatoFecha) {
		final String query = construirQueryEntrada(formatoFecha).build();
		String encoded = DatatypeConverter.printBase64Binary(query.getBytes());
		request.getDatos().put(AppConstantes.QUERY, encoded);

		return request;
	}

	public DatosRequest consultarDonados(DatosRequest request, String formatoFecha) {
		  final String query = construirQueryEntrada(formatoFecha).union(construirQuerySalida(formatoFecha));
		  String encoded =DatatypeConverter.printBase64Binary(query.getBytes());
		  request.getDatos().put(AppConstantes.QUERY, encoded);
		  
		  return request; 
	  }
	public Map<String, Object> generarReportePDF(ReporteDto reporteDto, String nombrePdfReportes) {
		Map<String, Object> envioDatos = new HashMap<>();
		String condicion = " ";
		String condicion1 = " ";
		if (this.idVelatorio != null && this.idDelegacion != null) {
			condicion = condicion + " AND sv.ID_VELATORIO = " + this.idVelatorio + "  AND sv.ID_DELEGACION = "
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
	private void genWhere (SelectQueryUtil query) {
		String formatoFechaAMD = "'%Y-%m-%d'";
		if (this.idVelatorio != null ) {
			query.where(PARAM_SF_ID_VELATORIO_IDVEL).setParameter(PARAM_IDVELATORIO, this.idVelatorio);
			if( this.idDelegacion != null)
				query.and(PARAM_SV_DELEGACION_IDDEL).setParameter(PARAM_IDDELEGACION, this.idDelegacion);
			if (this.fechaInicio != null ) 
				query.and(CAMPO_FECHA_DONACION_ENTRADA+ "," + formatoFechaAMD + PARAM_FECINI).setParameter(PARAM_FECHA_INICIO, this.fechaInicio);
			if( this.fechaFin != null) 
					query.and(CAMPO_FECHA_DONACION_ENTRADA + "," + formatoFechaAMD + PARAM_FECFIN).setParameter(PARAM_FECHA_FIN, this.fechaFin);
		}else if( this.idDelegacion != null) {
			query.where(PARAM_SV_DELEGACION_IDDEL).setParameter(PARAM_IDDELEGACION, this.idDelegacion);
			if (this.fechaInicio != null )
				query.and(CAMPO_FECHA_DONACION_ENTRADA+ "," + formatoFechaAMD + PARAM_FECINI).setParameter(PARAM_FECHA_INICIO, this.fechaInicio);
			if( this.fechaFin != null)
				query.and(CAMPO_FECHA_DONACION_ENTRADA + "," + formatoFechaAMD + PARAM_FECFIN).setParameter(PARAM_FECHA_FIN, this.fechaFin);
		}else if (this.fechaInicio != null ) {
			query.where(CAMPO_FECHA_DONACION_ENTRADA + "," + formatoFechaAMD + PARAM_FECINI).setParameter(PARAM_FECHA_INICIO, this.fechaInicio);
			if( this.fechaFin != null)
				query.and(CAMPO_FECHA_DONACION_ENTRADA + "," + formatoFechaAMD + PARAM_FECFIN).setParameter(PARAM_FECHA_FIN, this.fechaFin);
		}else if( this.fechaFin != null)
				query.where(CAMPO_FECHA_DONACION_ENTRADA + "," + formatoFechaAMD + PARAM_FECFIN).setParameter(PARAM_FECHA_FIN, this.fechaFin);

	}
	private SelectQueryUtil construirQueryEntrada(String formatoFecha) {
		SelectQueryUtil queryUtil = new SelectQueryUtil();
	queryUtil.select("distinct(sd.ID_DONACION )",CAMPO_SV_NOM_VELATORIO_AS_VELATORIO,CAMPO_STM_DES_TIPO_MATERIAL_AS_TIPOMATERIAL,CAMPO_SA_DES_MODELO_ARTICULO_AS_MODELOARTICULO,CAMPO_SIA_FOLIO_ARTICULO_AS_NUM_INVENTARIO
			,VALIDACION_DATE_FORMAT + CAMPO_SD_FEC_ALTA + formatoFecha + "')" + CAMPO_ALIAS_AS_FECDONACION + "," + CAMPO_ALIAS_AS_ODS_DONADO_POR
			,CAMPO_AS_NOMDONADOPOR_ENTRADA)
		.from(TABLA_SVC_DONACION_SD)
		.join(TABLA_SVC_ATAUDES_DONADOS_SAD,"sad.ID_DONACION = sd.ID_DONACION")
		.join(TABLA_SVT_INVENTARIO_ARTICULO_SIA,"sia.ID_INVE_ARTICULO = sad.ID_INVE_ARTICULO")
		.join(TABLA_SVT_ARTICULO_SA,"sa.ID_ARTICULO = sia.ID_ARTICULO")
		.join(TABLA_SVC_TIPO_MATERIAL_STM,CAMPOS_VAL_STM_VS_SA_TIPO_MATERIAL)
		.join(TABLA_SVT_ORDEN_ENTRADA_SOE,"soe.ID_ODE = sia.ID_ODE")
		.join(TABLA_SVT_CONTRATO_SC2,"sc2.ID_CONTRATO = soe.ID_CONTRATO")
		.join(TABLA_SVC_VELATORIO_SV,"sv.ID_VELATORIO = sc2.ID_VELATORIO")
		.join(TABLA_SVC_DELEGACION_SD2,"sd2.ID_DELEGACION = sv.ID_DELEGACION")
		.join(TABLA_SVT_PROVEEDOR_SP2,"sp2.ID_PROVEEDOR = soe.ID_CONTRATO");

		genWhere(queryUtil);
		return queryUtil;
	}
	private SelectQueryUtil construirQuerySalida(String formatoFecha) {
		SelectQueryUtil queryUtil = new SelectQueryUtil();
		queryUtil.select("distinct(ssda.ID_SALIDA_DONACION )",CAMPO_SV_NOM_VELATORIO_AS_VELATORIO,CAMPO_STM_DES_TIPO_MATERIAL_AS_TIPOMATERIAL,CAMPO_SA_DES_MODELO_ARTICULO_AS_MODELOARTICULO,CAMPO_SIA_FOLIO_ARTICULO_AS_NUM_INVENTARIO
				,VALIDACION_DATE_FORMAT + CAMPO_SSD_FEC_ALTA + formatoFecha + "')" + CAMPO_ALIAS_AS_FECDONACION, CAMPO_ALIAS_AS_INSTITUTO_DONADO_POR
				,CAMPO_AS_NOMDONADOPOR_SALIDA)
		.from(TABLA_SVC_DONACION_SD)
			.join(TABLA_SVC_ATAUDES_DONADOS_SAD,"sad.ID_DONACION = sd.ID_DONACION")
			.join(TABLA_SVT_INVENTARIO_ARTICULO_SIA,"sia.ID_INVE_ARTICULO = sad.ID_INVE_ARTICULO")
			.join(TABLA_SVT_ARTICULO_SA,"sa.ID_ARTICULO = sia.ID_ARTICULO")
			.join(TABLA_SVC_TIPO_MATERIAL_STM,CAMPOS_VAL_STM_VS_SA_TIPO_MATERIAL)
			.join(TABLA_SVT_ORDEN_ENTRADA_SOE,"soe.ID_ODE = sia.ID_ODE")
			.join(TABLA_SVT_CONTRATO_SC2,"sc2.ID_CONTRATO = soe.ID_CONTRATO")
			.join(TABLA_SVC_VELATORIO_SV,"sv.ID_VELATORIO = sc2.ID_VELATORIO")
			.join(TABLA_SVC_DELEGACION_SD2,"sd2.ID_DELEGACION = sv.ID_DELEGACION")
			.join(TABLA_SVT_PROVEEDOR_SP2,"sp2.ID_PROVEEDOR = soe.ID_CONTRATO")
			.join(TABLA_SVC_SALIDA_DONACION_ATAUDES_SSDA,"ssda.ID_INVE_ARTICULO = sad.ID_INVE_ARTICULO")
			.join(TABLA_SVC_SALIDA_DONACION_SSD,"ssd.ID_SALIDA_DONACION = ssda.ID_SALIDA_DONACION")
			.join(TABLA_SVC_CONTRATANTE_SC,"sc.ID_CONTRATANTE = ssd.ID_CONTRATANTE") 
			.join(TABLA_SVC_PERSONA_SP,"sp.ID_PERSONA = sc.ID_PERSONA");
		genWhereSalida(queryUtil);
		return queryUtil;
	}

	private void genWhereSalida (SelectQueryUtil query) {
		String formatoFechaAMD = "'%Y-%m-%d'";
		if (this.idVelatorio != null ) {
			query.where(PARAM_SF_ID_VELATORIO_IDVEL).setParameter(PARAM_IDVELATORIO, this.idVelatorio);
			if( this.idDelegacion != null)
				query.and(PARAM_SV_DELEGACION_IDDEL).setParameter(PARAM_IDDELEGACION, this.idDelegacion);
			if (this.fechaInicio != null ) 
				query.and(CAMPO_FECHA_DONACION_SALIDA + "," + formatoFechaAMD + PARAM_FECINI).setParameter(PARAM_FECHA_INICIO, this.fechaInicio);
			if( this.fechaFin != null) 
					query.and(CAMPO_FECHA_DONACION_SALIDA + "," + formatoFechaAMD + PARAM_FECFIN).setParameter(PARAM_FECHA_FIN, this.fechaFin);
		}else if( this.idDelegacion != null) {
			query.where(PARAM_SV_DELEGACION_IDDEL).setParameter(PARAM_IDDELEGACION, this.idDelegacion);
			if (this.fechaInicio != null )
				query.and(CAMPO_FECHA_DONACION_SALIDA + "," + formatoFechaAMD + PARAM_FECINI).setParameter(PARAM_FECHA_INICIO, this.fechaInicio);
			if( this.fechaFin != null)
				query.and(CAMPO_FECHA_DONACION_SALIDA + "," + formatoFechaAMD + PARAM_FECFIN).setParameter(PARAM_FECHA_FIN, this.fechaFin);
		}else if (this.fechaInicio != null ) {
			query.where(CAMPO_FECHA_DONACION_SALIDA + "," + formatoFechaAMD + PARAM_FECINI).setParameter(PARAM_FECHA_INICIO, this.fechaInicio);
			if( this.fechaFin != null)
				query.and(CAMPO_FECHA_DONACION_SALIDA + "," + formatoFechaAMD + PARAM_FECFIN).setParameter(PARAM_FECHA_FIN, this.fechaFin);
		}else if( this.fechaFin != null)
				query.where(CAMPO_FECHA_DONACION_SALIDA + "," + formatoFechaAMD + PARAM_FECFIN).setParameter(PARAM_FECHA_FIN, this.fechaFin);

	}
}
