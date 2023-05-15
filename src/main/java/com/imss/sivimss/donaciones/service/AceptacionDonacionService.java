package com.imss.sivimss.donaciones.service;

import java.io.IOException;

import org.springframework.security.core.Authentication;

import com.imss.sivimss.donaciones.util.DatosRequest;
import com.imss.sivimss.donaciones.util.Response;

public interface AceptacionDonacionService {
	
	Response<?> detalleNombreContratante(DatosRequest request, Authentication authentication) throws IOException;
	
	Response<?> detalleNombreFinado(DatosRequest request, Authentication authentication) throws IOException;
	
	Response<?> detalleAtaudDonado(DatosRequest request, Authentication authentication) throws IOException;
	
	Response<?> insertAtaudDonado(DatosRequest request, Authentication authentication)throws IOException;
	
	Response<?> generarDocumentoAceptacionControl(DatosRequest request, Authentication authentication) throws IOException;

}
