package com.imss.sivimss.donaciones.beans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.DatatypeConverter;

import com.imss.sivimss.donaciones.model.request.ActualizarMultiRequest;
import com.imss.sivimss.donaciones.model.request.AgregarArticuloRequest;
import com.imss.sivimss.donaciones.model.request.DonacionRequest;
import com.imss.sivimss.donaciones.model.request.InsertMultiNivelRequest;
import com.imss.sivimss.donaciones.model.request.PlantillaControlSalidaDonacionRequest;
import com.imss.sivimss.donaciones.model.request.UsuarioDto;
import com.imss.sivimss.donaciones.util.AppConstantes;
import com.imss.sivimss.donaciones.util.DatosRequest;
import com.imss.sivimss.donaciones.util.QueryHelper;

public class SalidaDonacion {
	
	private static final String RESPONSABLE_ALMACEN = "responsableAlmacen";
	private static final String ID_USUARIO_MODIFICA = "ID_USUARIO_MODIFICA";
	private static final String CURRENT_TIMESTAMP = "CURRENT_TIMESTAMP()";
	private static final String FEC_ACTUALIZACION = "FEC_ACTUALIZACION";
	private static final String ID_USUARIO_ALTA = "ID_USUARIO_ALTA";
	private static final String TIPO_REPORTE = "tipoReporte";
	private static final String ID_ARTICULO = "ID_ARTICULO";
	private static final String FEC_ALTA = "FEC_ALTA";
	private static final String ID_TABLA = "idTabla";
	
	
	public static final String DETALLECONTRATANTE = "  SELECT P.ID_PERSONA AS idPersona, P.CVE_RFC AS rfc, P.CVE_CURP AS curp, P.CVE_NSS AS nss, "
			.concat(" P.NOM_PERSONA AS nomPersona, P.NOM_PRIMER_APELLIDO AS nomPersonaPaterno, ")
			.concat(" P.NOM_SEGUNDO_APELLIDO AS nomPersonaMaterno, P.NUM_SEXO AS numSexo, P.DES_OTRO_SEXO AS desOtroSexo, P.FEC_NAC AS fecNac, P.ID_PAIS AS idPais,  ")
			.concat(" P.ID_ESTADO AS idEstado, P.DES_TELEFONO AS desTelefono, P.DES_CORREO AS desCorreo, P.TIPO_PERSONA AS tipoPersona, C.ID_CONTRATANTE AS idContratante, ")
			.concat(" C.CVE_MATRICULA AS claveMatricula, D.DES_CALLE AS desCalle, D.NUM_EXTERIOR AS numExterior, D.NUM_INTERIOR AS numInterior, D.ID_CP AS idCodigoPostal, ")
			.concat(" D.DES_COLONIA AS desColonia FROM SVBDQA.SVC_PERSONA P INNER JOIN SVBDQA.SVC_CONTRATANTE C ON P.ID_PERSONA = C.ID_PERSONA ")
	        .concat(" INNER JOIN SVT_DOMICILIO D on C.ID_DOMICILIO = D.ID_DOMICILIO ");
	
	public DatosRequest detalleContratanteRfc(DatosRequest request, DonacionRequest donacionRequest) {
		String query = DETALLECONTRATANTE.concat("  WHERE P.CVE_RFC = '").concat(donacionRequest.getRfc()).concat("'");
		String encoded = DatatypeConverter.printBase64Binary(query.getBytes());
		request.getDatos().put(AppConstantes.QUERY, encoded);
		return request;
	}
	
	public DatosRequest detalleContratanteCurp(DatosRequest request, DonacionRequest donacionRequest) {
		String query = DETALLECONTRATANTE.concat("  WHERE P.CVE_CURP = '").concat(donacionRequest.getCurp()).concat("'");
		String encoded = DatatypeConverter.printBase64Binary(query.getBytes());
		request.getDatos().put(AppConstantes.QUERY, encoded);
		return request;
	}
	
	public DatosRequest detalleSalidaAtaudDonado(DatosRequest request) {
		StringBuilder query = new StringBuilder(
				"  SELECT DISTINCT S.FOLIO_ARTICULO AS folioArticulo,  A.ID_ARTICULO AS idArticulo, TM.DES_TIPO_MATERIAL AS desTipoMaterial, A.DES_MODELO_ARTICULO AS desModeloArticulo "
						.concat(" FROM SVT_ORDEN_ENTRADA OE INNER JOIN  SVT_INVENTARIO_ARTICULO S ON OE.ID_ODE = S.ID_ODE   ")
						.concat("  INNER JOIN SVT_ARTICULO A ON S.ID_ARTICULO = A.ID_ARTICULO AND S.ID_TIPO_ASIGNACION_ART = 3 AND A.IND_ACTIVO = 1  ")
						.concat(" INNER JOIN SVC_CATEGORIA_ARTICULO CA ON A.ID_CATEGORIA_ARTICULO = CA.ID_CATEGORIA_ARTICULO  ")
						.concat("  AND A.ID_CATEGORIA_ARTICULO = 1 INNER JOIN SVC_TIPO_ARTICULO TA ON A.ID_TIPO_ARTICULO = TA.ID_TIPO_ARTICULO ")
						.concat(" AND A.ID_TIPO_ARTICULO = 1 INNER JOIN SVC_TIPO_MATERIAL TM ON A.ID_TIPO_MATERIAL = TM.ID_TIPO_MATERIAL  "));
		String encoded = DatatypeConverter.printBase64Binary(query.toString().getBytes());
		request.getDatos().put(AppConstantes.QUERY, encoded);
		return request;
	}
	
	public DatosRequest countSalidaAtaudDonado(DatosRequest request, AgregarArticuloRequest agregarArticuloRequest) {
		StringBuilder query = new StringBuilder(
				" SELECT COUNT(*) AS numArticulo FROM SVT_ORDEN_ENTRADA OE INNER JOIN  SVT_INVENTARIO_ARTICULO S ON OE.ID_ODE = S.ID_ODE  "
						.concat("  INNER JOIN SVT_ARTICULO A ON S.ID_ARTICULO = A.ID_ARTICULO AND S.ID_TIPO_ASIGNACION_ART = 3   ")
						.concat(" AND S.IND_DEVOLUCION IS NULL AND A.IND_ACTIVO = 1 ")
						.concat(" WHERE A.DES_MODELO_ARTICULO = '").concat(agregarArticuloRequest.getModeloArticulo()).concat("'"));
		String encoded = DatatypeConverter.printBase64Binary(query.toString().getBytes());
		request.getDatos().put(AppConstantes.QUERY, encoded);
		return request;
	}
	
	public InsertMultiNivelRequest insertPersona(DonacionRequest donacionRequest, UsuarioDto usuarioDto) {
		InsertMultiNivelRequest convertirQuery = new InsertMultiNivelRequest();
		List<String> unoAuno = new ArrayList<>();
		List<String> unoAn = new ArrayList<>();

		final QueryHelper q = new QueryHelper("INSERT INTO SVC_PERSONA");
		q.agregarParametroValues("CVE_RFC", "'" + donacionRequest.getRfc() + "'");
		q.agregarParametroValues("CVE_CURP", "'" + donacionRequest.getCurp() + "'");
		q.agregarParametroValues("CVE_NSS", "'" + donacionRequest.getNss() + "'");
		q.agregarParametroValues("NOM_PERSONA", "'" + donacionRequest.getNomPersona() + "'");
		q.agregarParametroValues("NOM_PRIMER_APELLIDO", "'" + donacionRequest.getNomPersonaPaterno() + "'");
		q.agregarParametroValues("NOM_SEGUNDO_APELLIDO", "'" + donacionRequest.getNomPersonaMaterno() + "'");
		q.agregarParametroValues("NUM_SEXO", String.valueOf(donacionRequest.getNumSexo()));
		q.agregarParametroValues("DES_OTRO_SEXO", "'" + donacionRequest.getDesOtroSexo() + "'");
		q.agregarParametroValues("FEC_NAC",  "'" + donacionRequest.getFecNacimiento() + "'");
		q.agregarParametroValues("ID_PAIS", String.valueOf(donacionRequest.getIdPais()));
		q.agregarParametroValues("ID_ESTADO", String.valueOf(donacionRequest.getIdEstado()));
		q.agregarParametroValues("DES_TELEFONO", "'" + donacionRequest.getDesTelefono() + "'");
		q.agregarParametroValues("DES_CORREO", "'" + donacionRequest.getDesCorreo() + "'");
		q.agregarParametroValues("TIPO_PERSONA", "'" + donacionRequest.getTipoPersona() + "'");
		q.agregarParametroValues(ID_USUARIO_ALTA, String.valueOf(usuarioDto.getIdUsuario()));
		q.agregarParametroValues(FEC_ALTA, CURRENT_TIMESTAMP);
		
		unoAuno.add(DatatypeConverter.printBase64Binary(q.obtenerQueryInsertar().getBytes()));
		unoAuno.add(insertDomicilio(donacionRequest, usuarioDto));
		unoAuno.add(insertContratante(donacionRequest, usuarioDto));
		unoAuno.add(insertSalidaDonacion(donacionRequest, usuarioDto));
		
		unoAn.addAll(insertSalidaDonacionAtaudes(donacionRequest, usuarioDto));
		if(!donacionRequest.getAgregarFinados().isEmpty()) {
			unoAn.addAll(insertSalidaDonacionFinados(donacionRequest, usuarioDto));
		}
		 convertirQuery.setUnoAuno(unoAuno);
		 
		 convertirQuery.setUnoAn(unoAn);
		 
		 convertirQuery.setId(ID_TABLA);
		
		return convertirQuery;
	}
	
	public String insertDomicilio(DonacionRequest donacionRequest, UsuarioDto usuarioDto) {
		final QueryHelper q = new QueryHelper("INSERT INTO SVT_DOMICILIO");
		q.agregarParametroValues("DES_CALLE", "'" + donacionRequest.getDesCalle() + "'");
		q.agregarParametroValues("NUM_EXTERIOR", "'" + donacionRequest.getNumExterior() + "'");
		q.agregarParametroValues("NUM_INTERIOR", "'" + donacionRequest.getNumInterior() + "'");
		q.agregarParametroValues("ID_CP", String.valueOf(donacionRequest.getIdCodigoPostal()));
		q.agregarParametroValues("DES_COLONIA", "'" + donacionRequest.getDesColonia() + "'");
		q.agregarParametroValues(ID_USUARIO_ALTA, String.valueOf(usuarioDto.getIdUsuario()));
		q.agregarParametroValues(FEC_ALTA, CURRENT_TIMESTAMP);
        
        return DatatypeConverter.printBase64Binary(q.obtenerQueryInsertar().getBytes());
	}

	public String insertContratante(DonacionRequest donacionRequest, UsuarioDto usuarioDto) {
		final QueryHelper q = new QueryHelper("INSERT INTO SVC_CONTRATANTE");
		q.agregarParametroValues("ID_PERSONA", "idTabla1");
		q.agregarParametroValues("CVE_MATRICULA", "'" + donacionRequest.getClaveMatricula() + "'");
		q.agregarParametroValues("ID_DOMICILIO", "idTabla2");
		q.agregarParametroValues(ID_USUARIO_ALTA, String.valueOf(usuarioDto.getIdUsuario()));
		q.agregarParametroValues(FEC_ALTA, CURRENT_TIMESTAMP);
        
        return DatatypeConverter.printBase64Binary(q.obtenerQueryInsertar().getBytes());
	}

	public String insertSalidaDonacion(DonacionRequest donacionRequest, UsuarioDto usuarioDto) {
		final QueryHelper q = new QueryHelper("INSERT INTO SVC_SALIDA_DONACION");
		q.agregarParametroValues("ID_CONTRATANTE", ID_TABLA);
		q.agregarParametroValues("NOM_INSTITUCION", "'" + donacionRequest.getNomInstitucion() + "'");
		q.agregarParametroValues("NUM_TOTAL_ATAUDES", String.valueOf(donacionRequest.getNumTotalAtaudes()));
		q.agregarParametroValues("INT_ESTUDIO_SOCIECONOMICO",String.valueOf(donacionRequest.getEstudioSocieconomico()));
		q.agregarParametroValues("INT_ESTUDIO_LIBRE", String.valueOf(donacionRequest.getEstudioLibre()));
		q.agregarParametroValues("FEC_SOLICITUD", "'" + donacionRequest.getFecSolicitad() + "'");
		q.agregarParametroValues("NOM_RESPONSABLE_ALMACEN", "'" + donacionRequest.getResponsableAlmacen() + "'");
		q.agregarParametroValues("CVE_MATRICULA_RESPONSABLE", "'" + donacionRequest.getMatricularesponsable() + "'");
		q.agregarParametroValues("IND_ACTIVO", String.valueOf(1));
		q.agregarParametroValues(ID_USUARIO_ALTA, String.valueOf(usuarioDto.getIdUsuario()));
		q.agregarParametroValues(FEC_ALTA, CURRENT_TIMESTAMP);

		return DatatypeConverter.printBase64Binary(q.obtenerQueryInsertar().getBytes());
	}

	public List<String> insertSalidaDonacionAtaudes(DonacionRequest donacionRequest, UsuarioDto usuarioDto) {
		List<String> unoAn = new ArrayList<>();
		donacionRequest.getAtaudesDonados().forEach(agregarArticuloRequest -> {
			final QueryHelper q = new QueryHelper("INSERT INTO SVC_SALIDA_DONACION_ATAUDES");
			q.agregarParametroValues("ID_SALIDA_DONACION", ID_TABLA);
			q.agregarParametroValues(ID_ARTICULO, String.valueOf(agregarArticuloRequest.getIdArticulo()));
			q.agregarParametroValues(ID_USUARIO_ALTA, String.valueOf(usuarioDto.getIdUsuario()));
			q.agregarParametroValues(FEC_ALTA, CURRENT_TIMESTAMP);
			unoAn.add(DatatypeConverter.printBase64Binary(q.obtenerQueryInsertar().getBytes()));
		});

		return unoAn;
	}

	public List<String>  insertSalidaDonacionFinados(DonacionRequest donacionRequest, UsuarioDto usuarioDto) {
		List<String> unoAn = new ArrayList<>();
		donacionRequest.getAgregarFinados().forEach(agregarArticuloRequest -> {
			final QueryHelper q = new QueryHelper("INSERT INTO SVC_SALIDA_DONACION_FINADOS");
			q.agregarParametroValues("ID_SALIDA_DONACION", ID_TABLA);
			q.agregarParametroValues("NOM_FINADO", "'" + agregarArticuloRequest.getNomFinado() + "'");
			q.agregarParametroValues("NOM_PRIMER_APELLIDO", "'" + agregarArticuloRequest.getNomFinadoPaterno() + "'");
			q.agregarParametroValues("NOM_SEGUNDO_APELLIDO", "'" + agregarArticuloRequest.getNomFinadoMaterno() + "'");
			q.agregarParametroValues(ID_USUARIO_ALTA, String.valueOf(usuarioDto.getIdUsuario()));
			q.agregarParametroValues(FEC_ALTA, CURRENT_TIMESTAMP);
			unoAn.add(DatatypeConverter.printBase64Binary(q.obtenerQueryInsertar().getBytes()));
		});

		return unoAn;
	}
	
	public ActualizarMultiRequest actualizarStockArticulo(DonacionRequest donacionRequest, UsuarioDto usuarioDto) {
		ActualizarMultiRequest actualizarMultiRequest = new ActualizarMultiRequest();
		List<String> updates = new ArrayList<>();
        donacionRequest.getAtaudesDonados().forEach(agregarArticuloRequest -> {
        	final QueryHelper q = new QueryHelper("UPDATE SVT_INVENTARIO_ARTICULO " );
        	q.agregarParametroValues("ID_TIPO_ASIGNACION_ART",  String.valueOf(4));
        	q.agregarParametroValues(ID_USUARIO_MODIFICA, String.valueOf(usuarioDto.getIdUsuario()));
    		q.agregarParametroValues(FEC_ACTUALIZACION, CURRENT_TIMESTAMP);
        	q.addWhere(" ID_ARTICULO = " + agregarArticuloRequest.getIdArticulo() + " AND FOLIO_ARTICULO = '" + agregarArticuloRequest.getFolioArticulo().concat("'"));
        	updates.add(DatatypeConverter.printBase64Binary(q.obtenerQueryActualizar().getBytes()));
        });
        actualizarMultiRequest.setUpdates(updates);

		return actualizarMultiRequest;
    }
	
	public Map<String, Object> generarPlantillaControlSalidaDonacionPDF(PlantillaControlSalidaDonacionRequest plantillaControlSalidaRequest, String nombrePdfControlSalida) {
		Map<String, Object> envioDatos = new HashMap<>();
		
		envioDatos.put("version", plantillaControlSalidaRequest.getVersion());
		envioDatos.put("ooadNom", plantillaControlSalidaRequest.getOoadNom());
		envioDatos.put("velatorio", plantillaControlSalidaRequest.getVelatorioId());
		envioDatos.put("velatorioNom", plantillaControlSalidaRequest.getVelatorioNom());
		envioDatos.put("numAtaudes", plantillaControlSalidaRequest.getNumAtaudes());
		envioDatos.put("modeloAtaud", plantillaControlSalidaRequest.getModeloAtaud());
		envioDatos.put("tipoAtaud", plantillaControlSalidaRequest.getTipoAtaud());
		envioDatos.put("numInventarios", plantillaControlSalidaRequest.getNumInventarios());
		envioDatos.put("nomSolicitantes", plantillaControlSalidaRequest.getNomSolicitantes());
		envioDatos.put("nomFinados", plantillaControlSalidaRequest.getNomFinados());
		envioDatos.put("fecSolicitud", plantillaControlSalidaRequest.getFecSolicitud());
		envioDatos.put(RESPONSABLE_ALMACEN, plantillaControlSalidaRequest.getNomResponsableAlmacen());
		envioDatos.put("solicitante", plantillaControlSalidaRequest.getNomSolicitante());
		envioDatos.put("administrador", plantillaControlSalidaRequest.getNomAdministrador());
		envioDatos.put("lugar", plantillaControlSalidaRequest.getLugar());
		envioDatos.put("dia", plantillaControlSalidaRequest.getDia());
		envioDatos.put("mes", plantillaControlSalidaRequest.getMes());
		envioDatos.put("anio", plantillaControlSalidaRequest.getAnio());
		envioDatos.put(TIPO_REPORTE, plantillaControlSalidaRequest.getTipoReporte());
		envioDatos.put("rutaNombreReporte", nombrePdfControlSalida);

		return envioDatos;
	}

}
