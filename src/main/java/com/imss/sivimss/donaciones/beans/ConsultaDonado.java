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
	private Integer version;


	private static final String CAMPO_FECHA_DONACION_ENTRADA = "date_format(sd.FEC_ALTA" ;
	private static final String CAMPO_FECHA_DONACION_SALIDA = "date_format(ssd.FEC_ALTA" ;
	private static final String CAMPO_SV_NOM_VELATORIO_AS_VELATORIO = "sv.DES_VELATORIO  AS velatorio";
	private static final String CAMPO_STM_DES_TIPO_MATERIAL_AS_TIPOMATERIAL = "stm.DES_TIPO_MATERIAL AS tipoMaterial";
	private static final String CAMPO_SA_DES_MODELO_ARTICULO_AS_MODELOARTICULO = "sa.DES_MODELO_ARTICULO AS modeloAtaud";
	private static final String CAMPO_SIA_FOLIO_ARTICULO_AS_NUM_INVENTARIO = "sia.CVE_FOLIO_ARTICULO AS numInventario";
	private static final String CAMPO_SSD_FEC_ALTA = "ssd.FEC_ALTA,'";
	private static final String CAMPO_SD_FEC_ALTA = "sd.FEC_ALTA,'";
	private static final String CAMPO_AS_NOMDONADOPOR_SALIDA =  "IFNULL((CONCAT(sp.NOM_PERSONA , ' ', sp.NOM_PRIMER_APELLIDO , ' ', sp.NOM_SEGUNDO_APELLIDO)), ssd.DES_INSTITUCION) AS nomDonador";
	private static final String CAMPO_AS_NOMDONADOPOR_ENTRADA = "IFNULL((CONCAT(sp3.NOM_PERSONA, ' ', sp3.NOM_PRIMER_APELLIDO, ' ', sp3.NOM_SEGUNDO_APELLIDO)), sp2.NOM_PROVEEDOR  ) AS nomDonador";

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

	private Map<Integer, Object> nombreCampos;
	private Map<Integer, Object> nombreCamposSalida;
	
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
	private void setCamposEntrada() {
		nombreCampos = new HashMap<>();
		nombreCampos.put(1,"ooadNom"); 
		nombreCampos.put(2,"velatorioId"); 
		nombreCampos.put(3,"numContrato"); 
		nombreCampos.put(4,"numInventarios");
		nombreCampos.put(5,"tipoAtaud");
		nombreCampos.put(6,"modeloAtaud");
		nombreCampos.put(7,"nomContratante");
		nombreCampos.put(8,"nomFinado");
		nombreCampos.put(9,"nomResponsableAlmacen");
		nombreCampos.put(10,"claveResponsableAlmacen");
		nombreCampos.put(11,"nomAdministrador");
		nombreCampos.put(12,"claveAdministrador");
		nombreCampos.put(13,"lugar");
		nombreCampos.put(14,"dia");
		nombreCampos.put(15,"mes1"); 
		nombreCampos.put(16,"mes");
		nombreCampos.put(17,"anio");
		nombreCampos.put(18,"velatorioNom");
		
		
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

	public Map<String, Object> generarReportePDF(ReporteDto reporteDto, String nombrePdfReportes, int entradaSalida,String formatoFecha) {
		Map<String, Object> envioDatos = new HashMap<>();
		String query1 = "";
		String query2 = "";
		String union = "";
		if(entradaSalida == 0) {
			query1 = construirQueryEntrada(formatoFecha).build();
			union = " UNION ";
			query2 = construirQuerySalida(formatoFecha).build();
		}else if(entradaSalida == 1) {
			query2 = construirQuerySalida(formatoFecha).build();
		}else if(entradaSalida == 2) {
			query1 = construirQueryEntrada(formatoFecha).build();
		}
		envioDatos.put("query1", query1);
		envioDatos.put("union", union);
		envioDatos.put("query2", query2);
		envioDatos.put("tipoReporte", reporteDto.getTipoReporte());
		envioDatos.put("rutaNombreReporte", nombrePdfReportes);

		return envioDatos;
	}

	public DatosRequest generarQueryReporteEntrada(DatosRequest request,ReporteDto reporteDto) {
		String query = construirQueryEntradaReporte(reporteDto.getIdDonacion(), reporteDto.getIdAtaudDonacion()).build();
		String encoded = DatatypeConverter.printBase64Binary(query.getBytes());
		request.getDatos().put(AppConstantes.QUERY, encoded);

		return request;
	}
	public DatosRequest generarQueryReporteSalida(DatosRequest request,ReporteDto reporteDto,String formatoFecha) {
		String query = construirQuerySalidaReporte(reporteDto.getIdSalidaDona(), formatoFecha).build();		
		String encoded = DatatypeConverter.printBase64Binary(query.getBytes());
		request.getDatos().put(AppConstantes.QUERY, encoded);
		return request;
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
	private SelectQueryUtil construirQueryEntradaReporte(Integer idDonacion, Integer idAtaudDona) {
		SelectQueryUtil queryUtil = new SelectQueryUtil();
		setCamposEntrada();
	queryUtil.select("SDN.DES_DELEGACION AS " + nombreCampos.get(1),"SV.ID_VELATORIO AS  " + nombreCampos.get(2),"SOE.CVE_FOLIO AS "  + nombreCampos.get(3)
			,"SIA.CVE_FOLIO_ARTICULO AS "  + nombreCampos.get(4), "STM.DES_TIPO_MATERIAL AS "  + nombreCampos.get(5), "CONCAT_WS('-',SIA.CVE_FOLIO_ARTICULO,SA.DES_MODELO_ARTICULO ) AS "  + nombreCampos.get(6)
			, "CONCAT_WS(' ',SCP.NOM_PERSONA,SCP.NOM_PRIMER_APELLIDO,SCP.NOM_SEGUNDO_APELLIDO ) AS "  + nombreCampos.get(7)
			, "CONCAT_WS(' ',SFP.NOM_PERSONA,SFP.NOM_PRIMER_APELLIDO,SFP.NOM_SEGUNDO_APELLIDO ) AS "  + nombreCampos.get(8)
			, "SD.DES_RESPONSABLE_ALMACEN AS " + nombreCampos.get(9), "SD.DES_MATRICULA_ALMACEN AS " + nombreCampos.get(10)
			, "CONCAT_WS(' ',SU.NOM_USUARIO,SU.NOM_APELLIDO_PATERNO,SU.NOM_APELLIDO_MATERNO) AS " + nombreCampos.get(11)
			, "SU.CVE_MATRICULA AS " + nombreCampos.get(12), "CONCAT_WS(',',SV.DES_VELATORIO,SDN.DES_DELEGACION) AS " + nombreCampos.get(13)
			, "DAY(SAD.FEC_ALTA) AS " + nombreCampos.get(14), "MONTHNAME(SAD.FEC_ALTA) AS " + nombreCampos.get(15), "ELT(MONTH(SAD.FEC_ALTA), \"Enero\", \"Febrero\", \"Marzo\", \"Abril\", \"Mayo\", \"Junio\", \"Julio\", \"Agosto\", \"Septiembre\", \"Octubre\", \"Noviembre\", \"Diciembre\") AS " + nombreCampos.get(16)
			, "YEAR(SAD.FEC_ALTA) AS " + nombreCampos.get(17))
		.from("SVC_DONACION SD")
		.join("SVC_ATAUDES_DONADOS SAD", " SD.ID_DONACION = SAD.ID_DONACION")
		.join("SVC_ORDEN_SERVICIO SOE","SOE.ID_ORDEN_SERVICIO = SD.ID_ORDEN_SERVICIO")
		.join("SVC_VELATORIO SV","SOE.ID_VELATORIO = SV.ID_VELATORIO ")
		.join("SVC_DELEGACION SDN","SDN.ID_DELEGACION = SV.ID_DELEGACION")
		.join("SVT_INVENTARIO_ARTICULO SIA","SIA.ID_INVE_ARTICULO = SAD.ID_INVE_ARTICULO ")
		.join("SVT_ARTICULO SA","SA.ID_ARTICULO = SIA.ID_ARTICULO")
		.join("SVC_TIPO_MATERIAL STM","SA.ID_TIPO_MATERIAL = STM.ID_TIPO_MATERIAL")
		.join("SVC_CONTRATANTE SC","SOE.ID_CONTRATANTE = SC.ID_CONTRATANTE")
		.join("SVC_PERSONA SCP","SC.ID_PERSONA = SCP.ID_PERSONA")
		.join("SVC_FINADO SF","SOE.ID_ORDEN_SERVICIO = SF.ID_ORDEN_SERVICIO")
		.join("SVC_PERSONA SFP","SF.ID_PERSONA = SFP.ID_PERSONA")
		.join("SVT_USUARIOS SU","SV.ID_USUARIO_ADMIN = SU.ID_USUARIO");
		if(idDonacion != null) {
			queryUtil.where("SD.ID_DONACION = :idDonacion")
			.setParameter("idDonacion",idDonacion);
			if(idAtaudDona != null) {
				queryUtil.and("SAD.ID_ATAUD_DONACION = :idAtaudDona")
				.setParameter("idAtaudDona", idAtaudDona);
			}
		}else if(idAtaudDona != null){
				queryUtil.where("SAD.ID_ATAUD_DONACION = :idAtaudDona")
				.setParameter("idAtaudDona", idAtaudDona);
			}
		
		return queryUtil;
	}

	
	private SelectQueryUtil construirQueryEntrada(String formatoFecha) {
		SelectQueryUtil queryUtil = new SelectQueryUtil();
	queryUtil.select("DISTINCT(sd.ID_DONACION ) AS idDonacion"
			,"sad.ID_ATAUD_DONACION AS idAtaudDonacion"
			," sia.ID_INVE_ARTICULO AS idInventario"
			,CAMPO_SV_NOM_VELATORIO_AS_VELATORIO
			,CAMPO_STM_DES_TIPO_MATERIAL_AS_TIPOMATERIAL
			,CAMPO_SA_DES_MODELO_ARTICULO_AS_MODELOARTICULO
			,CAMPO_SIA_FOLIO_ARTICULO_AS_NUM_INVENTARIO
			,VALIDACION_DATE_FORMAT + CAMPO_SD_FEC_ALTA + formatoFecha + "')" + CAMPO_ALIAS_AS_FECDONACION 
			+ "," + CAMPO_ALIAS_AS_ODS_DONADO_POR
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
		.join("SVC_ORDEN_SERVICIO sos", "sos.ID_ORDEN_SERVICIO = sd.ID_ORDEN_SERVICIO") 
		.leftJoin("SVC_CONTRATANTE sc3", "sc3.ID_CONTRATANTE = sos.ID_CONTRATANTE") 
		.join("SVC_PERSONA sp3"," sp3.ID_PERSONA = sc3.ID_PERSONA")
		.join("SVT_PROVEEDOR sp2", "sp2.ID_PROVEEDOR = soe.ID_CONTRATO");
		genWhere(queryUtil);
		return queryUtil;
	}
	 
	private SelectQueryUtil construirQuerySalida(String formatoFecha) {
		SelectQueryUtil queryUtil = new SelectQueryUtil();
		queryUtil.select("distinct(ssda.ID_SALIDA_DONACION ) AS idSalidaDonacion","sad.ID_ATAUD_DONACION AS idAtaudDonacion"," sia.ID_INVE_ARTICULO AS idInventario",CAMPO_SV_NOM_VELATORIO_AS_VELATORIO,CAMPO_STM_DES_TIPO_MATERIAL_AS_TIPOMATERIAL,CAMPO_SA_DES_MODELO_ARTICULO_AS_MODELOARTICULO,CAMPO_SIA_FOLIO_ARTICULO_AS_NUM_INVENTARIO
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


	private void setCamposSalida() {
		nombreCamposSalida = new HashMap<>();
		nombreCamposSalida.put(1,"ooadNom"); 
		nombreCamposSalida.put(2,"velatorioId");
		nombreCamposSalida.put(3,"velatorioNom"); 
		nombreCamposSalida.put(4,"numInventarios"); 
		nombreCamposSalida.put(5,"tipoAtaud");
		nombreCamposSalida.put(6,"modeloAtaud");
		nombreCamposSalida.put(7,"numAtaudes");
		nombreCamposSalida.put(8,"nomFinados");
		nombreCamposSalida.put(9,"nomSolicitantes");
		nombreCamposSalida.put(10,"nomResponsableAlmacen");
		nombreCamposSalida.put(11,"claveResponsableAlmacen");
		nombreCamposSalida.put(12,"fecSolicitud");
		nombreCamposSalida.put(13,"nomAdministrador");
		nombreCamposSalida.put(14,"claveAdministrador");
		
		nombreCamposSalida.put(15,"lugar");
		nombreCamposSalida.put(16,"dia");
		nombreCamposSalida.put(17,"mes");
		nombreCamposSalida.put(18,"anio");
		
		
	}

	private SelectQueryUtil construirQuerySalidaReporte(Integer idSalidaDona, String formatoFecha) {
		SelectQueryUtil queryUtil = new SelectQueryUtil();
		setCamposSalida();
	queryUtil.select("SDN.ID_DELEGACION  AS " + nombreCamposSalida.get(1)
			,"SV.ID_VELATORIO AS " + nombreCamposSalida.get(2),"SV.DES_VELATORIO  AS " + nombreCamposSalida.get(3)
			,"SIA.CVE_FOLIO_ARTICULO AS " + nombreCamposSalida.get(4),"STM.DES_TIPO_MATERIAL AS " + nombreCamposSalida.get(5)
			,"CONCAT_WS('-',SIA.CVE_FOLIO_ARTICULO,SA.DES_MODELO_ARTICULO ) AS " + nombreCamposSalida.get(6)
			,"SSD.NUM_TOTAL_ATAUDES AS " + nombreCamposSalida.get(7)
			,"CONCAT_WS(' ',SSDF.NOM_FINADO,SSDF.NOM_PRIMER_APELLIDO,SSDF.NOM_SEGUNDO_APELLIDO ) AS "  + nombreCamposSalida.get(8)
			,"CONCAT_WS(' ',SCP.NOM_PERSONA,SCP.NOM_PRIMER_APELLIDO,SCP.NOM_SEGUNDO_APELLIDO ) AS "  + nombreCamposSalida.get(9)
			,"SSD.DES_RESPONSABLE_ALMACEN AS " + nombreCamposSalida.get(10)
			,"SSD.CVE_MATRICULA_RESPONSABLE AS "  + nombreCamposSalida.get(11),"date_format(SSD.FEC_ALTA ,'" + formatoFecha + "') AS " + nombreCamposSalida.get(12)
			,"CONCAT_WS(' ',SU.NOM_USUARIO,SU.NOM_APELLIDO_PATERNO,SU.NOM_APELLIDO_MATERNO) AS " + nombreCamposSalida.get(13)
			,"SU.CVE_MATRICULA AS " + nombreCamposSalida.get(14)
			,"CONCAT_WS(',',SV.DES_VELATORIO,SDN.DES_DELEGACION) AS " + nombreCamposSalida.get(15),"DAY(SSD.FEC_ALTA) AS " + nombreCamposSalida.get(16)
			,"ELT(MONTH(SSD.FEC_ALTA), \"Enero\", \"Febrero\", \"Marzo\", \"Abril\", \"Mayo\", \"Junio\", \"Julio\", \"Agosto\", \"Septiembre\", \"Octubre\", \"Noviembre\", \"Diciembre\") AS " + nombreCamposSalida.get(17)
			,"YEAR(SSD.FEC_ALTA) AS "  + nombreCamposSalida.get(18))
			.from("SVC_DONACION SD")
			.join("SVC_ATAUDES_DONADOS SAD","SD.ID_DONACION = SAD.ID_DONACION")
			.join("SVC_ORDEN_SERVICIO SOE","SOE.ID_ORDEN_SERVICIO = SD.ID_ORDEN_SERVICIO")
			.join("SVC_VELATORIO SV","SOE.ID_VELATORIO = SV.ID_VELATORIO")
			.join("SVC_DELEGACION SDN","SDN.ID_DELEGACION = SV.ID_DELEGACION")
			.join("SVT_INVENTARIO_ARTICULO SIA","SIA.ID_INVE_ARTICULO = SAD.ID_INVE_ARTICULO")
			.join("SVT_ARTICULO SA","SA.ID_ARTICULO = SIA.ID_ARTICULO")
			.join("SVC_TIPO_MATERIAL STM","SA.ID_TIPO_MATERIAL = STM.ID_TIPO_MATERIAL")
			.join("SVC_SALIDA_DONACION_ATAUDES SSDA","SAD.ID_INVE_ARTICULO = SSDA.ID_INVE_ARTICULO")
			.join("SVC_SALIDA_DONACION SSD","SSDA.ID_SALIDA_DONACION = SSD.ID_SALIDA_DONACION") 
			.leftJoin("SVC_SALIDA_DONACION_FINADOS SSDF","SSD.ID_SALIDA_DONACION = SSDF.ID_SALIDA_DONACION")
			.join("SVC_CONTRATANTE SC","SSD.ID_CONTRATANTE = SC.ID_CONTRATANTE")
			.join("SVC_PERSONA SCP","SC.ID_PERSONA = SCP.ID_PERSONA")
			.join("SVT_USUARIOS SU","SV.ID_USUARIO_ADMIN = SU.ID_USUARIO");
			if(idSalidaDona != null) {
				queryUtil.where("SSD.ID_SALIDA_DONACION = :idSalidaDona")
				.setParameter("idSalidaDona", idSalidaDona);
			}

		return queryUtil;
	}
}
