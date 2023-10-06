package com.imss.sivimss.donaciones.beans;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.DatatypeConverter;

import com.imss.sivimss.donaciones.model.request.ActualizarMultiRequest;
import com.imss.sivimss.donaciones.model.request.DonacionRequest;
import com.imss.sivimss.donaciones.model.request.PlantillaAceptacionControlRequest;
import com.imss.sivimss.donaciones.model.request.UsuarioDto;
import com.imss.sivimss.donaciones.util.AppConstantes;
import com.imss.sivimss.donaciones.util.ConsultaConstantes;
import com.imss.sivimss.donaciones.util.DatosRequest;
import com.imss.sivimss.donaciones.util.QueryHelper;
import com.imss.sivimss.donaciones.util.SelectQueryUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Donacion {

	public DatosRequest detalleNombreContratante(DatosRequest request, DonacionRequest donacionRequest) {
		log.info(" INICIO - detalleNombreContratante");
		SelectQueryUtil queryUtil = new SelectQueryUtil();
		queryUtil.select("OS.ID_ORDEN_SERVICIO AS idOrdenService",
				"CONCAT_WS(' ',P.NOM_PERSONA,P.NOM_PRIMER_APELLIDO,P.NOM_SEGUNDO_APELLIDO ) AS  nombreContratante")
		.from(ConsultaConstantes.SVC_ORDEN_SERVICIO_OS)
		.innerJoin("SVC_CONTRATANTE C", "OS.ID_CONTRATANTE = C.ID_CONTRATANTE")
		.innerJoin("SVC_PERSONA P", "P.ID_PERSONA = C.ID_PERSONA")
		.where(ConsultaConstantes.OS_CVE_FOLIO_CVE_FOLIO).setParameter(ConsultaConstantes.CVE_FOLIO, donacionRequest.getClaveFolio())
		.and(ConsultaConstantes.AND_CVE_ESTATUS).setParameter(ConsultaConstantes.ESTATUS_ORDEN_SERVICIO, donacionRequest.getEstatusOrdenServicio());
		final String query = queryUtil.build();
		log.info(" detalleNombreContratante: " + query );
		String encoded = DatatypeConverter.printBase64Binary(query.getBytes(StandardCharsets.UTF_8));
		request.getDatos().put(AppConstantes.QUERY, encoded);
		log.info(" TERMINO - detalleNombreContratante");
		return request;
	}

	public DatosRequest detalleNombreFinado(DatosRequest request, DonacionRequest donacionRequest) throws UnsupportedEncodingException {
		log.info(" INICIO - detalleNombreFinado");
		SelectQueryUtil queryUtil = new SelectQueryUtil();
		queryUtil.select("OS.ID_ORDEN_SERVICIO AS idOrdenService",
				"CONCAT_WS(' ',P.NOM_PERSONA,P.NOM_PRIMER_APELLIDO,P.NOM_SEGUNDO_APELLIDO ) AS  nombreFinado")
		.from(ConsultaConstantes.SVC_ORDEN_SERVICIO_OS)
		.innerJoin("SVC_FINADO F", "OS.ID_ORDEN_SERVICIO = F.ID_ORDEN_SERVICIO")
		.innerJoin("SVC_PERSONA P", "F.ID_PERSONA = P.ID_PERSONA")
		.where(ConsultaConstantes.OS_CVE_FOLIO_CVE_FOLIO).setParameter(ConsultaConstantes.CVE_FOLIO, donacionRequest.getClaveFolio())
		.and(ConsultaConstantes.AND_CVE_ESTATUS).setParameter(ConsultaConstantes.ESTATUS_ORDEN_SERVICIO, donacionRequest.getEstatusOrdenServicio());
		final String query = queryUtil.build();
		log.info(" detalleNombreFinado: " + query );
		String encoded = DatatypeConverter.printBase64Binary(query.getBytes(StandardCharsets.UTF_8));
		request.getDatos().put(AppConstantes.QUERY, encoded);
		log.info(" TERMINO - detalleNombreFinado");
		return request;
	}
	
	public DatosRequest detalleAceptacionDonacion(DatosRequest request, DonacionRequest donacionRequest) {
		log.info(" INICIO - detalleAceptacionDonacion");
		SelectQueryUtil queryUtil = new SelectQueryUtil();
		queryUtil.select("CONCAT_WS(' ',U.NOM_USUARIO,U.NOM_APELLIDO_PATERNO,U.NOM_APELLIDO_MATERNO) AS nombreAdministrador",
				"U.CVE_MATRICULA AS matriculaAdministrador","CONCAT_WS(',',V.DES_VELATORIO,D.DES_DELEGACION) AS lugardonacion",
				"IFNULL(V.DES_VELATORIO,'') AS velatorio").from("SVT_USUARIOS U")
		.innerJoin("SVC_VELATORIO V", "U.ID_USUARIO = V.ID_USUARIO_ADMIN")
		.innerJoin("SVC_DELEGACION D", "V.ID_DELEGACION = D.ID_DELEGACION ")
		.where("V.ID_VELATORIO = :idVel").setParameter("idVel", donacionRequest.getIdVelatorio());
		final String query = queryUtil.build();
		log.info(" detalleAceptacionDonacion: " + query );
		String encoded = DatatypeConverter.printBase64Binary(query.getBytes(StandardCharsets.UTF_8));
		request.getDatos().put(AppConstantes.QUERY, encoded);
		log.info(" TERMINO - detalleAceptacionDonacion");
		return request;
	}

	public DatosRequest detalleAtaudDonado(DatosRequest request, DonacionRequest donacionRequest, UsuarioDto usuarioDto) {
		log.info(" INICIO - detalleAtaudDonado");
		SelectQueryUtil donacion = new SelectQueryUtil();
		donacion.select("DISTINCT OS.ID_ORDEN_SERVICIO").from("SVC_ORDEN_SERVICIO OS")
		.innerJoin("SVC_DONACION SD", "SD.ID_ORDEN_SERVICIO = OS.ID_ORDEN_SERVICIO")
		.innerJoin("SVC_ATAUDES_DONADOS SAD", "SAD.ID_DONACION = SD.ID_DONACION").and("SD.IND_ACTIVO = 1")
		.innerJoin("SVT_INVENTARIO_ARTICULO SIA", "SIA.ID_INVE_ARTICULO = SAD.ID_INVE_ARTICULO");
				
		SelectQueryUtil queryUtil = new SelectQueryUtil();
		queryUtil.select("S.CVE_FOLIO_ARTICULO AS folioArticulo","S.ID_INVE_ARTICULO AS idInventarioArticulo", "TM.DES_TIPO_MATERIAL AS desTipoMaterial",
				"CONCAT_WS('-',S.CVE_FOLIO_ARTICULO,A.DES_MODELO_ARTICULO ) AS  desModeloArticulo")
		.from(ConsultaConstantes.SVC_ORDEN_SERVICIO_OS)
		.innerJoin("SVC_CARAC_PRESUPUESTO CP", "OS.ID_ORDEN_SERVICIO  = CP.ID_ORDEN_SERVICIO").and("CP.IND_ACTIVO = 1")
		.innerJoin("SVC_DETALLE_CARAC_PRESUP DCP", "CP.ID_CARAC_PRESUPUESTO = DCP.ID_CARAC_PRESUPUESTO").and("DCP.IND_ACTIVO = 1")
		.innerJoin("SVT_INVENTARIO_ARTICULO S", "DCP.ID_INVE_ARTICULO = S.ID_INVE_ARTICULO").and("S.ID_TIPO_ASIGNACION_ART NOT IN( 2,3,5)")
		.innerJoin("SVT_ARTICULO A", "S.ID_ARTICULO = A.ID_ARTICULO").and("A.IND_ACTIVO = 1").and("A.ID_CATEGORIA_ARTICULO = 1").and("A.ID_TIPO_ARTICULO = 1")
		.innerJoin("SVC_TIPO_MATERIAL TM", "A.ID_TIPO_MATERIAL = TM.ID_TIPO_MATERIAL")
		.where("IFNULL(OS.ID_ORDEN_SERVICIO ,0) > 0")
		.and("OS.ID_ORDEN_SERVICIO NOT IN ("+donacion.build()+")")
		.and(ConsultaConstantes.OS_CVE_FOLIO_CVE_FOLIO).setParameter(ConsultaConstantes.CVE_FOLIO, donacionRequest.getClaveFolio())
		.and(ConsultaConstantes.AND_CVE_ESTATUS).setParameter(ConsultaConstantes.ESTATUS_ORDEN_SERVICIO, donacionRequest.getEstatusOrdenServicio())
		.and("OS.ID_VELATORIO = :idVelatorio").setParameter("idVelatorio", ConsultaConstantes.getIdVelatorio(usuarioDto.getIdVelatorio()));
		final String query = queryUtil.build();
		log.info(" detalleAtaudDonado: " + query );
		String encoded = DatatypeConverter.printBase64Binary(query.getBytes(StandardCharsets.UTF_8));
		request.getDatos().put(AppConstantes.QUERY, encoded);
		log.info(" TERMINO - detalleAtaudDonado");
		return request;
	}

	public DatosRequest insertarDonacion(DonacionRequest donacionRequest, UsuarioDto usuarioDto) {
		log.info(" INICIO - insertarDonacion");
		DatosRequest request = new DatosRequest();
		Map<String, Object> parametro = new HashMap<>();

		final QueryHelper q = new QueryHelper("INSERT INTO SVC_DONACION");
		q.agregarParametroValues("ID_ORDEN_SERVICIO", String.valueOf(donacionRequest.getIdOrdenServicio()));
		q.agregarParametroValues("NUM_TOTAL_ATAUDES", String.valueOf(donacionRequest.getNumTotalAtaudes()));
		q.agregarParametroValues("DES_RESPONSABLE_ALMACEN", SelectQueryUtil.setValor(SelectQueryUtil.eliminarEspacios(donacionRequest.getResponsableAlmacen())));
		q.agregarParametroValues("DES_MATRICULA_ALMACEN",  SelectQueryUtil.setValor( donacionRequest.getMatricularesponsable()));
		q.agregarParametroValues("IND_ACTIVO", String.valueOf(1));
		q.agregarParametroValues(ConsultaConstantes.ID_USUARIO_ALTA, String.valueOf(usuarioDto.getIdUsuario()));
		q.agregarParametroValues(ConsultaConstantes.FEC_ALTA, ConsultaConstantes.CURRENT_TIMESTAMP);

		String query = q.obtenerQueryInsertar() + insertAtaudDonado(donacionRequest, usuarioDto);
		log.info(" insertarDonacion: " + query );
		String encoded = DatatypeConverter.printBase64Binary(query.getBytes(StandardCharsets.UTF_8));
		parametro.put(AppConstantes.QUERY, encoded);
		parametro.put(ConsultaConstantes.SEPARADOR, "$$");
		parametro.put(ConsultaConstantes.REPLACE, ConsultaConstantes.ID_TABLA);

		request.setDatos(parametro);
		log.info(" TERMINO - insertarDonacion");
		return request;
	}

	public String insertAtaudDonado(DonacionRequest donacionRequest, UsuarioDto usuarioDto) {
		log.info(" INICIO - insertAtaudDonado");
		StringBuilder query = new StringBuilder();
		donacionRequest.getAtaudesDonados().forEach(agregarArticuloRequest -> {
			final QueryHelper q = new QueryHelper("INSERT INTO SVC_ATAUDES_DONADOS");
			q.agregarParametroValues("ID_DONACION",ConsultaConstantes. ID_TABLA);
			q.agregarParametroValues("ID_INVE_ARTICULO", String.valueOf(agregarArticuloRequest.getIdInventarioArticulo()));
			q.agregarParametroValues(ConsultaConstantes.ID_USUARIO_ALTA, String.valueOf(usuarioDto.getIdUsuario()));
			q.agregarParametroValues(ConsultaConstantes.FEC_ALTA, ConsultaConstantes.CURRENT_TIMESTAMP);
			query.append("$$").append(q.obtenerQueryInsertar());
		});
		log.info(" TERMINO - insertAtaudDonado");
		return query.toString();
	}
	
	public ActualizarMultiRequest actualizarStockArticulo(DonacionRequest donacionRequest, UsuarioDto usuarioDto) {
		log.info(" INICIO - actualizarStockArticulo");
		ActualizarMultiRequest actualizarMultiRequest = new ActualizarMultiRequest();
		List<String> updates = new ArrayList<>();
        donacionRequest.getAtaudesDonados().forEach(agregarArticuloRequest -> {
        	final QueryHelper q = new QueryHelper("UPDATE SVT_INVENTARIO_ARTICULO " );
        	q.agregarParametroValues("ID_TIPO_ASIGNACION_ART",  String.valueOf(3));
        	q.agregarParametroValues("IND_ESTATUS",  String.valueOf(0));
        	q.agregarParametroValues(ConsultaConstantes.ID_USUARIO_MODIFICA, String.valueOf(usuarioDto.getIdUsuario()));
    		q.agregarParametroValues(ConsultaConstantes.FEC_ACTUALIZACION, ConsultaConstantes.CURRENT_TIMESTAMP);
    		q.addWhere(" ID_INVE_ARTICULO = " + agregarArticuloRequest.getIdInventarioArticulo());
        	updates.add(DatatypeConverter.printBase64Binary(q.obtenerQueryActualizar().getBytes(StandardCharsets.UTF_8)));
        });
        actualizarMultiRequest.setUpdates(updates);
        log.info(" TERMINO - actualizarStockArticulo");
		return actualizarMultiRequest;
    }
	
	
	public Map<String, Object> generarPlantillaAceptacionControlPDF(PlantillaAceptacionControlRequest plantillaAceptacionControlRequest, String nombrePdfAceptacionControl, Integer folioDonacion) {
		log.info(" INICIO - generarPlantillaAceptacionControlPDF");
		
		Map<String, Object> envioDatos = new HashMap<>();
		
		envioDatos.put("ooadNom", ConsultaConstantes.validar(plantillaAceptacionControlRequest.getOoadNom()));
		envioDatos.put("velatorio", plantillaAceptacionControlRequest.getVelatorioId());
		envioDatos.put("numContrato", ConsultaConstantes.validar(plantillaAceptacionControlRequest.getNumContrato()));
		envioDatos.put("folioDonacion", folioDonacion);
		envioDatos.put("tipoAtaud", ConsultaConstantes.validar(plantillaAceptacionControlRequest.getTipoAtaud()));
		envioDatos.put("modeloAtaud", ConsultaConstantes.validar(plantillaAceptacionControlRequest.getModeloAtaud()));
		envioDatos.put("numInventarios", ConsultaConstantes.validar(plantillaAceptacionControlRequest.getNumInventarios()));
		envioDatos.put("nomFinado", ConsultaConstantes.validar(plantillaAceptacionControlRequest.getNomFinado()));
		envioDatos.put(ConsultaConstantes.RESPONSABLE_ALMACEN, ConsultaConstantes.validar(plantillaAceptacionControlRequest.getNomResponsableAlmacen()));
		envioDatos.put("matriculaResponSable",ConsultaConstantes.validar( plantillaAceptacionControlRequest.getClaveResponsableAlmacen()));
		envioDatos.put("contratante", ConsultaConstantes.validar(plantillaAceptacionControlRequest.getNomContratante()));
		envioDatos.put("administrador", ConsultaConstantes.validar(plantillaAceptacionControlRequest.getNomAdministrador()));
		envioDatos.put("matriculaAdministrador", ConsultaConstantes.validar(plantillaAceptacionControlRequest.getClaveAdministrador()));
		envioDatos.put("lugar", ConsultaConstantes.validar(plantillaAceptacionControlRequest.getLugar()));
		envioDatos.put("dia", plantillaAceptacionControlRequest.getDia());
		envioDatos.put("mes", ConsultaConstantes.validar(plantillaAceptacionControlRequest.getMes()));
		envioDatos.put("anio", plantillaAceptacionControlRequest.getAnio());
		envioDatos.put(ConsultaConstantes.TIPO_REPORTE, plantillaAceptacionControlRequest.getTipoReporte());
		envioDatos.put("rutaNombreReporte", nombrePdfAceptacionControl);
		
		log.info(" TERMINO - generarPlantillaAceptacionControlPDF");
		
		return envioDatos;
	}
	
	public DatosRequest obtenerFolioDonacion(DatosRequest request, String numInventario) {
		log.info(" INICIO - obtenerFolioDonacion");
		SelectQueryUtil queryUtil = new SelectQueryUtil();
		queryUtil.select("SD.ID_DONACION AS folioDonacion").from("SVC_DONACION SD")
		.innerJoin("SVC_ATAUDES_DONADOS SAD", "SD.ID_DONACION = SAD.ID_DONACION").and("SD.IND_ACTIVO = 1")
		.innerJoin("SVT_INVENTARIO_ARTICULO SIA", "SAD.ID_INVE_ARTICULO = SIA.ID_INVE_ARTICULO")
		.where("SIA.CVE_FOLIO_ARTICULO = :folioArticulo").setParameter("folioArticulo", numInventario);
		final String query = queryUtil.build();
		String encoded = DatatypeConverter.printBase64Binary(query.getBytes(StandardCharsets.UTF_8));
		request.getDatos().put(AppConstantes.QUERY, encoded);
		log.info(" TERMINO - obtenerFolioDonacion" + query );
		return request;
	}
	
}
