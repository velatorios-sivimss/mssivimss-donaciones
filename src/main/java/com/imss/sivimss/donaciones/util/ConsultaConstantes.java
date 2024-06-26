package com.imss.sivimss.donaciones.util;

public class ConsultaConstantes {
	
	public static final String OS_CVE_FOLIO_CVE_FOLIO = "OS.CVE_FOLIO = :cveFolio";
	public static final String ESTATUS_ORDEN_SERVICIO = "estatusOrdenServicio";
	public static final String SVC_ORDEN_SERVICIO_OS = "SVC_ORDEN_SERVICIO OS";
	public static final String RESPONSABLE_ALMACEN = "responsableAlmacen";
	public static final String ID_USUARIO_MODIFICA = "ID_USUARIO_MODIFICA";
	public static final String CURRENT_TIMESTAMP = "CURRENT_TIMESTAMP()";
	public static final String FEC_ACTUALIZACION = "FEC_ACTUALIZACION";
	public static final String AND_CVE_ESTATUS = "OS.ID_ESTATUS_ORDEN_SERVICIO = :estatusOrdenServicio";
	public static final String ID_USUARIO_ALTA = "ID_USUARIO_ALTA";
	public static final String TIPO_REPORTE = "tipoReporte";
	public static final String ID_ARTICULO = "ID_ARTICULO";
	public static final String CVE_FOLIO = "cveFolio";
	public static final String SEPARADOR = "separador";
	public static final String FEC_ALTA = "FEC_ALTA";
	public static final String ID_TABLA = "idTabla";
	public static final String REPLACE = "replace";
	
	private ConsultaConstantes() {
		super();
	}
	
	public static SelectQueryUtil detalleContratante () {
		SelectQueryUtil queryUtil = new SelectQueryUtil();
		queryUtil.select("P.ID_PERSONA AS idPersona","P.CVE_RFC AS rfc","P.CVE_CURP AS curp","P.CVE_NSS AS nss","P.NOM_PERSONA AS nomPersona","P.NOM_PRIMER_APELLIDO AS nomPersonaPaterno",
				"P.NOM_SEGUNDO_APELLIDO AS nomPersonaMaterno","P.NUM_SEXO AS numSexo","P.DES_OTRO_SEXO AS desOtroSexo","P.FEC_NAC as fechaNacimiento","P.ID_PAIS AS idPais","P.ID_ESTADO AS idEstado","P.DES_TELEFONO AS desTelefono",
				"P.DES_CORREO AS desCorreo","P.TIPO_PERSONA AS tipoPersona","C.ID_CONTRATANTE AS idContratante","C.CVE_MATRICULA AS claveMatricula","D.DES_CALLE AS desCalle","D.NUM_EXTERIOR AS numExterior",
				"D.NUM_INTERIOR AS numInterior","D.DES_CP AS DesCodigoPostal","D.DES_COLONIA AS desColonia","D.DES_MUNICIPIO AS desMunicipio","D.DES_ESTADO AS desEstado")
		.from("SVC_PERSONA P")
		.innerJoin("SVC_CONTRATANTE C", "P.ID_PERSONA = C.ID_PERSONA")
		.innerJoin("SVT_DOMICILIO D", "C.ID_DOMICILIO = D.ID_DOMICILIO");
		return queryUtil;
	}
	
	public static Integer getIdVelatorio(Integer idVelatorio) {
		if(idVelatorio == null){
			return 0;
		}
		return idVelatorio;
	}
}
