package com.imss.sivimss.donaciones.beans;

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

public class Donacion {

	public DatosRequest detalleNombreContratante(DatosRequest request, DonacionRequest donacionRequest) {
		SelectQueryUtil queryUtil = new SelectQueryUtil();
		queryUtil.select("OS.ID_ORDEN_SERVICIO AS idOrdenService",
				"CONCAT_WS(' ',P.NOM_PERSONA,P.NOM_PRIMER_APELLIDO,P.NOM_SEGUNDO_APELLIDO ) AS  nombreContratante")
		.from(ConsultaConstantes.SVC_ORDEN_SERVICIO_OS)
		.innerJoin("SVC_CONTRATANTE C", "OS.ID_CONTRATANTE = C.ID_CONTRATANTE")
		.innerJoin("SVC_PERSONA P", "P.ID_PERSONA = C.ID_PERSONA")
		.where(ConsultaConstantes.OS_CVE_FOLIO_CVE_FOLIO).setParameter(ConsultaConstantes.CVE_FOLIO, donacionRequest.getClaveFolio())
		.and(ConsultaConstantes.AND_CVE_ESTATUS).setParameter(ConsultaConstantes.ESTATUS_ORDEN_SERVICIO, donacionRequest.getEstatusOrdenServicio());
		final String query = queryUtil.build();
		String encoded = DatatypeConverter.printBase64Binary(query.getBytes());
		request.getDatos().put(AppConstantes.QUERY, encoded);
		return request;
	}

	public DatosRequest detalleNombreFinado(DatosRequest request, DonacionRequest donacionRequest) {
		SelectQueryUtil queryUtil = new SelectQueryUtil();
		queryUtil.select("OS.ID_ORDEN_SERVICIO AS idOrdenService",
				"CONCAT_WS(' ',P.NOM_PERSONA,P.NOM_PRIMER_APELLIDO,P.NOM_SEGUNDO_APELLIDO ) AS  nombreFinado")
		.from(ConsultaConstantes.SVC_ORDEN_SERVICIO_OS)
		.innerJoin("SVC_FINADO F", "OS.ID_ORDEN_SERVICIO = F.ID_ORDEN_SERVICIO")
		.innerJoin("SVC_PERSONA P", "F.ID_PERSONA = P.ID_PERSONA")
		.where(ConsultaConstantes.OS_CVE_FOLIO_CVE_FOLIO).setParameter(ConsultaConstantes.CVE_FOLIO, donacionRequest.getClaveFolio())
		.and(ConsultaConstantes.AND_CVE_ESTATUS).setParameter(ConsultaConstantes.ESTATUS_ORDEN_SERVICIO, donacionRequest.getEstatusOrdenServicio());
		final String query = queryUtil.build();
		String encoded = DatatypeConverter.printBase64Binary(query.getBytes());
		request.getDatos().put(AppConstantes.QUERY, encoded);
		return request;
	}
	
	public DatosRequest detalleAceptacionDonacion(DatosRequest request, DonacionRequest donacionRequest) {
		SelectQueryUtil queryUtil = new SelectQueryUtil();
		queryUtil.select("CONCAT_WS(' ',U.NOM_USUARIO,U.NOM_APELLIDO_PATERNO,U.NOM_APELLIDO_MATERNO) AS nombreAdministrador",
				"U.CVE_MATRICULA AS matriculaAdministrador","CONCAT_WS(',',V.DES_VELATORIO,D.DES_DELEGACION) AS lugardonacion")
		.from("SVT_USUARIOS U")
		.innerJoin("SVC_VELATORIO V", "U.ID_USUARIO = V.ID_USUARIO_ADMIN")
		.innerJoin("SVC_DELEGACION D", "V.ID_DELEGACION = D.ID_DELEGACION ")
		.where("V.ID_VELATORIO = :idVel").setParameter("idVel", donacionRequest.getIdVelatorio());
		final String query = queryUtil.build();
		String encoded = DatatypeConverter.printBase64Binary(query.getBytes());
		request.getDatos().put(AppConstantes.QUERY, encoded);
		return request;
	}

	public DatosRequest detalleAtaudDonado(DatosRequest request, DonacionRequest donacionRequest, UsuarioDto usuarioDto) {
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
		String encoded = DatatypeConverter.printBase64Binary(query.getBytes());
		request.getDatos().put(AppConstantes.QUERY, encoded);
		return request;
	}

	public DatosRequest insertarDonacion(DonacionRequest donacionRequest, UsuarioDto usuarioDto) {
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
		String encoded = DatatypeConverter.printBase64Binary(query.getBytes());
		parametro.put(AppConstantes.QUERY, encoded);
		parametro.put(ConsultaConstantes.SEPARADOR, "$$");
		parametro.put(ConsultaConstantes.REPLACE, ConsultaConstantes.ID_TABLA);

		request.setDatos(parametro);

		return request;
	}

	public String insertAtaudDonado(DonacionRequest donacionRequest, UsuarioDto usuarioDto) {
		StringBuilder query = new StringBuilder();
		donacionRequest.getAtaudesDonados().forEach(agregarArticuloRequest -> {
			final QueryHelper q = new QueryHelper("INSERT INTO SVC_ATAUDES_DONADOS");
			q.agregarParametroValues("ID_DONACION",ConsultaConstantes. ID_TABLA);
			q.agregarParametroValues(ConsultaConstantes.ID_ARTICULO, String.valueOf(agregarArticuloRequest.getIdArticulo()));
			q.agregarParametroValues(ConsultaConstantes.ID_USUARIO_ALTA, String.valueOf(usuarioDto.getIdUsuario()));
			q.agregarParametroValues(ConsultaConstantes.FEC_ALTA, ConsultaConstantes.CURRENT_TIMESTAMP);
			query.append("$$").append(q.obtenerQueryInsertar());
		});
		
		return query.toString();
	}
	
	public ActualizarMultiRequest actualizarStockArticulo(DonacionRequest donacionRequest, UsuarioDto usuarioDto) {
		ActualizarMultiRequest actualizarMultiRequest = new ActualizarMultiRequest();
		List<String> updates = new ArrayList<>();
        donacionRequest.getAtaudesDonados().forEach(agregarArticuloRequest -> {
        	final QueryHelper q = new QueryHelper("UPDATE SVT_INVENTARIO_ARTICULO " );
        	q.agregarParametroValues("ID_TIPO_ASIGNACION_ART",  String.valueOf(3));
        	q.agregarParametroValues(ConsultaConstantes.ID_USUARIO_MODIFICA, String.valueOf(usuarioDto.getIdUsuario()));
    		q.agregarParametroValues(ConsultaConstantes.FEC_ACTUALIZACION, ConsultaConstantes.CURRENT_TIMESTAMP);
        	q.addWhere(" ID_ARTICULO = " + agregarArticuloRequest.getIdArticulo() + " AND FOLIO_ARTICULO = '" + agregarArticuloRequest.getFolioArticulo().concat("'"));
        	updates.add(DatatypeConverter.printBase64Binary(q.obtenerQueryActualizar().getBytes()));
        });
        actualizarMultiRequest.setUpdates(updates);

		return actualizarMultiRequest;
    }
	
	
	public Map<String, Object> generarPlantillaAceptacionControlPDF(PlantillaAceptacionControlRequest plantillaAceptacionControlRequest, String nombrePdfAceptacionControl) {
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
		envioDatos.put("contratante", plantillaAceptacionControlRequest.getNomContratante());
		envioDatos.put("administrador", plantillaAceptacionControlRequest.getNomAdministrador());
		envioDatos.put("lugar", plantillaAceptacionControlRequest.getLugar());
		envioDatos.put("dia", plantillaAceptacionControlRequest.getDia());
		envioDatos.put("mes", plantillaAceptacionControlRequest.getMes());
		envioDatos.put("anio", plantillaAceptacionControlRequest.getAnio());
		envioDatos.put(ConsultaConstantes.TIPO_REPORTE, plantillaAceptacionControlRequest.getTipoReporte());
		envioDatos.put("rutaNombreReporte", nombrePdfAceptacionControl);

		return envioDatos;
	}
	
}
