package com.imss.sivimss.donaciones.beans;

import java.nio.charset.StandardCharsets;
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
import com.imss.sivimss.donaciones.util.ConsultaConstantes;
import com.imss.sivimss.donaciones.util.DatosRequest;
import com.imss.sivimss.donaciones.util.QueryHelper;
import com.imss.sivimss.donaciones.util.SelectQueryUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SalidaDonacion {

	public DatosRequest detalleContratanteRfc(DatosRequest request, DonacionRequest donacionRequest) {
		log.info(" INICIO - detalleContratanteRfc");
		String query = ConsultaConstantes.detalleContratante().where("P.CVE_RFC = :rfc").setParameter("rfc", donacionRequest.getRfc()).build();
		log.info(" detalleContratanteRfc: " + query );
		String encoded = DatatypeConverter.printBase64Binary(query.getBytes(StandardCharsets.UTF_8));
		request.getDatos().put(AppConstantes.QUERY, encoded);
		log.info(" TERMINO - detalleContratanteRfc");
		return request;
	}
	
	public DatosRequest detalleContratanteCurp(DatosRequest request, DonacionRequest donacionRequest) {
		log.info(" INICIO - detalleContratanteCurp");
		String query = ConsultaConstantes.detalleContratante().where("P.CVE_CURP = :curp").setParameter("curp", donacionRequest.getCurp()).build();
		log.info(" detalleContratanteCurp: " + query );
		String encoded = DatatypeConverter.printBase64Binary(query.getBytes(StandardCharsets.UTF_8));
		request.getDatos().put(AppConstantes.QUERY, encoded);
		log.info(" TERMINO - detalleContratanteCurp");
		return request;
	}
	
	public DatosRequest detalleSalidaAtaudDonado(DatosRequest request, UsuarioDto usuarioDto) {
		log.info(" INICIO - detalleSalidaAtaudDonado");
		SelectQueryUtil queryUtil = new SelectQueryUtil();
		queryUtil.select("S.CVE_FOLIO_ARTICULO AS folioArticulo","S.ID_INVE_ARTICULO AS idInventarioArticulo","TM.DES_TIPO_MATERIAL AS desTipoMaterial",
				"CONCAT_WS('-',S.CVE_FOLIO_ARTICULO,A.DES_MODELO_ARTICULO ) AS  desModeloArticulo")
		.from("SVT_ORDEN_ENTRADA OE")
		.innerJoin("SVT_CONTRATO C", "OE.ID_CONTRATO = C.ID_CONTRATO")
		.innerJoin("SVT_INVENTARIO_ARTICULO S","OE.ID_ODE = S.ID_ODE")
		.innerJoin("SVT_ARTICULO A", "S.ID_ARTICULO = A.ID_ARTICULO").and("S.ID_TIPO_ASIGNACION_ART = 3").and("A.IND_ACTIVO = 1")
		.innerJoin("SVC_CATEGORIA_ARTICULO CA", "A.ID_CATEGORIA_ARTICULO = CA.ID_CATEGORIA_ARTICULO").and("A.ID_CATEGORIA_ARTICULO = 1")
		.innerJoin("SVC_TIPO_ARTICULO TA", "A.ID_TIPO_ARTICULO = TA.ID_TIPO_ARTICULO").and("A.ID_TIPO_ARTICULO = 1")
		.innerJoin("SVC_TIPO_MATERIAL TM", "A.ID_TIPO_MATERIAL = TM.ID_TIPO_MATERIAL")
		.where("C.ID_VELATORIO = :idVelatorio").setParameter("idVelatorio", ConsultaConstantes.getIdVelatorio(usuarioDto.getIdVelatorio()));
		final String query = queryUtil.build();
		log.info(" detalleSalidaAtaudDonado: " + query );
		String encoded = DatatypeConverter.printBase64Binary(query.getBytes(StandardCharsets.UTF_8));
		request.getDatos().put(AppConstantes.QUERY, encoded);
		log.info(" TERMINO - detalleSalidaAtaudDonado");
		return request;
	}
	
	public DatosRequest countSalidaAtaudDonado(DatosRequest request, AgregarArticuloRequest agregarArticuloRequest) {
		log.info(" INICIO - countSalidaAtaudDonado");
		SelectQueryUtil queryUtil = new SelectQueryUtil();
		queryUtil.select("COUNT(*) AS numArticulo")
		.from("SVT_ORDEN_ENTRADA OE")
		.innerJoin("SVT_INVENTARIO_ARTICULO S", "OE.ID_ODE = S.ID_ODE")
		.innerJoin("SVT_ARTICULO A", "S.ID_ARTICULO = A.ID_ARTICULO").and("S.ID_TIPO_ASIGNACION_ART = 3").and("S.IND_DEVOLUCION IS NULL").and("A.IND_ACTIVO = 1")
		.where("A.DES_MODELO_ARTICULO = :modeloArticlo").setParameter("modeloArticlo", agregarArticuloRequest.getModeloArticulo());
		final String query = queryUtil.build();
		log.info(" countSalidaAtaudDonado: " + query );
		String encoded = DatatypeConverter.printBase64Binary(query.getBytes(StandardCharsets.UTF_8));
		request.getDatos().put(AppConstantes.QUERY, encoded);
		log.info(" TERMINO - countSalidaAtaudDonado");
		return request;
	}
	
	public InsertMultiNivelRequest insertPersona(DonacionRequest donacionRequest, UsuarioDto usuarioDto) {
		log.info(" INICIO - insertPersona");
		InsertMultiNivelRequest convertirQuery = new InsertMultiNivelRequest();
		List<String> unoAuno = new ArrayList<>();
		List<String> unoAn = new ArrayList<>();

		final QueryHelper q = new QueryHelper("INSERT INTO SVC_PERSONA");
		q.agregarParametroValues("CVE_RFC", SelectQueryUtil.setValor(donacionRequest.getRfc()));
		q.agregarParametroValues("CVE_CURP", SelectQueryUtil.setValor( donacionRequest.getCurp()));
		q.agregarParametroValues("CVE_NSS", SelectQueryUtil.setValor(donacionRequest.getNss()));
		q.agregarParametroValues("NOM_PERSONA", SelectQueryUtil.setValor(donacionRequest.getNomPersona()));
		q.agregarParametroValues(ConsultaConstantes.NOM_PRIMER_APELLIDO, SelectQueryUtil.setValor(donacionRequest.getNomPersonaPaterno()));
		q.agregarParametroValues(ConsultaConstantes.NOM_SEGUNDO_APELLIDO, SelectQueryUtil.setValor(donacionRequest.getNomPersonaMaterno()));
		q.agregarParametroValues("NUM_SEXO", String.valueOf(donacionRequest.getNumSexo()));
		q.agregarParametroValues("DES_OTRO_SEXO", SelectQueryUtil.setValor(donacionRequest.getDesOtroSexo()));
		q.agregarParametroValues("FEC_NAC",  SelectQueryUtil.setValor( donacionRequest.getFecNacimiento()));
		q.agregarParametroValues("ID_PAIS", String.valueOf(donacionRequest.getIdPais()));
		q.agregarParametroValues("ID_ESTADO", String.valueOf(donacionRequest.getIdEstado()));
		q.agregarParametroValues("DES_TELEFONO",  SelectQueryUtil.setValor(donacionRequest.getDesTelefono()));
		q.agregarParametroValues("DES_CORREO", SelectQueryUtil.setValor( donacionRequest.getDesCorreo()));
		q.agregarParametroValues("TIP_PERSONA", SelectQueryUtil.setValor( donacionRequest.getTipoPersona()));
		q.agregarParametroValues(ConsultaConstantes.ID_USUARIO_ALTA, String.valueOf(usuarioDto.getIdUsuario()));
		q.agregarParametroValues(ConsultaConstantes.FEC_ALTA, ConsultaConstantes.CURRENT_TIMESTAMP);
		
		unoAuno.add(DatatypeConverter.printBase64Binary(q.obtenerQueryInsertar().getBytes(StandardCharsets.UTF_8)));
		unoAuno.add(insertDomicilio(donacionRequest, usuarioDto));
		unoAuno.add(insertContratante(donacionRequest, usuarioDto));
		unoAuno.add(insertSalidaDonacion(donacionRequest, usuarioDto));
		
		unoAn.addAll(insertSalidaDonacionAtaudes(donacionRequest, usuarioDto));
		if(!donacionRequest.getAgregarFinados().isEmpty()) {
			unoAn.addAll(insertSalidaDonacionFinados(donacionRequest, usuarioDto));
		}
		 convertirQuery.setUnoAuno(unoAuno);
		 
		 convertirQuery.setUnoAn(unoAn);
		 
		 convertirQuery.setId(ConsultaConstantes.ID_TABLA);
		 
		 log.info(" insertPersona: " + convertirQuery.toString() );
		 
		 log.info(" TERMINO - insertPersona");
		 
		return convertirQuery;
	}
	
	public InsertMultiNivelRequest insertSalidaDonacionPrincipal(DonacionRequest donacionRequest, UsuarioDto usuarioDto) {
		log.info(" INICIO - insertSalidaDonacionPrincipal");
		InsertMultiNivelRequest convertirQuery = new InsertMultiNivelRequest();
		List<String> unoAuno = new ArrayList<>();
		List<String> unoAn = new ArrayList<>();
		
		unoAuno.add(insertSalidaDonacion(donacionRequest, usuarioDto));
		
		unoAn.addAll(insertSalidaDonacionAtaudes(donacionRequest, usuarioDto));
		if(!donacionRequest.getAgregarFinados().isEmpty()) {
			unoAn.addAll(insertSalidaDonacionFinados(donacionRequest, usuarioDto));
		}
		
		convertirQuery.setUnoAuno(unoAuno);
		 
		 convertirQuery.setUnoAn(unoAn);
		 
		 convertirQuery.setId(ConsultaConstantes.ID_TABLA);
		 
		 log.info(" insertSalidaDonacionPrincipal: " + convertirQuery.toString() );
		 
		 log.info(" TERMINO - insertSalidaDonacionPrincipal");
		 
		return convertirQuery;
		
	}
	
	public String insertDomicilio(DonacionRequest donacionRequest, UsuarioDto usuarioDto) {
		log.info(" INICIO - insertDomicilio");
		final QueryHelper q = new QueryHelper("INSERT INTO SVT_DOMICILIO");
		q.agregarParametroValues("DES_CALLE", SelectQueryUtil.setValor(donacionRequest.getDesCalle()));
		q.agregarParametroValues("NUM_EXTERIOR", SelectQueryUtil.setValor(donacionRequest.getNumExterior()));
		q.agregarParametroValues("NUM_INTERIOR", SelectQueryUtil.setValor( donacionRequest.getNumInterior()));
		q.agregarParametroValues("DES_CP", String.valueOf(donacionRequest.getDesCodigoPostal()));
		q.agregarParametroValues("DES_COLONIA", SelectQueryUtil.setValor(donacionRequest.getDesColonia()));
		q.agregarParametroValues("DES_MUNICIPIO", SelectQueryUtil.setValor(donacionRequest.getDesMunicipio()));
		q.agregarParametroValues("DES_ESTADO", SelectQueryUtil.setValor(donacionRequest.getDesEstado()));
		q.agregarParametroValues(ConsultaConstantes.ID_USUARIO_ALTA, String.valueOf(usuarioDto.getIdUsuario()));
		q.agregarParametroValues(ConsultaConstantes.FEC_ALTA, ConsultaConstantes.CURRENT_TIMESTAMP);
		log.info(" TERMINO - insertDomicilio");
        return DatatypeConverter.printBase64Binary(q.obtenerQueryInsertar().getBytes(StandardCharsets.UTF_8));
	}

	public String insertContratante(DonacionRequest donacionRequest, UsuarioDto usuarioDto) {
		log.info(" INICIO - insertContratante");
		final QueryHelper q = new QueryHelper("INSERT INTO SVC_CONTRATANTE");
		q.agregarParametroValues("ID_PERSONA", "idTabla1");
		q.agregarParametroValues("CVE_MATRICULA", SelectQueryUtil.setValor(donacionRequest.getClaveMatricula()));
		q.agregarParametroValues("ID_DOMICILIO", "idTabla2");
		q.agregarParametroValues("IND_ACTIVO", String.valueOf(1));
		q.agregarParametroValues(ConsultaConstantes.ID_USUARIO_ALTA, String.valueOf(usuarioDto.getIdUsuario()));
		q.agregarParametroValues(ConsultaConstantes.FEC_ALTA, ConsultaConstantes.CURRENT_TIMESTAMP);
		log.info(" TERMINO - insertContratante");
        return DatatypeConverter.printBase64Binary(q.obtenerQueryInsertar().getBytes(StandardCharsets.UTF_8));
	}

	public String insertSalidaDonacion(DonacionRequest donacionRequest, UsuarioDto usuarioDto) {
		log.info(" INICIO - insertSalidaDonacion");
		final QueryHelper q = new QueryHelper("INSERT INTO SVC_SALIDA_DONACION");
		q.agregarParametroValues("ID_CONTRATANTE", (donacionRequest.getIdContratante() != null) ? String.valueOf(donacionRequest.getIdContratante()):ConsultaConstantes.ID_TABLA);
		q.agregarParametroValues("DES_INSTITUCION", SelectQueryUtil.setValor(donacionRequest.getNomInstitucion()));
		q.agregarParametroValues("NUM_TOTAL_ATAUDES", String.valueOf(donacionRequest.getNumTotalAtaudes()));
		q.agregarParametroValues("IND_ESTUDIO_SOCIECONOMICO",String.valueOf(donacionRequest.getEstudioSocieconomico()));
		q.agregarParametroValues("IND_ESTUDIO_LIBRE", String.valueOf(donacionRequest.getEstudioLibre()));
		q.agregarParametroValues("FEC_SOLICITUD", SelectQueryUtil.setValor( donacionRequest.getFecSolicitad()));
		q.agregarParametroValues("DES_RESPONSABLE_ALMACEN", SelectQueryUtil.setValor(SelectQueryUtil.eliminarEspacios(donacionRequest.getResponsableAlmacen())));
		q.agregarParametroValues("CVE_MATRICULA_RESPONSABLE", SelectQueryUtil.setValor(donacionRequest.getMatricularesponsable()));
		q.agregarParametroValues("IND_ACTIVO", String.valueOf(1));
		q.agregarParametroValues(ConsultaConstantes.ID_USUARIO_ALTA, String.valueOf(usuarioDto.getIdUsuario()));
		q.agregarParametroValues(ConsultaConstantes.FEC_ALTA, ConsultaConstantes.CURRENT_TIMESTAMP);
		log.info(" TERMINO - insertSalidaDonacion");
		return DatatypeConverter.printBase64Binary(q.obtenerQueryInsertar().getBytes(StandardCharsets.UTF_8));
	}

	public List<String> insertSalidaDonacionAtaudes(DonacionRequest donacionRequest, UsuarioDto usuarioDto) {
		log.info(" INICIO - insertSalidaDonacionAtaudes");
		List<String> unoAn = new ArrayList<>();
		donacionRequest.getAtaudesDonados().forEach(agregarArticuloRequest -> {
			final QueryHelper q = new QueryHelper("INSERT INTO SVC_SALIDA_DONACION_ATAUDES");
			q.agregarParametroValues("ID_SALIDA_DONACION", ConsultaConstantes.ID_TABLA);
			q.agregarParametroValues("ID_INVE_ARTICULO", String.valueOf(agregarArticuloRequest.getIdInventarioArticulo()));
			q.agregarParametroValues(ConsultaConstantes.ID_USUARIO_ALTA, String.valueOf(usuarioDto.getIdUsuario()));
			q.agregarParametroValues(ConsultaConstantes.FEC_ALTA, ConsultaConstantes.CURRENT_TIMESTAMP);
			unoAn.add(DatatypeConverter.printBase64Binary(q.obtenerQueryInsertar().getBytes(StandardCharsets.UTF_8)));
		});
		log.info(" TERMINO - insertSalidaDonacionAtaudes");
		return unoAn;
	}

	public List<String>  insertSalidaDonacionFinados(DonacionRequest donacionRequest, UsuarioDto usuarioDto) {
		log.info(" INICIO - insertSalidaDonacionFinados");
		List<String> unoAn = new ArrayList<>();
		donacionRequest.getAgregarFinados().forEach(agregarArticuloRequest -> {
			final QueryHelper q = new QueryHelper("INSERT INTO SVC_SALIDA_DONACION_FINADOS");
			q.agregarParametroValues("ID_SALIDA_DONACION", ConsultaConstantes.ID_TABLA);
			q.agregarParametroValues("NOM_FINADO", SelectQueryUtil.setValor(agregarArticuloRequest.getNomFinado()));
			q.agregarParametroValues(ConsultaConstantes.NOM_PRIMER_APELLIDO, SelectQueryUtil.setValor( agregarArticuloRequest.getNomFinadoPaterno()));
			q.agregarParametroValues(ConsultaConstantes.NOM_SEGUNDO_APELLIDO, SelectQueryUtil.setValor(agregarArticuloRequest.getNomFinadoMaterno()));
			q.agregarParametroValues(ConsultaConstantes.ID_USUARIO_ALTA, String.valueOf(usuarioDto.getIdUsuario()));
			q.agregarParametroValues(ConsultaConstantes.FEC_ALTA, ConsultaConstantes.CURRENT_TIMESTAMP);
			unoAn.add(DatatypeConverter.printBase64Binary(q.obtenerQueryInsertar().getBytes(StandardCharsets.UTF_8)));
		});
		log.info(" TERMINO - insertSalidaDonacionFinados");
		return unoAn;
	}
	
	public ActualizarMultiRequest actualizarSalidaDonacionPrincipal(DonacionRequest donacionRequest, UsuarioDto usuarioDto) {
		log.info(" INICIO - actualizarSalidaDonacionPrincipal");
		ActualizarMultiRequest actualizarMultiRequest = new ActualizarMultiRequest();
		List<String> updates = new ArrayList<>();
		if (donacionRequest.getIdContratante() != null) {
			final QueryHelper q = new QueryHelper("UPDATE SVC_PERSONA ");
			q.agregarParametroValues("CVE_RFC", SelectQueryUtil.setValor(donacionRequest.getRfc()));
			q.agregarParametroValues("CVE_CURP", SelectQueryUtil.setValor(donacionRequest.getCurp()));
			q.agregarParametroValues("CVE_NSS", SelectQueryUtil.setValor(donacionRequest.getNss()));
			q.agregarParametroValues("NOM_PERSONA", SelectQueryUtil.setValor(donacionRequest.getNomPersona()));
			q.agregarParametroValues(ConsultaConstantes.NOM_PRIMER_APELLIDO, SelectQueryUtil.setValor(donacionRequest.getNomPersonaPaterno()));
			q.agregarParametroValues(ConsultaConstantes.NOM_SEGUNDO_APELLIDO, SelectQueryUtil.setValor(donacionRequest.getNomPersonaMaterno()));
			q.agregarParametroValues("NUM_SEXO", String.valueOf(donacionRequest.getNumSexo()));
			q.agregarParametroValues("DES_OTRO_SEXO", SelectQueryUtil.setValor(donacionRequest.getDesOtroSexo()));
			q.agregarParametroValues("FEC_NAC",  SelectQueryUtil.setValor(donacionRequest.getFecNacimiento()));
			q.agregarParametroValues("ID_PAIS", String.valueOf(donacionRequest.getIdPais()));
			q.agregarParametroValues("ID_ESTADO", String.valueOf(donacionRequest.getIdEstado()));
			q.agregarParametroValues("DES_TELEFONO", SelectQueryUtil.setValor(donacionRequest.getDesTelefono() ));
			q.agregarParametroValues("DES_CORREO", SelectQueryUtil.setValor(donacionRequest.getDesCorreo()));
			q.agregarParametroValues("TIP_PERSONA", SelectQueryUtil.setValor( donacionRequest.getTipoPersona()));
			q.agregarParametroValues(ConsultaConstantes.ID_USUARIO_MODIFICA, String.valueOf(usuarioDto.getIdUsuario()));
			q.agregarParametroValues(ConsultaConstantes.FEC_ACTUALIZACION, ConsultaConstantes.CURRENT_TIMESTAMP);
			q.addWhere("ID_PERSONA = " + donacionRequest.getIdPersona());
			
			String query = q.obtenerQueryActualizar();
			log.info(" actualizarPersona: " + query);
			updates.add(DatatypeConverter.printBase64Binary(q.obtenerQueryActualizar().getBytes(StandardCharsets.UTF_8)));
			actualizarMultiRequest.setUpdates(updates);
			
			final QueryHelper q1 = new QueryHelper("UPDATE SVT_DOMICILIO ");
			q1.agregarParametroValues("DES_CALLE", SelectQueryUtil.setValor( donacionRequest.getDesCalle()));
			q1.agregarParametroValues("NUM_EXTERIOR", SelectQueryUtil.setValor(donacionRequest.getNumExterior()));
			q1.agregarParametroValues("NUM_INTERIOR", SelectQueryUtil.setValor(donacionRequest.getNumInterior()));
			q1.agregarParametroValues("DES_CP", String.valueOf(donacionRequest.getDesCodigoPostal()));
			q1.agregarParametroValues("DES_COLONIA", SelectQueryUtil.setValor(donacionRequest.getDesColonia()));
			q1.agregarParametroValues("DES_MUNICIPIO", SelectQueryUtil.setValor(donacionRequest.getDesMunicipio()));
			q1.agregarParametroValues("DES_ESTADO", SelectQueryUtil.setValor(donacionRequest.getDesEstado()));
			q1.agregarParametroValues(ConsultaConstantes.ID_USUARIO_MODIFICA, String.valueOf(usuarioDto.getIdUsuario()));
			q1.agregarParametroValues(ConsultaConstantes.FEC_ACTUALIZACION, ConsultaConstantes.CURRENT_TIMESTAMP);
			q1.addWhere("ID_DOMICILIO = " + donacionRequest.getIdDomicilio());
			
			String query1 = q1.obtenerQueryActualizar();
			log.info(" actualizarDomicilio: " + query1);
			updates.add(DatatypeConverter.printBase64Binary(q1.obtenerQueryActualizar().getBytes(StandardCharsets.UTF_8)));
			actualizarMultiRequest.setUpdates(updates);
			
		}
		
        donacionRequest.getAtaudesDonados().forEach(agregarArticuloRequest -> {
        	final QueryHelper q = new QueryHelper("UPDATE SVT_INVENTARIO_ARTICULO " );
        	q.agregarParametroValues("ID_TIPO_ASIGNACION_ART",  String.valueOf(4));
        	q.agregarParametroValues(ConsultaConstantes.ID_USUARIO_MODIFICA, String.valueOf(usuarioDto.getIdUsuario()));
    		q.agregarParametroValues(ConsultaConstantes.FEC_ACTUALIZACION, ConsultaConstantes.CURRENT_TIMESTAMP);
    		q.addWhere(" ID_INVE_ARTICULO = " + agregarArticuloRequest.getIdInventarioArticulo());
        	updates.add(DatatypeConverter.printBase64Binary(q.obtenerQueryActualizar().getBytes(StandardCharsets.UTF_8)));
        });
        actualizarMultiRequest.setUpdates(updates);
        
        log.info(" actualizarSalidaDonacionPrincipal: " + actualizarMultiRequest.toString() );

        log.info(" TERMINO - actualizarSalidaDonacionPrincipal");
        
		return actualizarMultiRequest;
    }
	
	public Map<String, Object> generarPlantillaControlSalidaDonacionPDF(PlantillaControlSalidaDonacionRequest plantillaControlSalidaRequest, String nombrePdfControlSalida) {
		log.info(" INICIO - generarPlantillaControlSalidaDonacionPDF");
		
		Map<String, Object> envioDatos = new HashMap<>();
		
		envioDatos.put("version", plantillaControlSalidaRequest.getVersion());
		envioDatos.put("ooadNom", ConsultaConstantes.validar(plantillaControlSalidaRequest.getOoadNom()));
		envioDatos.put("velatorio", plantillaControlSalidaRequest.getVelatorioId());
		envioDatos.put("velatorioNom", ConsultaConstantes.validar(plantillaControlSalidaRequest.getVelatorioNom()));
		envioDatos.put("numAtaudes", plantillaControlSalidaRequest.getNumAtaudes());
		envioDatos.put("modeloAtaud", ConsultaConstantes.validar(plantillaControlSalidaRequest.getModeloAtaud()));
		envioDatos.put("tipoAtaud", ConsultaConstantes.validar(plantillaControlSalidaRequest.getTipoAtaud()));
		envioDatos.put("numInventarios", ConsultaConstantes.validar(plantillaControlSalidaRequest.getNumInventarios()));
		envioDatos.put("nomSolicitantes", ConsultaConstantes.validar(plantillaControlSalidaRequest.getNomSolicitantes()));
		envioDatos.put("nomFinados", ConsultaConstantes.validar(plantillaControlSalidaRequest.getNomFinados()));
		envioDatos.put("fecSolicitud", ConsultaConstantes.validar(plantillaControlSalidaRequest.getFecSolicitud()));
		envioDatos.put(ConsultaConstantes.RESPONSABLE_ALMACEN, ConsultaConstantes.validar(plantillaControlSalidaRequest.getNomResponsableAlmacen()));
		envioDatos.put("matriculaResponSable", ConsultaConstantes.validar(plantillaControlSalidaRequest.getClaveResponsableAlmacen()));
		envioDatos.put("solicitante", ConsultaConstantes.validar(plantillaControlSalidaRequest.getNomSolicitante()));
		envioDatos.put("administrador", ConsultaConstantes.validar(plantillaControlSalidaRequest.getNomAdministrador()));
		envioDatos.put("matriculaAdministrador", ConsultaConstantes.validar(plantillaControlSalidaRequest.getClaveAdministrador()));
		envioDatos.put("lugar", ConsultaConstantes.validar(plantillaControlSalidaRequest.getLugar()));
		envioDatos.put("dia", plantillaControlSalidaRequest.getDia());
		envioDatos.put("mes", ConsultaConstantes.validar(plantillaControlSalidaRequest.getMes()));
		envioDatos.put("anio", plantillaControlSalidaRequest.getAnio());
		envioDatos.put(ConsultaConstantes.TIPO_REPORTE, plantillaControlSalidaRequest.getTipoReporte());
		envioDatos.put("rutaNombreReporte", nombrePdfControlSalida);
		
		log.info(" TERMINO - generarPlantillaControlSalidaDonacionPDF");

		return envioDatos;
	}

}
