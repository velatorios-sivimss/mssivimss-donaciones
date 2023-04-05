package com.imss.sivimss.donaciones.beans;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.DatatypeConverter;

import com.imss.sivimss.donaciones.model.request.ConsultaDonadoRequest;
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

	private Integer idInventario;
	private String velatorio;
	private String tipo;
	private String modeloAtaud;
	private String numInventario;
	private Date fecDonacion;
	private String donadoPor;
	private String nomDonador ;
	private Integer idVelatorio; 
	private Integer idDelegacion; 
	private String fechaInicio;
	private String fechaFin;
	

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
	}

	public DatosRequest consultaDonado(DatosRequest request) {

		String query = "SELECT si.ID_INVENTARIO AS idInventario, sv.NOM_VELATORIO AS velatorio,stm.DES_TIPO_MATERIAL AS tipo, sa.DES_MODELO_ARTICULO AS modeloAtaud"
				+ ", si.NUM_INVENTARIO AS numInventario "
				+ ", ssd.FEC_SOLICITUD AS fecDonacion , ssd.NOMBRE_INSTITUCION AS donadoPor, sp.NOM_PERSONA AS nomDonador "
				+ " FROM svc_inventario si "
				+ " INNER JOIN svt_articulo sa ON sa.ID_ARTICULO = si.ID_ARTICULO "
				+ " INNER JOIN svc_velatorio sv ON sv.ID_VELATORIO = si.ID_VELATORIO "
				+ " INNER JOIN svc_tipo_material stm ON stm.ID_TIPO_MATERIAL = sa.ID_TIPO_MATERIAL "
				+ " INNER JOIN svc_salida_donacion_ataudes ssda ON ssda.ID_ARTICULO = sa.ID_ARTICULO "
				+ " INNER JOIN svc_salida_donacion ssd ON ssd.ID_SALIDA_DONACION  = ssda.ID_SALIDA_DONACION "
				+ " INNER JOIN svc_contratante sc ON sc.ID_CONTRATANTE = ssd.ID_CONTRATANTE "
				+ " INNER JOIN svc_persona sp ON sp.ID_PERSONA = sc.ID_PERSONA ";
		String encoded = DatatypeConverter.printBase64Binary(query.getBytes());
		request.getDatos().put(AppConstantes.QUERY, encoded);

		return request;
	}

	public DatosRequest consultarFiltroDonados()  {
		DatosRequest request = new DatosRequest();
		Map<String, Object> parametro = new HashMap<>();
		String query = "SELECT si.ID_INVENTARIO AS idInventario, sv.NOM_VELATORIO AS velatorio,stm.DES_TIPO_MATERIAL AS tipo, sa.DES_MODELO_ARTICULO AS modeloAtaud "
				+ " , si.NUM_INVENTARIO AS numInventario , ssd.FEC_SOLICITUD fecDonacion , ssd.NOMBRE_INSTITUCION AS donadoPor, sp.NOM_PERSONA AS nomDonador  "
				+ " FROM svc_inventario si "
				+ " INNER JOIN svt_articulo sa ON sa.ID_ARTICULO = si.ID_ARTICULO  "
				+ " INNER JOIN svc_velatorio sv ON sv.ID_VELATORIO = si.ID_VELATORIO  "
				+ " INNER JOIN svc_tipo_material stm ON stm.ID_TIPO_MATERIAL = sa.ID_TIPO_MATERIAL "
				+ " INNER JOIN svc_salida_donacion_ataudes ssda ON ssda.ID_ARTICULO = sa.ID_ARTICULO  "
				+ " INNER JOIN svc_salida_donacion ssd ON ssd.ID_SALIDA_DONACION  = ssda.ID_SALIDA_DONACION  "
				+ " INNER JOIN svc_contratante sc ON sc.ID_CONTRATANTE = ssd.ID_CONTRATANTE  "
				+ " INNER JOIN svc_persona sp ON sp.ID_PERSONA = sc.ID_PERSONA "
				+ " INNER JOIN svc_delegacion sd ON sd.ID_DELEGACION = sv.ID_DELEGACION  "
				+ " WHERE si.ID_VELATORIO = " + this.idVelatorio + "  AND sv.ID_DELEGACION = " + this.idDelegacion 
				+ " AND ssd.FEC_SOLICITUD >= '" + this.fechaInicio + "' AND ssd.FEC_SOLICITUD <= '" + this.fechaFin + "' ";

		String encoded = DatatypeConverter.printBase64Binary(query.getBytes());
		parametro.put(AppConstantes.QUERY, encoded);
		request.setDatos(parametro);

		return request;
	}
	
}
