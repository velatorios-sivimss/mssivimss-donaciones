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
				"U.CVE_MATRICULA AS matriculaAdministrador","CONCAT_WS(',',V.DES_VELATORIO,D.DES_DELEGACION) AS lugardonacion")
		.from("SVT_USUARIOS U")
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
		SelectQueryUtil queryUtil = new SelectQueryUtil();
		queryUtil.select("S.FOLIO_ARTICULO AS folioArticulo","A.ID_ARTICULO AS idArticulo", "TM.DES_TIPO_MATERIAL AS desTipoMaterial",
				"CONCAT_WS('-',S.FOLIO_ARTICULO,A.DES_MODELO_ARTICULO ) AS  desModeloArticulo")
		.from(ConsultaConstantes.SVC_ORDEN_SERVICIO_OS)
		.innerJoin("SVC_CARACTERISTICAS_PRESUPUESTO CP", "OS.ID_ORDEN_SERVICIO  = CP.ID_ORDEN_SERVICIO").and("CP.IND_ACTIVO = 1")
		.innerJoin("SVC_DETALLE_CARACTERISTICAS_PRESUPUESTO DCP", "CP.ID_CARACTERISTICAS_PRESUPUESTO = DCP.ID_CARACTERISTICAS_PRESUPUESTO").and("DCP.IND_ACTIVO = 1")
		.innerJoin("SVT_INVENTARIO_ARTICULO S", "DCP.ID_INVE_ARTICULO = S.ID_INVE_ARTICULO").and("S.ID_TIPO_ASIGNACION_ART NOT IN( 3,4)")
		.innerJoin("SVT_ARTICULO A", "S.ID_ARTICULO = A.ID_ARTICULO").and("A.IND_ACTIVO = 1").and("A.ID_CATEGORIA_ARTICULO = 1").and("A.ID_TIPO_ARTICULO = 1")
		.innerJoin("SVC_TIPO_MATERIAL TM", "A.ID_TIPO_MATERIAL = TM.ID_TIPO_MATERIAL")
		.where(ConsultaConstantes.OS_CVE_FOLIO_CVE_FOLIO).setParameter(ConsultaConstantes.CVE_FOLIO, donacionRequest.getClaveFolio())
		.and(ConsultaConstantes.AND_CVE_ESTATUS).setParameter(ConsultaConstantes.ESTATUS_ORDEN_SERVICIO, donacionRequest.getEstatusOrdenServicio())
		.and("OS.ID_VELATORIO = :idVelatorio").setParameter("idVelatorio", usuarioDto.getIdVelatorio());
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
		q.agregarParametroValues("DES_RESPONSABLE_ALMACEN", "'" +   SelectQueryUtil.eliminarEspacios(donacionRequest.getResponsableAlmacen()) + "'");
		q.agregarParametroValues("DES_MATRICULA_ALMACEN", "'" + donacionRequest.getMatricularesponsable() + "'");
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
			q.agregarParametroValues(ConsultaConstantes.ID_ARTICULO, String.valueOf(agregarArticuloRequest.getIdArticulo()));
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
        	q.agregarParametroValues(ConsultaConstantes.ID_USUARIO_MODIFICA, String.valueOf(usuarioDto.getIdUsuario()));
    		q.agregarParametroValues(ConsultaConstantes.FEC_ACTUALIZACION, ConsultaConstantes.CURRENT_TIMESTAMP);
        	q.addWhere(" ID_ARTICULO = " + agregarArticuloRequest.getIdArticulo() + " AND FOLIO_ARTICULO = '" + agregarArticuloRequest.getFolioArticulo().concat("'"));
        	updates.add(DatatypeConverter.printBase64Binary(q.obtenerQueryActualizar().getBytes(StandardCharsets.UTF_8)));
        });
        actualizarMultiRequest.setUpdates(updates);
        log.info(" TERMINO - actualizarStockArticulo");
		return actualizarMultiRequest;
    }
	
	
	public Map<String, Object> generarPlantillaAceptacionControlPDF(PlantillaAceptacionControlRequest plantillaAceptacionControlRequest, String nombrePdfAceptacionControl) {
		log.info(" INICIO - generarPlantillaAceptacionControlPDF");
		
		Map<String, Object> envioDatos = new HashMap<>();
		
		envioDatos.put("version", plantillaAceptacionControlRequest.getVersion());
		envioDatos.put("ooadNom", plantillaAceptacionControlRequest.getOoadNom());
		envioDatos.put("velatorio", plantillaAceptacionControlRequest.getVelatorioId());
		envioDatos.put("numContrato", plantillaAceptacionControlRequest.getNumContrato());
		envioDatos.put("tipoAtaud", plantillaAceptacionControlRequest.getTipoAtaud());
		envioDatos.put("modeloAtaud", plantillaAceptacionControlRequest.getModeloAtaud());
		envioDatos.put("numInventarios", plantillaAceptacionControlRequest.getNumInventarios());
		envioDatos.put("nomFinado", plantillaAceptacionControlRequest.getNomFinado());
		envioDatos.put(ConsultaConstantes.RESPONSABLE_ALMACEN, plantillaAceptacionControlRequest.getNomResponsableAlmacen());
		envioDatos.put("matriculaResponSable", plantillaAceptacionControlRequest.getClaveResponsableAlmacen());
		envioDatos.put("contratante", plantillaAceptacionControlRequest.getNomContratante());
		envioDatos.put("administrador", plantillaAceptacionControlRequest.getNomAdministrador());
		envioDatos.put("matriculaAdministrador", plantillaAceptacionControlRequest.getClaveAdministrador());
		envioDatos.put("lugar", plantillaAceptacionControlRequest.getLugar());
		envioDatos.put("dia", plantillaAceptacionControlRequest.getDia());
		envioDatos.put("mes", plantillaAceptacionControlRequest.getMes());
		envioDatos.put("anio", plantillaAceptacionControlRequest.getAnio());
		envioDatos.put(ConsultaConstantes.TIPO_REPORTE, plantillaAceptacionControlRequest.getTipoReporte());
		envioDatos.put("rutaNombreReporte", nombrePdfAceptacionControl);
		
		log.info(" TERMINO - generarPlantillaAceptacionControlPDF");
		
		return envioDatos;
	}
	
}
