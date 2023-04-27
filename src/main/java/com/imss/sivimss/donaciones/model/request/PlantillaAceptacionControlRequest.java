package com.imss.sivimss.donaciones.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class PlantillaAceptacionControlRequest {
	
	@JsonProperty
	private Double version;
	@JsonProperty
	private Integer ooadId;
	@JsonProperty
	private Integer velatorioId;
	@JsonProperty
	private String numContrato;
	@JsonProperty
	private String nomFinado;
	@JsonProperty
	private String nomResponsableAlmacen;
	@JsonProperty
	private String nomContratante;
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
