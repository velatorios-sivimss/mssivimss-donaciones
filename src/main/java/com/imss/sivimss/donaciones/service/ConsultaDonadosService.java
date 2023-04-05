package com.imss.sivimss.donaciones.service;

import java.io.IOException;

import org.springframework.security.core.Authentication;

import com.imss.sivimss.donaciones.util.DatosRequest;
import com.imss.sivimss.donaciones.util.Response;


public interface ConsultaDonadosService {

	Response<?> consultaFiltroDonado(DatosRequest request, Authentication authentication) throws IOException;
	Response<?> consultarDonados(DatosRequest request, Authentication authentication) throws IOException;
}
