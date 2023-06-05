package com.imss.sivimss.donaciones.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class PlantillaControlSalidaDonacionRequest {

	@JsonProperty
	private Double version;
	@JsonProperty
	private String ooadNom;
	@JsonProperty
	private Integer velatorioId;
	@JsonProperty
	private String velatorioNom;
	@JsonProperty
	private Integer numAtaudes;
	@JsonProperty
	private String modeloAtaud;
	@JsonProperty
	private String tipoAtaud;
	@JsonProperty
	private String numInventarios;
	@JsonProperty
	private String nomSolicitantes;
	@JsonProperty
	private String nomFinados;
	@JsonProperty
	private String fecSolicitud;
	@JsonProperty
	private String nomResponsableAlmacen;
	@JsonProperty
	private String nomSolicitante;
	@JsonProperty
	private String nomAdministrador;
	@JsonProperty
	private String lugar;
	@JsonProperty
	private Integer dia;
	@JsonProperty
	private String mes;
	@JsonProperty
	private Integer anio;
	@JsonProperty
	private String tipoReporte;

}
