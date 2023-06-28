package com.imss.sivimss.donaciones.service;

import java.io.IOException;

import org.springframework.security.core.Authentication;

import com.imss.sivimss.donaciones.util.DatosRequest;
import com.imss.sivimss.donaciones.util.Response;


public interface ConsultaDonadosService {

	Response<Object> consultaFiltroDonado(DatosRequest request, Authentication authentication) throws IOException;
	Response<Object> consultarDonados(DatosRequest request, Authentication authentication) throws IOException;
	Response<Object> generarDocumento(DatosRequest request, Authentication authentication) throws IOException;
	Response<Object> generarDocumentoEntrada(DatosRequest request, Authentication authentication) throws IOException;
	Response<Object> generarDocumentoSalida(DatosRequest request, Authentication authentication) throws IOException;
}
