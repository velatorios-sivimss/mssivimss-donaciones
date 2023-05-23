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
import com.imss.sivimss.donaciones.util.ConsultaConstantes;
import com.imss.sivimss.donaciones.util.DatosRequest;
import com.imss.sivimss.donaciones.util.QueryHelper;
import com.imss.sivimss.donaciones.util.SelectQueryUtil;

public class SalidaDonacion {
	
	public DatosRequest detalleContratanteRfc(DatosRequest request, DonacionRequest donacionRequest) {
		String query = ConsultaConstantes.detalleContratante().where("P.CVE_RFC = :rfc").setParameter("rfc", donacionRequest.getRfc()).build();
		String encoded = DatatypeConverter.printBase64Binary(query.getBytes());
		request.getDatos().put(AppConstantes.QUERY, encoded);
		return request;
	}
	
	public DatosRequest detalleContratanteCurp(DatosRequest request, DonacionRequest donacionRequest) {
		String query = ConsultaConstantes.detalleContratante().where("P.CVE_CURP = :curp").setParameter("curp", donacionRequest.getCurp()).build();
		String encoded = DatatypeConverter.printBase64Binary(query.getBytes());
		request.getDatos().put(AppConstantes.QUERY, encoded);
		return request;
	}
	
	public DatosRequest detalleSalidaAtaudDonado(DatosRequest request, UsuarioDto usuarioDto) {
		SelectQueryUtil queryUtil = new SelectQueryUtil();
		queryUtil.select("S.FOLIO_ARTICULO AS folioArticulo","A.ID_ARTICULO AS idArticulo","TM.DES_TIPO_MATERIAL AS desTipoMaterial",
				"CONCAT_WS('-',S.FOLIO_ARTICULO,A.DES_MODELO_ARTICULO ) AS  desModeloArticulo")
		.from("SVT_ORDEN_ENTRADA OE")
		.innerJoin("SVT_CONTRATO C", "OE.ID_CONTRATO = C.ID_CONTRATO")
		.innerJoin("SVT_INVENTARIO_ARTICULO S","OE.ID_ODE = S.ID_ODE")
		.innerJoin("SVT_ARTICULO A", "S.ID_ARTICULO = A.ID_ARTICULO").and("S.ID_TIPO_ASIGNACION_ART = 3").and("A.IND_ACTIVO = 1")
		.innerJoin("SVC_CATEGORIA_ARTICULO CA", "A.ID_CATEGORIA_ARTICULO = CA.ID_CATEGORIA_ARTICULO").and("A.ID_CATEGORIA_ARTICULO = 1")
		.innerJoin("SVC_TIPO_ARTICULO TA", "A.ID_TIPO_ARTICULO = TA.ID_TIPO_ARTICULO").and("A.ID_TIPO_ARTICULO = 1")
		.innerJoin("SVC_TIPO_MATERIAL TM", "A.ID_TIPO_MATERIAL = TM.ID_TIPO_MATERIAL")
		.where("C.ID_VELATORIO = :idVelatorio").setParameter("idVelatorio", usuarioDto.getIdVelatorio());
		final String query = queryUtil.build();
		String encoded = DatatypeConverter.printBase64Binary(query.getBytes());
		request.getDatos().put(AppConstantes.QUERY, encoded);
		return request;
	}
	
	public DatosRequest countSalidaAtaudDonado(DatosRequest request, AgregarArticuloRequest agregarArticuloRequest) {
		SelectQueryUtil queryUtil = new SelectQueryUtil();
		queryUtil.select("COUNT(*) AS numArticulo")
		.from("SVT_ORDEN_ENTRADA OE")
		.innerJoin("SVT_INVENTARIO_ARTICULO S", "OE.ID_ODE = S.ID_ODE")
		.innerJoin("SVT_ARTICULO A", "S.ID_ARTICULO = A.ID_ARTICULO").and("S.ID_TIPO_ASIGNACION_ART = 3").and("S.IND_DEVOLUCION IS NULL").and("A.IND_ACTIVO = 1")
		.where("A.DES_MODELO_ARTICULO = :modeloArticlo").setParameter("modeloArticlo", agregarArticuloRequest.getModeloArticulo());
		final String query = queryUtil.build();
		String encoded = DatatypeConverter.printBase64Binary(query.getBytes());
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
		q.agregarParametroValues(ConsultaConstantes.ID_USUARIO_ALTA, String.valueOf(usuarioDto.getIdUsuario()));
		q.agregarParametroValues(ConsultaConstantes.FEC_ALTA, ConsultaConstantes.CURRENT_TIMESTAMP);
		
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
		 
		 convertirQuery.setId(ConsultaConstantes.ID_TABLA);
		
		return convertirQuery;
	}
	
	public String insertDomicilio(DonacionRequest donacionRequest, UsuarioDto usuarioDto) {
		final QueryHelper q = new QueryHelper("INSERT INTO SVT_DOMICILIO");
		q.agregarParametroValues("DES_CALLE", "'" + donacionRequest.getDesCalle() + "'");
		q.agregarParametroValues("NUM_EXTERIOR", "'" + donacionRequest.getNumExterior() + "'");
		q.agregarParametroValues("NUM_INTERIOR", "'" + donacionRequest.getNumInterior() + "'");
		q.agregarParametroValues("DES_CP", String.valueOf(donacionRequest.getDesCodigoPostal()));
		q.agregarParametroValues("DES_COLONIA", "'" + donacionRequest.getDesColonia() + "'");
		q.agregarParametroValues("DES_MUNICIPIO", "'" + donacionRequest.getDesMunicipio()+ "'");
		q.agregarParametroValues("DES_ESTADO", "'" + donacionRequest.getDesEstado() + "'");
		q.agregarParametroValues(ConsultaConstantes.ID_USUARIO_ALTA, String.valueOf(usuarioDto.getIdUsuario()));
		q.agregarParametroValues(ConsultaConstantes.FEC_ALTA, ConsultaConstantes.CURRENT_TIMESTAMP);
        
        return DatatypeConverter.printBase64Binary(q.obtenerQueryInsertar().getBytes());
	}

	public String insertContratante(DonacionRequest donacionRequest, UsuarioDto usuarioDto) {
		final QueryHelper q = new QueryHelper("INSERT INTO SVC_CONTRATANTE");
		q.agregarParametroValues("ID_PERSONA", "idTabla1");
		q.agregarParametroValues("CVE_MATRICULA", "'" + donacionRequest.getClaveMatricula() + "'");
		q.agregarParametroValues("ID_DOMICILIO", "idTabla2");
		q.agregarParametroValues(ConsultaConstantes.ID_USUARIO_ALTA, String.valueOf(usuarioDto.getIdUsuario()));
		q.agregarParametroValues(ConsultaConstantes.FEC_ALTA, ConsultaConstantes.CURRENT_TIMESTAMP);
        
        return DatatypeConverter.printBase64Binary(q.obtenerQueryInsertar().getBytes());
	}

	public String insertSalidaDonacion(DonacionRequest donacionRequest, UsuarioDto usuarioDto) {
		final QueryHelper q = new QueryHelper("INSERT INTO SVC_SALIDA_DONACION");
		q.agregarParametroValues("ID_CONTRATANTE", ConsultaConstantes.ID_TABLA);
		q.agregarParametroValues("DES_INSTITUCION", "'" + donacionRequest.getNomInstitucion() + "'");
		q.agregarParametroValues("NUM_TOTAL_ATAUDES", String.valueOf(donacionRequest.getNumTotalAtaudes()));
		q.agregarParametroValues("IND_ESTUDIO_SOCIECONOMICO",String.valueOf(donacionRequest.getEstudioSocieconomico()));
		q.agregarParametroValues("IND_ESTUDIO_LIBRE", String.valueOf(donacionRequest.getEstudioLibre()));
		q.agregarParametroValues("FEC_SOLICITUD", "'" + donacionRequest.getFecSolicitad() + "'");
		q.agregarParametroValues("DES_RESPONSABLE_ALMACEN", "'" + SelectQueryUtil.eliminarEspacios(donacionRequest.getResponsableAlmacen()) + "'");
		q.agregarParametroValues("CVE_MATRICULA_RESPONSABLE", "'" + donacionRequest.getMatricularesponsable() + "'");
		q.agregarParametroValues("IND_ACTIVO", String.valueOf(1));
		q.agregarParametroValues(ConsultaConstantes.ID_USUARIO_ALTA, String.valueOf(usuarioDto.getIdUsuario()));
		q.agregarParametroValues(ConsultaConstantes.FEC_ALTA, ConsultaConstantes.CURRENT_TIMESTAMP);

		return DatatypeConverter.printBase64Binary(q.obtenerQueryInsertar().getBytes());
	}

	public List<String> insertSalidaDonacionAtaudes(DonacionRequest donacionRequest, UsuarioDto usuarioDto) {
		List<String> unoAn = new ArrayList<>();
		donacionRequest.getAtaudesDonados().forEach(agregarArticuloRequest -> {
			final QueryHelper q = new QueryHelper("INSERT INTO SVC_SALIDA_DONACION_ATAUDES");
			q.agregarParametroValues("ID_SALIDA_DONACION", ConsultaConstantes.ID_TABLA);
			q.agregarParametroValues(ConsultaConstantes.ID_ARTICULO, String.valueOf(agregarArticuloRequest.getIdArticulo()));
			q.agregarParametroValues(ConsultaConstantes.ID_USUARIO_ALTA, String.valueOf(usuarioDto.getIdUsuario()));
			q.agregarParametroValues(ConsultaConstantes.FEC_ALTA, ConsultaConstantes.CURRENT_TIMESTAMP);
			unoAn.add(DatatypeConverter.printBase64Binary(q.obtenerQueryInsertar().getBytes()));
		});

		return unoAn;
	}

	public List<String>  insertSalidaDonacionFinados(DonacionRequest donacionRequest, UsuarioDto usuarioDto) {
		List<String> unoAn = new ArrayList<>();
		donacionRequest.getAgregarFinados().forEach(agregarArticuloRequest -> {
			final QueryHelper q = new QueryHelper("INSERT INTO SVC_SALIDA_DONACION_FINADOS");
			q.agregarParametroValues("ID_SALIDA_DONACION", ConsultaConstantes.ID_TABLA);
			q.agregarParametroValues("NOM_FINADO", "'" + agregarArticuloRequest.getNomFinado() + "'");
			q.agregarParametroValues("NOM_PRIMER_APELLIDO", "'" + agregarArticuloRequest.getNomFinadoPaterno() + "'");
			q.agregarParametroValues("NOM_SEGUNDO_APELLIDO", "'" + agregarArticuloRequest.getNomFinadoMaterno() + "'");
			q.agregarParametroValues(ConsultaConstantes.ID_USUARIO_ALTA, String.valueOf(usuarioDto.getIdUsuario()));
			q.agregarParametroValues(ConsultaConstantes.FEC_ALTA, ConsultaConstantes.CURRENT_TIMESTAMP);
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
        	q.agregarParametroValues(ConsultaConstantes.ID_USUARIO_MODIFICA, String.valueOf(usuarioDto.getIdUsuario()));
    		q.agregarParametroValues(ConsultaConstantes.FEC_ACTUALIZACION, ConsultaConstantes.CURRENT_TIMESTAMP);
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
		envioDatos.put(ConsultaConstantes.RESPONSABLE_ALMACEN, plantillaControlSalidaRequest.getNomResponsableAlmacen());
		envioDatos.put("solicitante", plantillaControlSalidaRequest.getNomSolicitante());
		envioDatos.put("administrador", plantillaControlSalidaRequest.getNomAdministrador());
		envioDatos.put("matriculaAdministrador", plantillaControlSalidaRequest.getClaveAdministrador());
		envioDatos.put("lugar", plantillaControlSalidaRequest.getLugar());
		envioDatos.put("dia", plantillaControlSalidaRequest.getDia());
		envioDatos.put("mes", plantillaControlSalidaRequest.getMes());
		envioDatos.put("anio", plantillaControlSalidaRequest.getAnio());
		envioDatos.put(ConsultaConstantes.TIPO_REPORTE, plantillaControlSalidaRequest.getTipoReporte());
		envioDatos.put("rutaNombreReporte", nombrePdfControlSalida);

		return envioDatos;
	}

}
