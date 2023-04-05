package com.imss.sivimss.donaciones.beans;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.DatatypeConverter;

import com.imss.sivimss.donaciones.model.request.DonacionRequest;
import com.imss.sivimss.donaciones.model.request.RolRequest;
import com.imss.sivimss.donaciones.model.request.UsuarioRequest;
import com.imss.sivimss.donaciones.util.AppConstantes;
import com.imss.sivimss.donaciones.util.DatosRequest;
import com.imss.sivimss.donaciones.util.QueryHelper;

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
public class Donacion {
	
	
	private Integer idRol;
	private String desRol;
	private Integer estatusRol;
	private Integer nivel;
	private String claveFolio;
	private Integer estatusOrdenServicio;
	private String claveAlta;
	private String claveModifica;
	private String claveBaja;
	
	private static final String AND_CVE_ESTATUS = "' AND OS.CVE_ESTATUS = ";
	private static final String CURRENT_TIMESTAMP = "CURRENT_TIMESTAMP()";
	private static final String ID_USUARIO_MODIFICA = "ID_USUARIO_MODIFICA";
	private static final String FEC_ACTUALIZACION = "FEC_ACTUALIZACION";
	private static final String ID_USUARIO_BAJA = "ID_USUARIO_BAJA";
	private static final String UPDATE_SVC_ROL = "UPDATE SVC_ROL";
	private static final String CVE_ESTATUS = "CVE_ESTATUS";
	private static final String FEC_BAJA = "FEC_BAJA";
	private static final String DES_ROL = "DES_ROL";
	private static final String ID_ROL = "ID_ROL = ";
	private static final String NULL = "NULL";


	public Donacion(RolRequest rolRequest) {
		this.idRol = rolRequest.getIdRol();
		this.desRol = rolRequest.getDesRol();
		this.estatusRol = rolRequest.getEstatusRol();
		this.nivel= rolRequest.getNivel();
	}
	public Donacion(UsuarioRequest usuarioRequest) {
		this.nivel= usuarioRequest.getIdOficina();
		this.idRol = usuarioRequest.getIdRol();
	}
	
	public Donacion(DonacionRequest donacionRequest) {
		this.claveFolio = donacionRequest.getClaveFolio();
		this.estatusOrdenServicio = donacionRequest.getEstatusOrdenServicio();
	}
	
	public DatosRequest obtenerRoles(DatosRequest request, String formatoFecha) {
		String query = "SELECT R.ID_ROL AS idRol, R.DES_ROL AS desRol, \r\n "
				+ "NO.ID_OFICINA AS nivelOficina, NO.DES_NIVELOFICINA AS desNivelOficina, date_format(R.FEC_ALTA,'" + formatoFecha+ "') AS fCreacion, \r\n"
				+ "R.CVE_ESTATUS AS estatus FROM SVC_ROL AS R INNER JOIN SVC_NIVEL_OFICINA NO  ON R.ID_OFICINA = NO.ID_OFICINA "
				+ "ORDER BY ID_ROL ASC";
		String encoded = DatatypeConverter.printBase64Binary(query.getBytes());
		request.getDatos().put(AppConstantes.QUERY, encoded);

		return request;
	}

	public DatosRequest buscarFiltrosRol(DatosRequest request, Donacion rol, String formatoFecha) {
		StringBuilder query = new StringBuilder(" SELECT  ID_ROL as idRol, DES_ROL as desRol, NO.ID_OFICINA AS nivelOficina, NO.DES_NIVELOFICINA AS desNivelOficina, "
				+ " R.CVE_ESTATUS AS estatusRol, date_format(R.FEC_ALTA,'"+ formatoFecha+ "') AS fCreacion FROM SVC_ROL AS R "
				+ " INNER JOIN SVC_NIVEL_OFICINA NO  ON R.ID_OFICINA = NO.ID_OFICINA ");
		query.append(" WHERE IFNULL(ID_ROL,0) > 0" );
		if (rol.getNivel() != null) {
			query.append(" AND R.ID_OFICINA = ").append(this.getNivel());
		}
		if (this.getIdRol() != null) {
			query.append(" AND R.ID_ROL = ").append(this.getIdRol());
		}
		
		query.append(" ORDER BY R.ID_ROL DESC");
		
		String encoded = DatatypeConverter.printBase64Binary(query.toString().getBytes());
		request.getDatos().put(AppConstantes.QUERY, encoded);
		return request;
	}
	


	public DatosRequest detalleNombreContratante(DatosRequest request, String formatoFecha) {
		StringBuilder query = new StringBuilder(" SELECT OS.ID_ORDEN_SERVICIO AS idOrdenService,  CONCAT_WS(' ',P.NOM_PERSONA,P.NOM_PRIMER_APELLIDO,P.NOM_SEGUNDO_APELLIDO ) AS  nombreContratante "
				.concat(" FROM SVBDQA.SVC_ORDEN_SERVICIO OS INNER JOIN SVBDQA.SVC_CONTRATANTE C ON OS.ID_CONTRATANTE = C.ID_CONTRATANTE ")
				.concat(" INNER JOIN SVBDQA.SVC_PERSONA P ON P.ID_PERSONA = C.ID_PERSONA ")
				.concat("WHERE OS.CVE_FOLIO = '").concat(this.claveFolio).concat(AND_CVE_ESTATUS) + this.estatusOrdenServicio);
		String encoded = DatatypeConverter.printBase64Binary(query.toString().getBytes());
		request.getDatos().put(AppConstantes.QUERY, encoded);
		return request;
	}
	
	public DatosRequest detalleNombreFinado(DatosRequest request, String formatoFecha) {
		StringBuilder query = new StringBuilder(" SELECT OS.ID_ORDEN_SERVICIO AS idOrdenService, CONCAT_WS(' ',P.NOM_PERSONA,P.NOM_PRIMER_APELLIDO,P.NOM_SEGUNDO_APELLIDO ) AS  nombreFinado "
				.concat(" FROM SVC_ORDEN_SERVICIO OS INNER JOIN SVC_FINADO F ON OS.ID_ORDEN_SERVICIO = F.ID_ORDEN_SERVICIO ")
				.concat(" INNER JOIN SVC_PERSONA P ON F.ID_PERSONA = P.ID_PERSONA ")
				.concat("WHERE OS.CVE_FOLIO = '").concat(this.claveFolio).concat(AND_CVE_ESTATUS) + this.estatusOrdenServicio);
		String encoded = DatatypeConverter.printBase64Binary(query.toString().getBytes());
		request.getDatos().put(AppConstantes.QUERY, encoded);
		return request;
	}
	
	public DatosRequest detalleAtaudDonado(DatosRequest request, String formatoFecha) {
		StringBuilder query = new StringBuilder(" SELECT A.ID_ARTICULO AS idArticulo, TM.DES_TIPO_MATERIAL AS desTipoMaterial, A.DES_MODELO_ARTICULO AS desModeloArticulo "
				.concat("  FROM SVC_ORDEN_SERVICIO OS INNER JOIN SVC_CARACTERISTICAS_PRESUPUESTO CP ON OS.ID_ORDEN_SERVICIO  = CP.ID_ORDEN_SERVICIO ")
				.concat(" AND CP.CVE_ESTATUS = 1 INNER JOIN  SVC_DETALLE_CARACTERISTICAS_PRESUPUESTO DCP ")
				.concat(" ON CP.ID_CARACTERISTICAS_PRESUPUESTO = DCP.ID_CARACTERISTICAS_PRESUPUESTO AND DCP.CVE_ESTATUS = 1 ")
				.concat(" INNER JOIN SVT_ARTICULO A ON DCP.ID_ARTICULO = A.ID_ARTICULO AND A.CVE_ESTATUS = 1 ")
				.concat(" INNER JOIN SVC_CATEGORIA_ARTICULO CA ON A.ID_CATEGORIA_ARTICULO = CA.ID_CATEGORIA_ARTICULO  ")
				.concat(" AND A.ID_CATEGORIA_ARTICULO = 1 INNER JOIN SVC_TIPO_ARTICULO TA ON A.ID_TIPO_ARTICULO = TA.ID_TIPO_ARTICULO ")
				.concat(" AND A.ID_TIPO_ARTICULO = 1 INNER JOIN SVC_TIPO_MATERIAL TM ON A.ID_TIPO_MATERIAL = TM.ID_TIPO_MATERIAL ")
				.concat(" WHERE OS.CVE_FOLIO = '").concat(this.claveFolio).concat(AND_CVE_ESTATUS) + this.estatusOrdenServicio);
		String encoded = DatatypeConverter.printBase64Binary(query.toString().getBytes());
		request.getDatos().put(AppConstantes.QUERY, encoded);
		return request;
	}

	public DatosRequest insertar() {
		DatosRequest request = new DatosRequest();
		Map<String, Object> parametro = new HashMap<>();

		final QueryHelper q = new QueryHelper("INSERT INTO SVC_ROL");
		q.agregarParametroValues(DES_ROL, "'" + this.desRol + "'");
		q.agregarParametroValues(CVE_ESTATUS, "1");
		q.agregarParametroValues("FEC_ALTA", CURRENT_TIMESTAMP);
		q.agregarParametroValues("ID_USUARIO_ALTA", "'" + this.claveAlta + "'");
		q.agregarParametroValues(FEC_ACTUALIZACION, NULL );
		q.agregarParametroValues(ID_USUARIO_MODIFICA, NULL);
		q.agregarParametroValues(ID_USUARIO_BAJA, NULL);
		q.agregarParametroValues(FEC_BAJA , NULL);
		q.agregarParametroValues("ID_OFICINA", "" + this.nivel + "");
		
		String query = q.obtenerQueryInsertar();
		String encoded = DatatypeConverter.printBase64Binary(query.getBytes());
		parametro.put(AppConstantes.QUERY, encoded);
		
		request.setDatos(parametro);
		
		return request;
	}

	public DatosRequest actualizar() {
		DatosRequest request = new DatosRequest();
		Map<String, Object> parametro = new HashMap<>();

		final QueryHelper q = new QueryHelper(UPDATE_SVC_ROL);
		q.agregarParametroValues(DES_ROL, "'" + this.desRol + "'");
		q.agregarParametroValues(CVE_ESTATUS, "" + this.estatusRol +"");
		q.agregarParametroValues("ID_OFICINA", "" + this.nivel + "");
		q.agregarParametroValues(ID_USUARIO_MODIFICA, "'" + this.claveModifica + "'");
		q.agregarParametroValues(FEC_ACTUALIZACION, CURRENT_TIMESTAMP);
		q.addWhere(ID_ROL + this.idRol);
		
		String query = q.obtenerQueryActualizar();
		String encoded = DatatypeConverter.printBase64Binary(query.getBytes());
		parametro.put(AppConstantes.QUERY, encoded);
		request.setDatos(parametro);
		
		return request;
	}
	
	public DatosRequest cambiarEstatus() {
		DatosRequest request = new DatosRequest();
		Map<String, Object> parametro = new HashMap<>();

		final QueryHelper q = new QueryHelper(UPDATE_SVC_ROL);
		q.agregarParametroValues(CVE_ESTATUS, "" + this.estatusRol +"");
		q.agregarParametroValues(ID_USUARIO_MODIFICA, "'" + this.claveModifica + "'");
		q.agregarParametroValues(FEC_ACTUALIZACION, CURRENT_TIMESTAMP);
		q.addWhere(ID_ROL + this.idRol);
		
		String query = q.obtenerQueryActualizar();
		String encoded = DatatypeConverter.printBase64Binary(query.getBytes());
		parametro.put(AppConstantes.QUERY, encoded);
		request.setDatos(parametro);
		
		return request;
	}

}