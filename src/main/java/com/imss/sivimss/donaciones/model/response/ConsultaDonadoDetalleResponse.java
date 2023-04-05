package com.imss.sivimss.donaciones.model.response;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreType;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
@ToString
@JsonIgnoreType(value = true)
public class ConsultaDonadoDetalleResponse {

	@JsonProperty(value = "idInventario")
	private Integer idInventario;
	@JsonProperty(value = "velatorio")
	private String velatorio;
	@JsonProperty(value = "tipo")
	private String tipo;
	@JsonProperty(value = "modeloAtaud")
	private String modeloAtaud;
	@JsonProperty(value = "numInventario")
	private String numInventario;
	@JsonProperty(value = "fecDonacion")
	private Date fecDonacion;
	@JsonProperty(value = "donadoPor")
	private String donadoPor;
	@JsonProperty(value = "nomDonador")
	private String nomDonador ;
}
