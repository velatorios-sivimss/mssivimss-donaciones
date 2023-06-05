package com.imss.sivimss.donaciones.service;

import java.io.IOException;

import org.springframework.security.core.Authentication;

import com.imss.sivimss.donaciones.util.DatosRequest;
import com.imss.sivimss.donaciones.util.Response;

public interface AceptacionDonacionService {
	
	Response<Object> detalleNombreContratante(DatosRequest request, Authentication authentication) throws IOException;
	
	Response<Object> detalleNombreFinado(DatosRequest request, Authentication authentication) throws IOException;
	
	Response<Object> detalleAceptacionDonacion(DatosRequest request, Authentication authentication) throws IOException;
	
	Response<Object> detalleAtaudDonado(DatosRequest request, Authentication authentication) throws IOException;
	
	Response<Object> insertAtaudDonado(DatosRequest request, Authentication authentication)throws IOException;
	
	Response<Object> generarDocumentoAceptacionControl(DatosRequest request, Authentication authentication) throws IOException;

}
