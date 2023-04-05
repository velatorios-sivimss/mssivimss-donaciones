package com.imss.sivimss.donaciones.model.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class RolDto {

	private Integer id;
	private String desRol;
	private String estatus;
	private String cveMatriculaAlta;
	private Integer nivel;
}