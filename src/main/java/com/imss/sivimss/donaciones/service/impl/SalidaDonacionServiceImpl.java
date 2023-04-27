package com.imss.sivimss.donaciones.service.impl;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.imss.sivimss.donaciones.beans.SalidaDonacion;
import com.imss.sivimss.donaciones.exception.BadRequestException;
import com.imss.sivimss.donaciones.model.request.AgregarArticuloRequest;
import com.imss.sivimss.donaciones.model.request.DonacionRequest;
import com.imss.sivimss.donaciones.model.request.PlantillaControlSalidaDonacionRequest;
import com.imss.sivimss.donaciones.model.request.UsuarioDto;
import com.imss.sivimss.donaciones.service.SalidaDonacionService;
import com.imss.sivimss.donaciones.util.AppConstantes;
import com.imss.sivimss.donaciones.util.DatosRequest;
import com.imss.sivimss.donaciones.util.MensajeResponseUtil;
import com.imss.sivimss.donaciones.util.ProviderServiceRestTemplate;
import com.imss.sivimss.donaciones.util.Response;

@Service
public class SalidaDonacionServiceImpl implements SalidaDonacionService {

	
	private static final String R_F_C_NO_VALIDO = "33"; // R.F.C. no valido.
	private static final String CURP_NO_VALIDO = "34"; // CURP no valido.
	private static final String REGISTRADO_CORRECTAMENTE = "118"; // Se ha registrado correctamente el registro de salida de ataúdes de donación.
	private static final String INFORMACION_INCOMPLETA = "Informacion incompleta";
	private static final String ERROR_AL_DESCARGAR_DOCUMENTO= "64"; // Error en la descarga del documento.Intenta nuevamente.
	private static final String YA_NO_HAY_STOCK = "15";  // Ya no hay stock de este artículo.
	private static final String SIN_INFORMACION = "45";  // No se encontró información relacionada a tu búsqueda.
	

	@Value("${endpoints.dominio-consulta}")
	private String urlConsulta;
	
	@Value("${endpoints.dominio-insercion-salida-donacion}")
	private String urlInsercionSalidaDonacion;
	
	@Value("${endpoints.dominio-actualizar-multiples}")
	private String urlActualizarMultiple;

	@Value("${formato_fecha}")
	private String formatoFecha;
	
	@Value("${endpoints.ms-reportes}")
	private String urlReportes;
	
	@Value("${endpoints.ms-rfc}")
	private String urlRfc;
	
	@Value("${endpoints.ms-curp}")
	private String urlCurp;
	
	@Value("${plantilla.control-salida-ataudes-donacion}")
	private String nombrePdfControlSalida;

	@Autowired
	private ProviderServiceRestTemplate providerRestTemplate;
	

	@Override
	public Response<Object> detalleContratanteRfc(DatosRequest request, Authentication authentication) throws IOException {
		DonacionRequest donacionRequest = mappeoObject(request);
		Response<Object> response = new Response<>();

		if (donacionRequest.getRfc() == null) {
			throw new BadRequestException(HttpStatus.BAD_REQUEST, INFORMACION_INCOMPLETA);
		}
		
		response = providerRestTemplate.consumirServicioObject(new SalidaDonacion().detalleContratanteRfc(request,donacionRequest).getDatos(), urlConsulta, authentication);
		
		if (!response.getDatos().toString().contains("nomPersona")) {
			response = providerRestTemplate.consumirServicioObject(urlRfc.concat(donacionRequest.getRfc()), 0);
		}
		return MensajeResponseUtil.mensajeConsultaResponseObject(response,R_F_C_NO_VALIDO);
	}
	
	@Override
	public Response<Object> detalleContratanteCurp(DatosRequest request, Authentication authentication) throws IOException {
		DonacionRequest donacionRequest = mappeoObject(request);
		Response<Object> response = new Response<>();

		if (donacionRequest.getCurp() == null) {
			throw new BadRequestException(HttpStatus.BAD_REQUEST, INFORMACION_INCOMPLETA);
		}
		
		response = providerRestTemplate.consumirServicioObject(new SalidaDonacion().detalleContratanteCurp(request,donacionRequest).getDatos(),urlConsulta, authentication);
		
		if (!response.getDatos().toString().contains("nomPersona")) {
			response = providerRestTemplate.consumirServicioObject(urlCurp.concat(donacionRequest.getCurp()), 1);
		}

		return MensajeResponseUtil.mensajeConsultaResponseObject(response,CURP_NO_VALIDO);
	}
	
	@Override
	public Response<?> detalleSalidaAtaudDonado(DatosRequest request, Authentication authentication) throws IOException  {

	return MensajeResponseUtil.mensajeConsultaResponse(providerRestTemplate.consumirServicio(new SalidaDonacion().detalleSalidaAtaudDonado(request).getDatos(),
					urlConsulta, authentication),
			SIN_INFORMACION);
	}
	
	@Override
	public Response<?> countSalidaAtaudDonado(DatosRequest request, Authentication authentication) throws IOException  {
		AgregarArticuloRequest agregarArticuloRequest = new Gson().fromJson(String.valueOf(request.getDatos().get(AppConstantes.DATOS)), AgregarArticuloRequest.class);

	if (agregarArticuloRequest.getModeloArticulo() == null) {
		throw new BadRequestException(HttpStatus.BAD_REQUEST, INFORMACION_INCOMPLETA);
	}
	
	Response<?> respuestaGenerado = providerRestTemplate.consumirServicio(new SalidaDonacion().countSalidaAtaudDonado(request, agregarArticuloRequest).getDatos(),
			urlConsulta, authentication);
	if(respuestaGenerado.getCodigo() == 200 &&  (respuestaGenerado.getDatos().toString().contains("0"))) {
		return MensajeResponseUtil.mensajeConsultaResponse(respuestaGenerado, YA_NO_HAY_STOCK);
	}
	return respuestaGenerado;
	}
	
	@Override
	public Response<?> insertSalidaAtaudDonado(DatosRequest request, Authentication authentication) throws IOException {
		Gson gson = new Gson();
		DonacionRequest donacionRequest = mappeoObject(request);
		UsuarioDto usuarioDto = gson.fromJson((String) authentication.getPrincipal(), UsuarioDto.class);
		
		Response<?> response = providerRestTemplate.consumirServicio(new SalidaDonacion().insertPersona(donacionRequest, usuarioDto),urlInsercionSalidaDonacion,authentication);
		if(200 == response.getCodigo()) {
			response = providerRestTemplate.consumirServicio(new SalidaDonacion().actualizarStockArticulo(donacionRequest, usuarioDto),urlActualizarMultiple,authentication);
		}
		
		return MensajeResponseUtil.mensajeResponse(response, REGISTRADO_CORRECTAMENTE);
	}
	
	private DonacionRequest mappeoObject(DatosRequest request) {
		return new Gson().fromJson(String.valueOf(request.getDatos().get(AppConstantes.DATOS)), DonacionRequest.class);
	}


	@Override
	public Response<?> generarDocumentoControlSalidaDonacion(DatosRequest request, Authentication authentication)
			throws IOException {
		Map<String, Object> envioDatos = new SalidaDonacion().generarPlantillaControlSalidaDonacionPDF(new Gson().fromJson(String.valueOf(request.getDatos().get(AppConstantes.DATOS)), PlantillaControlSalidaDonacionRequest.class),nombrePdfControlSalida);
		return MensajeResponseUtil.mensajeConsultaResponse(providerRestTemplate.consumirServicioReportes(envioDatos, urlReportes, authentication)
				, ERROR_AL_DESCARGAR_DOCUMENTO);
	}

}