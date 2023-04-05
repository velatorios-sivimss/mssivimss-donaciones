package com.imss.sivimss.donaciones.service;

import java.io.IOException;

import org.springframework.security.core.Authentication;

import com.imss.sivimss.donaciones.util.DatosRequest;
import com.imss.sivimss.donaciones.util.Response;

public interface DonacionService {

	Response<?> consultarRoles(DatosRequest request, Authentication authentication) throws IOException;

	Response<?> buscarFiltrosRol(DatosRequest request, Authentication authentication) throws IOException;

	Response<?> detalleNombreContratante(DatosRequest request, Authentication authentication) throws IOException;
	
	Response<?> detalleNombreFinado(DatosRequest request, Authentication authentication) throws IOException;
	
	Response<?> detalleAtaudDonado(DatosRequest request, Authentication authentication) throws IOException;

	Response<?> agregarRol(DatosRequest request, Authentication authentication)throws IOException;

	Response<?> actualizarRol(DatosRequest request, Authentication authentication)throws IOException;
	
	Response<?> cambiarEstatusRol(DatosRequest request, Authentication authentication)throws IOException;
	
}