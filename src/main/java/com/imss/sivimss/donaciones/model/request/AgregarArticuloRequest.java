package com.imss.sivimss.donaciones.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class AgregarArticuloRequest {
	@JsonProperty
	private Integer idArticulo;
	@JsonProperty
	private Integer idCategoria;
	@JsonProperty
	private Integer idTipoArticulo;
	@JsonProperty
	private Integer idTipoMaterial;
	@JsonProperty
	private Integer idTamanio;
	@JsonProperty
	private Integer idClasificacionProducto;
	@JsonProperty
	private String modeloArticulo;
	@JsonProperty
	private String descripcionArticulo;
	@JsonProperty
	private Integer idPartidaPresupuestal;
	@JsonProperty
	private Integer idCuentaContable;
	@JsonProperty
	private Integer idClaveSAT;
	@JsonProperty
	private Integer idTipoAsignacionArt;
	@JsonProperty
	private Integer idInventarioArticulo;
	@JsonProperty
	private String folioArticulo;

}
