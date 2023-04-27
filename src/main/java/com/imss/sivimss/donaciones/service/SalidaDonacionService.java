package com.imss.sivimss.donaciones.service;

import java.io.IOException;

import org.springframework.security.core.Authentication;

import com.imss.sivimss.donaciones.util.DatosRequest;
import com.imss.sivimss.donaciones.util.Response;

public interface SalidaDonacionService {
	
	
	Response<Object> detalleContratanteRfc(DatosRequest request, Authentication authentication) throws IOException;
	
	Response<Object> detalleContratanteCurp(DatosRequest request, Authentication authentication) throws IOException;
	
	Response<?> detalleSalidaAtaudDonado(DatosRequest request, Authentication authentication) throws IOException;
	
	Response<?> countSalidaAtaudDonado(DatosRequest request, Authentication authentication) throws IOException;
	
	Response<?> insertSalidaAtaudDonado(DatosRequest request, Authentication authentication)throws IOException;
	
	Response<?> generarDocumentoControlSalidaDonacion(DatosRequest request, Authentication authentication) throws IOException;

}
