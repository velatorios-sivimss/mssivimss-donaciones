package com.imss.sivimss.donaciones.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class AgregarFinadoRequest {
    @JsonProperty
	private String nomFinado;
    @JsonProperty
	private String nomFinadoPaterno;
    @JsonProperty
	private String nomFinadoMaterno;

}
