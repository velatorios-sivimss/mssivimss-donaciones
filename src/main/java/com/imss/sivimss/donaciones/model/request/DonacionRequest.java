package com.imss.sivimss.donaciones.model.request;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;


@Data
public class DonacionRequest {
	
	@JsonProperty
	private Integer idSalidaDonacion;
	@JsonProperty
	private Integer idPais;
	@JsonProperty
	private Integer idEstado;
	@JsonProperty
	private String nomPersona;
	@JsonProperty
	private String nomPersonaPaterno;
	@JsonProperty
	private String nomPersonaMaterno;
	@JsonProperty
	private String rfc;
	@JsonProperty
	private String curp;
	@JsonProperty
	private String nss;
	@JsonProperty
	private Integer numTotalAtaudes;
	@JsonProperty
	private String responsableAlmacen;
	@JsonProperty
	private String matricularesponsable;
	@JsonProperty
	private String nomInstitucion;
	@JsonProperty
	private Integer estudioSocieconomico;
	@JsonProperty
	private Integer estudioLibre;
	@JsonFormat(pattern="yyyy-MM-dd")
	private String fecSolicitad;
	@JsonProperty
	private Integer idOrdenServicio;
	@JsonProperty
	private String claveFolio;
	@JsonProperty
	private Integer estatusOrdenServicio;
	@JsonProperty
	private String claveMatricula;
	@JsonProperty
	private Integer idCodigoPostal;
	@JsonProperty
	private String desCalle;
	@JsonProperty
	private String numExterior;
	@JsonProperty
	private String numInterior;
	@JsonProperty
	private String desColonia;
	@JsonProperty
	private String desTelefono;
	@JsonProperty
	private String desCorreo;
	@JsonProperty
	private String tipoPersona;
	@JsonProperty
	private Integer numSexo;
	@JsonProperty
	private String desOtroSexo;
	@JsonFormat(pattern="yyyy-MM-dd")
	private String  fecNacimiento;
	@JsonProperty
	Set<AgregarArticuloRequest> ataudesDonados = new HashSet<>();
	@JsonProperty
	Set<AgregarFinadoRequest> agregarFinados = new HashSet<>();

}
