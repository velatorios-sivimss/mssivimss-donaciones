package com.imss.sivimss.donaciones.beans;

import java.util.Date;

import javax.xml.bind.DatatypeConverter;

import com.imss.sivimss.donaciones.util.SelectQueryUtil;
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
	private String nomDonador;
	private Integer idVelatorio;
	private Integer idDelegacion;
	private String fechaInicio;
	private String fechaFin;

	private Integer desDelegacion;

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
		this.desDelegacion = consultaDonadoRequest.getDesDelegacion();
	}

	public DatosRequest consultaDonado(DatosRequest request, String formatoFecha) {

		SelectQueryUtil queryUtil = new SelectQueryUtil();
		queryUtil
				.select("si.ID_INVENTARIO AS idInventario", "sv.NOM_VELATORIO AS velatorio",
						"stm.DES_TIPO_MATERIAL AS tipo", "sa.DES_MODELO_ARTICULO AS modeloAtaud",
						"si.NUM_INVENTARIO AS numInventario",
						"date_format(ssd.FEC_SOLICITUD,'" + formatoFecha + "')  AS fecDonacion",
						"ssd.NOMBRE_INSTITUCION AS donadoPor", "sp.NOM_PERSONA AS nomDonador")
				.from("svc_inventario si").innerJoin("svt_articulo sa", "sa.ID_ARTICULO = si.ID_ARTICULO")
				.innerJoin("svc_velatorio sv", "sv.ID_VELATORIO = si.ID_VELATORIO")
				.innerJoin("svc_tipo_material stm", "stm.ID_TIPO_MATERIAL = sa.ID_TIPO_MATERIAL")
				.innerJoin("svc_salida_donacion_ataudes ssda", "ssda.ID_ARTICULO = sa.ID_ARTICULO")
				.innerJoin("svc_salida_donacion ssd", "ssd.ID_SALIDA_DONACION  = ssda.ID_SALIDA_DONACION")
				.innerJoin("svc_contratante sc", "sc.ID_CONTRATANTE = ssd.ID_CONTRATANTE")
				.innerJoin("svc_persona sp", "sp.ID_PERSONA = sc.ID_PERSONA ");
		final String query = queryUtil.build();
		String encoded = DatatypeConverter.printBase64Binary(query.getBytes());
		request.getDatos().put(AppConstantes.QUERY, encoded);

		return request;
	}

	public DatosRequest consultarFiltroDonadosSalida(DatosRequest request, String formatoFecha) {
		SelectQueryUtil queryUtil = new SelectQueryUtil();
		queryUtil
				.select("si.ID_INVENTARIO AS idInventario", " sv.NOM_VELATORIO AS velatorio",
						"stm.DES_TIPO_MATERIAL AS tipo", " sa.DES_MODELO_ARTICULO AS modeloAtaud ",
						" si.NUM_INVENTARIO AS numInventario ",
						" date_format(ssd.FEC_SOLICITUD,'" + formatoFecha + "')  AS fecDonacion",
						" ssd.NOMBRE_INSTITUCION AS donadoPor", " sp.NOM_PERSONA AS nomDonador")
				.from("svc_inventario si ").innerJoin("svt_articulo sa", "sa.ID_ARTICULO = si.ID_ARTICULO")
				.innerJoin("svc_velatorio sv", "sv.ID_VELATORIO = si.ID_VELATORIO")
				.innerJoin("svc_tipo_material stm", "stm.ID_TIPO_MATERIAL = sa.ID_TIPO_MATERIAL")
				.innerJoin("svc_salida_donacion_ataudes ssda", "ssda.ID_ARTICULO = sa.ID_ARTICULO")
				.innerJoin("svc_salida_donacion ssd", "ssd.ID_SALIDA_DONACION  = ssda.ID_SALIDA_DONACION")
				.innerJoin("svc_contratante sc", "sc.ID_CONTRATANTE = ssd.ID_CONTRATANTE")
				.innerJoin("svc_persona sp", "sp.ID_PERSONA = sc.ID_PERSONA")
				.innerJoin("svc_delegacion sd", "sd.ID_DELEGACION = sv.ID_DELEGACION").where("si.ID_VELATORIO = :idVel")
				.setParameter("idVel", this.idVelatorio).and("sv.ID_DELEGACION = :idDel")
				.setParameter("idDel", this.idDelegacion);
		if (this.fechaInicio != null && this.fechaFin != null) {
			queryUtil.and("ssd.FEC_SOLICITUD >= :fecIni").setParameter("fecIni", this.fechaInicio)
					.and("ssd.FEC_SOLICITUD <= :fecFin").setParameter("fecFin", this.fechaFin);
		}
		final String query = queryUtil.build();
		String encoded = DatatypeConverter.printBase64Binary(query.getBytes());
		request.getDatos().put(AppConstantes.QUERY, encoded);

		return request;
	}

	public DatosRequest obtenerDelegaciones(DatosRequest request) {

		SelectQueryUtil queryUtil = new SelectQueryUtil();
		queryUtil.select("sd.ID_DELEGACION, sd.DES_DELEGACION").from("svc_delegacion sd");
		final String query = queryUtil.build();
		String encoded = DatatypeConverter.printBase64Binary(query.getBytes());
		request.getDatos().put(AppConstantes.QUERY, encoded);
		return request;
	}

	public DatosRequest obtenerVelatorio(DatosRequest request) {

		SelectQueryUtil queryUtil = new SelectQueryUtil();
		queryUtil.select("sv.ID_VELATORIO", "sv.NOM_VELATORIO").from("svc_velatorio sv");
		final String query = queryUtil.build();
		String encoded = DatatypeConverter.printBase64Binary(query.getBytes());
		request.getDatos().put(AppConstantes.QUERY, encoded);
		return request;
	}

	public DatosRequest obtenerNivel(DatosRequest request) {

		SelectQueryUtil queryUtil = new SelectQueryUtil();
		queryUtil.select("sno.ID_OFICINA", "sno.DES_NIVELOFICINA").from("svc_nivel_oficina sno ");
		final String query = queryUtil.build();
		String encoded = DatatypeConverter.printBase64Binary(query.getBytes());
		request.getDatos().put(AppConstantes.QUERY, encoded);
		return request;
	}

	public DatosRequest consultarFiltroDonadosEntrada(DatosRequest request, String formatoFecha) {
		SelectQueryUtil queryUtil = new SelectQueryUtil();
		queryUtil
				.select("si.ID_INVENTARIO AS idInventario", "sv.NOM_VELATORIO AS velatorio",
						"stm.DES_TIPO_MATERIAL AS tipo", "sa.DES_MODELO_ARTICULO AS modeloAtaud",
						"si.NUM_INVENTARIO AS numInventario",
						"date_format(sd.FEC_ALTA ,'" + formatoFecha + "')  AS fecDonacion", "'Instituto' AS donadoPor",
						"sp.NOM_PERSONA AS nomDonador")
				.from("svc_donacion sd")
				.join("svc_donacion_orden_servicio sdos", "sdos.ID_ORDEN_SERVICIO = sd.ID_ORDEN_SERVICIO")
				.join(" svc_orden_servicio sos ", " sos.ID_ORDEN_SERVICIO = sdos.ID_ORDEN_SERVICIO")
				.join(" svc_contratante sc ", " sc.ID_CONTRATANTE = sos.ID_CONTRATANTE")
				.join(" svc_persona sp ", " sp.ID_PERSONA =sc.ID_PERSONA ")
				.join(" svt_articulo sa ", " sa.ID_ARTICULO = sdos.ID_ARTICULO")
				.join(" svc_tipo_articulo sta ", " sta.ID_TIPO_ARTICULO = sa.ID_TIPO_ARTICULO ")
				.join(" svc_tipo_material stm ", " stm.ID_TIPO_MATERIAL = sa.ID_TIPO_MATERIAL")
				.join(" svc_inventario si ", " si.ID_ARTICULO = sa.ID_ARTICULO ")
				.join(" svc_velatorio sv ", " sv.ID_VELATORIO = si.ID_VELATORIO").where("sv.ID_VELATORIO = :idVel")
				.setParameter("idVel", this.idVelatorio).and("sv.ID_DELEGACION = :idDel")
				.setParameter("idDel", this.idDelegacion);
		if (this.fechaInicio != null && this.fechaFin != null) {
			queryUtil.and("date_format(sd.FEC_ALTA,'" + formatoFecha + "') >= :fecIni")
					.setParameter("fecIni", this.fechaInicio)
					.and("date_format(sd.FEC_ALTA ,'" + formatoFecha + "') <= :fecFin")
					.setParameter("fecFin", this.fechaFin);
		}
		final String query = queryUtil.build();
		String encoded = DatatypeConverter.printBase64Binary(query.getBytes());
		request.getDatos().put(AppConstantes.QUERY, encoded);

		return request;
	}

	public DatosRequest consultarDonados(DatosRequest request, String formatoFecha) {
		String query = "SELECT si.ID_INVENTARIO AS idInventario,  sv.NOM_VELATORIO AS velatorio, stm.DES_TIPO_MATERIAL AS tipo "
				+ ",  sa.DES_MODELO_ARTICULO AS modeloAtaud ,  si.NUM_INVENTARIO AS numInventario "
				+ ",  date_format(ssd.FEC_SOLICITUD,'" + formatoFecha + "')  AS fecDonacion,  'ODS' AS donadoPor "
				+ ",  sp.NOM_PERSONA AS nomDonador " + "FROM svc_inventario si  "
				+ "JOIN svt_articulo sa ON sa.ID_ARTICULO = si.ID_ARTICULO  "
				+ "JOIN svc_velatorio sv ON sv.ID_VELATORIO = si.ID_VELATORIO  "
				+ "JOIN svc_tipo_material stm ON stm.ID_TIPO_MATERIAL = sa.ID_TIPO_MATERIAL  "
				+ "JOIN svc_salida_donacion_ataudes ssda ON ssda.ID_ARTICULO = sa.ID_ARTICULO  "
				+ "JOIN svc_salida_donacion ssd ON ssd.ID_SALIDA_DONACION  = ssda.ID_SALIDA_DONACION  "
				+ "JOIN svc_contratante sc ON sc.ID_CONTRATANTE = ssd.ID_CONTRATANTE  "
				+ "JOIN svc_persona sp ON sp.ID_PERSONA = sc.ID_PERSONA "
				+ "JOIN svc_delegacion sd ON sd.ID_DELEGACION = sv.ID_DELEGACION  ";
		if(this.idVelatorio != null && this.idDelegacion != null) 
			query = query + "WHERE si.ID_VELATORIO = " + this.idVelatorio + "  AND sv.ID_DELEGACION = " + this.idDelegacion;
		if (this.fechaInicio != null && this.fechaFin != null) {
			query = query + " AND date_format(ssd.FEC_SOLICITUD,'%Y-%m-%d') >= '" + this.fechaInicio + "'"
					+ " AND date_format(ssd.FEC_SOLICITUD,'%Y-%m-%d') <= '" + this.fechaFin + "'";
		}
		query = query + " UNION "
				+ "SELECT si.ID_INVENTARIO AS idInventario,  sv.NOM_VELATORIO AS velatorio, stm.DES_TIPO_MATERIAL AS tipo "
				+ ",  sa.DES_MODELO_ARTICULO AS modeloAtaud ,  si.NUM_INVENTARIO AS numInventario "
				+ ",  date_format(sd.FEC_ALTA ,'%d/%m/%Y')  AS fecDonacion " + ",  'Instituto' AS donadoPor "
				+ ",  sp.NOM_PERSONA AS nomDonador " + " FROM svc_donacion sd "
				+ " JOIN svc_donacion_orden_servicio sdos ON sdos.ID_ORDEN_SERVICIO = sd.ID_ORDEN_SERVICIO "
				+ " JOIN svc_orden_servicio sos ON sos.ID_ORDEN_SERVICIO = sdos.ID_ORDEN_SERVICIO "
				+ " JOIN svc_contratante sc ON sc.ID_CONTRATANTE = sos.ID_CONTRATANTE "
				+ " JOIN svc_persona sp ON sp.ID_PERSONA =sc.ID_PERSONA "
				+ " JOIN svt_articulo sa ON sa.ID_ARTICULO = sdos.ID_ARTICULO "
				+ " JOIN svc_tipo_articulo sta ON sta.ID_TIPO_ARTICULO = sa.ID_TIPO_ARTICULO "
				+ " JOIN svc_tipo_material stm ON stm.ID_TIPO_MATERIAL = sa.ID_TIPO_MATERIAL "
				+ " JOIN svc_inventario si ON si.ID_ARTICULO = sa.ID_ARTICULO "
				+ " JOIN svc_velatorio sv ON sv.ID_VELATORIO = si.ID_VELATORIO ";
		if(this.idVelatorio != null && this.idDelegacion != null) 
				query = query +  " WHERE sv.ID_VELATORIO = " + this.idVelatorio + "  AND sv.ID_DELEGACION = " + this.idDelegacion;
		if (this.fechaInicio != null && this.fechaFin != null) {
			query = query + " AND date_format(sd.FEC_ALTA ,'%Y-%m-%d') >= '" + this.fechaInicio + "'"
					+ " AND date_format(sd.FEC_ALTA ,'%Y-%m-%d') <= '" + this.fechaFin + "'";
		}
		String encoded = DatatypeConverter.printBase64Binary(query.getBytes());
		request.getDatos().put(AppConstantes.QUERY, encoded);

		return request;
	}
}
