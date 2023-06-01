package com.imss.sivimss.donaciones.service;

import java.io.IOException;

import org.springframework.security.core.Authentication;

import com.imss.sivimss.donaciones.util.DatosRequest;
import com.imss.sivimss.donaciones.util.Response;

public interface SalidaDonacionService {
	
	Response<Object> detalleContratanteRfc(DatosRequest request, Authentication authentication) throws IOException;
	
	Response<Object> detalleContratanteCurp(DatosRequest request, Authentication authentication) throws IOException;
	
	Response<Object> detalleSalidaAtaudDonado(DatosRequest request, Authentication authentication) throws IOException;
	
	Response<Object> cantidadSalidaAtaudDonado(DatosRequest request, Authentication authentication) throws IOException;
	
	Response<Object> insertSalidaAtaudDonado(DatosRequest request, Authentication authentication)throws IOException;
	
	Response<Object> generarDocumentoControlSalidaDonacion(DatosRequest request, Authentication authentication) throws IOException;

}
