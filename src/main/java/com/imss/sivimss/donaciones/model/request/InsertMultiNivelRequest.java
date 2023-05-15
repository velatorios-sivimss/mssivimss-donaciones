package com.imss.sivimss.donaciones.model.request;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class InsertMultiNivelRequest {

	 @JsonProperty
	 private List<String> unoAuno = new ArrayList<>();
	 
	 @JsonProperty
	 private List<String> unoAn = new ArrayList<>();
	 
	 @JsonProperty
	 private String id;
	
}
