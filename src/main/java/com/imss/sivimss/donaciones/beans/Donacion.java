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
import com.imss.sivimss.donaciones.util.DatosRequest;
import com.imss.sivimss.donaciones.util.QueryHelper;

public class Donacion {
	
	private static final String RESPONSABLE_ALMACEN = "responsableAlmacen";
	
	private static final String ID_USUARIO_MODIFICA = "ID_USUARIO_MODIFICA";
	private static final String CURRENT_TIMESTAMP = "CURRENT_TIMESTAMP()";
	private static final String FEC_ACTUALIZACION = "FEC_ACTUALIZACION";
	private static final String AND_CVE_ESTATUS = "' AND OS.CVE_ESTATUS = ";
	private static final String ID_USUARIO_ALTA = "ID_USUARIO_ALTA";
	private static final String ID_ARTICULO = "ID_ARTICULO";
	private static final String TIPO_REPORTE = "tipoReporte";
	private static final String SEPARADOR = "separador";
	private static final String REPLACE = "replace";
	private static final String ID_TABLA = "idTabla";
	private static final String FEC_ALTA = "FEC_ALTA";

	public DatosRequest detalleNombreContratante(DatosRequest request, DonacionRequest donacionRequest) {
		StringBuilder query = new StringBuilder(
				" SELECT OS.ID_ORDEN_SERVICIO AS idOrdenService,  CONCAT_WS(' ',P.NOM_PERSONA,P.NOM_PRIMER_APELLIDO,P.NOM_SEGUNDO_APELLIDO ) AS  nombreContratante "
						.concat(" FROM SVC_ORDEN_SERVICIO OS INNER JOIN SVC_CONTRATANTE C ON OS.ID_CONTRATANTE = C.ID_CONTRATANTE ")
						.concat(" INNER JOIN SVC_PERSONA P ON P.ID_PERSONA = C.ID_PERSONA ")
						.concat("WHERE OS.CVE_FOLIO = '").concat(donacionRequest.getClaveFolio())
						.concat(AND_CVE_ESTATUS) + donacionRequest.getEstatusOrdenServicio());
		String encoded = DatatypeConverter.printBase64Binary(query.toString().getBytes());
		request.getDatos().put(AppConstantes.QUERY, encoded);
		return request;
	}

	public DatosRequest detalleNombreFinado(DatosRequest request, DonacionRequest donacionRequest) {
		StringBuilder query = new StringBuilder(
				" SELECT OS.ID_ORDEN_SERVICIO AS idOrdenService, CONCAT_WS(' ',P.NOM_PERSONA,P.NOM_PRIMER_APELLIDO,P.NOM_SEGUNDO_APELLIDO ) AS  nombreFinado "
						.concat(" FROM SVC_ORDEN_SERVICIO OS INNER JOIN SVC_FINADO F ON OS.ID_ORDEN_SERVICIO = F.ID_ORDEN_SERVICIO ")
						.concat(" INNER JOIN SVC_PERSONA P ON F.ID_PERSONA = P.ID_PERSONA ")
						.concat("WHERE OS.CVE_FOLIO = '").concat(donacionRequest.getClaveFolio())
						.concat(AND_CVE_ESTATUS) + donacionRequest.getEstatusOrdenServicio());
		String encoded = DatatypeConverter.printBase64Binary(query.toString().getBytes());
		request.getDatos().put(AppConstantes.QUERY, encoded);
		return request;
	}

	public DatosRequest detalleAtaudDonado(DatosRequest request, DonacionRequest donacionRequest) {
		StringBuilder query = new StringBuilder(
				" SELECT S.FOLIO_ARTICULO AS folioArticulo, A.ID_ARTICULO AS idArticulo, TM.DES_TIPO_MATERIAL AS desTipoMaterial, A.DES_MODELO_ARTICULO AS desModeloArticulo "
						.concat(" FROM SVC_ORDEN_SERVICIO OS INNER JOIN SVC_CARACTERISTICAS_PRESUPUESTO CP ON OS.ID_ORDEN_SERVICIO  = CP.ID_ORDEN_SERVICIO  ")
						.concat(" AND CP.IND_ACTIVO = 1 INNER JOIN  SVC_DETALLE_CARACTERISTICAS_PRESUPUESTO DCP ")
						.concat(" ON CP.ID_CARACTERISTICAS_PRESUPUESTO = DCP.ID_CARACTERISTICAS_PRESUPUESTO AND DCP.IND_ACTIVO = 1 ")
						.concat(" INNER JOIN SVT_INVENTARIO_ARTICULO S ON DCP.ID_INVE_ARTICULO = S.ID_INVE_ARTICULO  AND S.ID_TIPO_ASIGNACION_ART NOT IN( 3) ")
						.concat(" INNER JOIN SVT_ARTICULO A ON S.ID_ARTICULO = A.ID_ARTICULO AND A.IND_ACTIVO = 1  ")
						.concat(" INNER JOIN SVC_CATEGORIA_ARTICULO CA ON A.ID_CATEGORIA_ARTICULO = CA.ID_CATEGORIA_ARTICULO   ")
						.concat(" AND A.ID_CATEGORIA_ARTICULO = 1 INNER JOIN SVC_TIPO_ARTICULO TA ON A.ID_TIPO_ARTICULO = TA.ID_TIPO_ARTICULO ")
						.concat(" AND A.ID_TIPO_ARTICULO = 1 INNER JOIN SVC_TIPO_MATERIAL TM ON A.ID_TIPO_MATERIAL = TM.ID_TIPO_MATERIAL ")
						.concat(" WHERE OS.CVE_FOLIO = '").concat(donacionRequest.getClaveFolio())
						.concat(AND_CVE_ESTATUS) + donacionRequest.getEstatusOrdenServicio());
		String encoded = DatatypeConverter.printBase64Binary(query.toString().getBytes());
		request.getDatos().put(AppConstantes.QUERY, encoded);
		return request;
	}

	public DatosRequest insertarDonacion(DonacionRequest donacionRequest, UsuarioDto usuarioDto) {
		DatosRequest request = new DatosRequest();
		Map<String, Object> parametro = new HashMap<>();

		final QueryHelper q = new QueryHelper("INSERT INTO SVC_DONACION");
		q.agregarParametroValues("ID_ORDEN_SERVICIO", String.valueOf(donacionRequest.getIdOrdenServicio()));
		q.agregarParametroValues("NUM_TOTAL_ATAUDES", String.valueOf(donacionRequest.getNumTotalAtaudes()));
		q.agregarParametroValues("DES_RESPONSABLE_ALMACEN", "'" + donacionRequest.getResponsableAlmacen() + "'");
		q.agregarParametroValues("DES_MATRICULA_ALMACEN", "'" + donacionRequest.getMatricularesponsable() + "'");
		q.agregarParametroValues(ID_USUARIO_ALTA, String.valueOf(usuarioDto.getIdUsuario()));
		q.agregarParametroValues(FEC_ALTA, CURRENT_TIMESTAMP);

		String query = q.obtenerQueryInsertar() + insertAtaudDonado(donacionRequest, usuarioDto);
		String encoded = DatatypeConverter.printBase64Binary(query.getBytes());
		parametro.put(AppConstantes.QUERY, encoded);
		parametro.put(SEPARADOR, "$$");
		parametro.put(REPLACE, ID_TABLA);

		request.setDatos(parametro);

		return request;
	}

	public String insertAtaudDonado(DonacionRequest donacionRequest, UsuarioDto usuarioDto) {
		StringBuilder query = new StringBuilder();
		donacionRequest.getAtaudesDonados().forEach(agregarArticuloRequest -> {
			final QueryHelper q = new QueryHelper("INSERT INTO SVC_ATAUDES_DONADOS");
			q.agregarParametroValues("ID_DONACION", ID_TABLA);
			q.agregarParametroValues(ID_ARTICULO, String.valueOf(agregarArticuloRequest.getIdArticulo()));
			q.agregarParametroValues(ID_USUARIO_ALTA, String.valueOf(usuarioDto.getIdUsuario()));
			q.agregarParametroValues(FEC_ALTA, CURRENT_TIMESTAMP);
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
        	q.agregarParametroValues(ID_USUARIO_MODIFICA, String.valueOf(usuarioDto.getIdUsuario()));
    		q.agregarParametroValues(FEC_ACTUALIZACION, CURRENT_TIMESTAMP);
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
		envioDatos.put("nomFinado", plantillaAceptacionControlRequest.getNomFinado());
		envioDatos.put(RESPONSABLE_ALMACEN, plantillaAceptacionControlRequest.getNomResponsableAlmacen());
		envioDatos.put("contratante", plantillaAceptacionControlRequest.getNomContratante());
		envioDatos.put("administrador", plantillaAceptacionControlRequest.getNomAdministrador());
		envioDatos.put("lugar", plantillaAceptacionControlRequest.getLugar());
		envioDatos.put("dia", plantillaAceptacionControlRequest.getDia());
		envioDatos.put("mes", plantillaAceptacionControlRequest.getMes());
		envioDatos.put("anio", plantillaAceptacionControlRequest.getAnio());
		envioDatos.put(TIPO_REPORTE, plantillaAceptacionControlRequest.getTipoReporte());
		envioDatos.put("rutaNombreReporte", nombrePdfAceptacionControl);

		return envioDatos;
	}
	
}