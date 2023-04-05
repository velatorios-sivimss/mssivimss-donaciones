package com.imss.sivimss.donaciones.model.response;

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
public class RolResponse {

	@JsonProperty(value = "id")
	private Integer ID_ROL;
	
	@JsonProperty(value = "des_rol")
	private String DES_ROL;
	
	@JsonProperty(value = "estatus")
	private String CVE_ESTATUS;
	
	@JsonProperty(value = "nivel")
	private Integer ID_OFICINA;
	
	@JsonProperty(value = "fCreacion")
	private String FEC_ALTA;
	
	@JsonProperty(value = "usuarioAlta")
	private String ID_USUARIO_ALTA;
	
	@JsonProperty(value = "fActualizacion")
	private String FEC_ACTUALIZACION;
	
	@JsonProperty(value = "usuarioModifica")
	private String ID_USUARIO_MODIFICA;
	
	@JsonProperty(value = "fBaja")
	private String FEC_BAJA;
	
	@JsonProperty(value = "usuarioBaja")
	private String ID_USUARIO_BAJA;
	
	
}