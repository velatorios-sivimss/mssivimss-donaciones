package com.imss.sivimss.donaciones.service;

import java.io.IOException;

import org.springframework.security.core.Authentication;

import com.imss.sivimss.donaciones.util.DatosRequest;
import com.imss.sivimss.donaciones.util.Response;


public interface ConsultaDonadosService {

	Response<?> consultaFiltroDonado(DatosRequest request, Authentication authentication) throws IOException;
	Response<?> consultarDonados(DatosRequest request, Authentication authentication) throws IOException;
	Response<?> consultarDelegacion(DatosRequest request, Authentication authentication) throws IOException;
	Response<?> consultarVelatorio(DatosRequest request, Authentication authentication) throws IOException;
	Response<?> consultarNivel(DatosRequest request, Authentication authentication) throws IOException;
	Response<?> generarDocumento(DatosRequest request, Authentication authentication) throws IOException;
}
