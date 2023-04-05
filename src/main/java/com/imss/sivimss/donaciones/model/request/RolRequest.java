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
public class RolRequest {
	private Integer idRol;
	private String desRol;
	private Integer estatusRol;
	private Integer nivel;
	private String claveAlta;
	
}