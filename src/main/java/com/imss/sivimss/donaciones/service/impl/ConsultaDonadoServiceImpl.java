package com.imss.sivimss.donaciones.service.impl;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.imss.sivimss.donaciones.beans.ConsultaDonado;
import com.imss.sivimss.donaciones.model.request.ConsultaDonadoRequest;
import com.imss.sivimss.donaciones.service.ConsultaDonadosService;
import com.imss.sivimss.donaciones.util.AppConstantes;
import com.imss.sivimss.donaciones.util.DatosRequest;
import com.imss.sivimss.donaciones.util.MensajeResponseUtil;
import com.imss.sivimss.donaciones.util.ProviderServiceRestTemplate;
import com.imss.sivimss.donaciones.util.Response;

@Service
public class ConsultaDonadoServiceImpl implements ConsultaDonadosService {

	@Value("${endpoints.dominio-consulta-paginado}")
	private String urlConsultaGenericoPaginado;

	@Value("${endpoints.dominio-consulta}")
	private String urlConsultaGenerica;


	@Value("${formato_fecha}")
	private String formatoFecha;

	@Autowired
	private ProviderServiceRestTemplate providerRestTemplate;

	ConsultaDonado consultarDonado = new ConsultaDonado();


	private static final String NO_SE_ENCONTRO_INFORMACION = "45"; // No se encontró información relacionada a tu
																	// búsqueda.

	@Override
	public Response<?> consultarDonados(DatosRequest request, Authentication authentication)
			throws IOException {

		return MensajeResponseUtil.mensajeConsultaResponse(
				providerRestTemplate.consumirServicio(consultarDonado.consultarDonados(request, formatoFecha).getDatos(),
						urlConsultaGenericoPaginado, authentication),
				NO_SE_ENCONTRO_INFORMACION);
	}

	
	@Override
	public Response<?> consultaFiltroDonado(DatosRequest request, Authentication authentication)
			throws IOException {

		Gson gson = new Gson();
		String datosJson = String.valueOf(request.getDatos().get(AppConstantes.DATOS));
		ConsultaDonadoRequest consultaDonadoRequest = gson.fromJson(datosJson, ConsultaDonadoRequest.class);
		consultarDonado = new ConsultaDonado(consultaDonadoRequest);
		Response<?> response = null ;
		if(consultaDonadoRequest.getDonadoPor() == null) {
			response = providerRestTemplate.consumirServicio(consultarDonado.consultarDonados(request, formatoFecha).getDatos(),
					urlConsultaGenericoPaginado, authentication);
		}else if(consultaDonadoRequest.getDonadoPor().equals("1") ){
			response = providerRestTemplate.consumirServicio(consultarDonado.consultarFiltroDonadosSalida(request, formatoFecha).getDatos(),
					urlConsultaGenericoPaginado, authentication);
		}else if(consultaDonadoRequest.getDonadoPor().equals("2") ){
				response = providerRestTemplate.consumirServicio(consultarDonado.consultarFiltroDonadosEntrada(request, formatoFecha).getDatos(),
						urlConsultaGenericoPaginado, authentication);
		}

		return MensajeResponseUtil.mensajeConsultaResponse( response, NO_SE_ENCONTRO_INFORMACION);
	}
	

	@Override
	public Response<?> consultarDelegacion(DatosRequest request, Authentication authentication)
			throws IOException {
	return MensajeResponseUtil.mensajeConsultaResponse(
			providerRestTemplate.consumirServicio(consultarDonado.obtenerDelegaciones(request).getDatos(),
					urlConsultaGenerica, authentication),
			NO_SE_ENCONTRO_INFORMACION);
	}
	@Override
	public Response<?> consultarVelatorio(DatosRequest request, Authentication authentication)
			throws IOException {

		return MensajeResponseUtil.mensajeConsultaResponse(
		providerRestTemplate.consumirServicio(consultarDonado.obtenerVelatorio(request).getDatos(),
				urlConsultaGenerica, authentication),
		NO_SE_ENCONTRO_INFORMACION);
	}

	@Override
	public Response<?> consultarNivel(DatosRequest request, Authentication authentication)
			throws IOException {

		return MensajeResponseUtil.mensajeConsultaResponse(
		providerRestTemplate.consumirServicio(consultarDonado.obtenerNivel(request).getDatos(),
				urlConsultaGenerica, authentication),
		NO_SE_ENCONTRO_INFORMACION);
	}
}
