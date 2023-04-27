package com.imss.sivimss.donaciones.model.request;

import com.fasterxml.jackson.annotation.JsonIgnoreType;

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
@JsonIgnoreType(value = true)
public class FinadoRequest {
	
	private Integer idSalidaDonacion;
	private String nomFinado;
	private String nomFinadoPaterno;
	private String nomFinadoMaterno;
	private String claveAlta;
	private String claveModifica;
	private String claveBaja;

}
