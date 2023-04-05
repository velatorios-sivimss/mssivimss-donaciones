package com.imss.sivimss.donaciones.model.request;



import java.util.Date;

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
public class ConsultaDonadoRequest {
	
	private Integer idInventario;
	private String velatorio;
	private String tipo;
	private String modeloAtaud;
	private String numInventario;
	private Date fecDonacion;
	private String donadoPor;
	private String nomDonador ;
	private Integer idVelatorio; 
	private Integer idDelegacion; 
	private String fechaInicio;
	private String fechaFin;
}
