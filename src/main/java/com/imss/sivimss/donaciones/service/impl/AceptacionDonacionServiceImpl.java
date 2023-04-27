package com.imss.sivimss.donaciones.service.impl;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.imss.sivimss.donaciones.beans.Donacion;
import com.imss.sivimss.donaciones.exception.BadRequestException;
import com.imss.sivimss.donaciones.model.request.DonacionRequest;
import com.imss.sivimss.donaciones.model.request.PlantillaAceptacionControlRequest;
import com.imss.sivimss.donaciones.model.request.UsuarioDto;
import com.imss.sivimss.donaciones.service.AceptacionDonacionService;
import com.imss.sivimss.donaciones.util.AppConstantes;
import com.imss.sivimss.donaciones.util.DatosRequest;
import com.imss.sivimss.donaciones.util.MensajeResponseUtil;
import com.imss.sivimss.donaciones.util.ProviderServiceRestTemplate;
import com.imss.sivimss.donaciones.util.Response;

@Service
public class AceptacionDonacionServiceImpl implements AceptacionDonacionService {
	
	private static final String ERROR_AL_DESCARGAR_DOCUMENTO= "64"; // Error en la descarga del documento.Intenta nuevamente.
	private static final String NUMERO_FOLIO_NO_EXISTE = "85"; // El número de folio no existe. Verifica tu información.
	private static final String INFORMACION_INCOMPLETA = "Informacion incompleta";
	private static final String ATAUD_REGISTRADO = "114"; // El ataúd ya fue registrado como donado exitosamente.
	private static final String SIN_INFORMACION = "45";  // No se encontró información relacionada a tu búsqueda.
	
	@Autowired
	private ProviderServiceRestTemplate providerRestTemplate;
	
	@Value("${endpoints.dominio-crear-multiple}")
	private String urlCrearMultiple;
	
	@Value("${endpoints.dominio-consulta}")
	private String urlConsulta;
	
	@Value("${endpoints.dominio-actualizar-multiples}")
	private String urlActualizarMultiple;
	
	@Value("${plantilla.aceptacion-control-ataudes-donacion}")
	private String nombrePdfAceptacionControl;
	
	@Value("${endpoints.ms-reportes}")
	private String urlReportes;
	
	@Override
	public Response<?> detalleNombreContratante(DatosRequest request, Authentication authentication)
			throws IOException {
		DonacionRequest donacionRequest = mappeoObject(request);

		if (donacionRequest.getClaveFolio() == null) {
			throw new BadRequestException(HttpStatus.BAD_REQUEST, INFORMACION_INCOMPLETA);
		}

		return MensajeResponseUtil.mensajeConsultaResponse(providerRestTemplate.consumirServicio(
				new Donacion().detalleNombreContratante(request, donacionRequest).getDatos(), urlConsulta, authentication),
				NUMERO_FOLIO_NO_EXISTE);
	}

	@Override
	public Response<?> detalleNombreFinado(DatosRequest request, Authentication authentication) throws IOException {
		DonacionRequest donacionRequest = mappeoObject(request);

		if (donacionRequest.getClaveFolio() == null) {
			throw new BadRequestException(HttpStatus.BAD_REQUEST, INFORMACION_INCOMPLETA);
		}

		return MensajeResponseUtil.mensajeConsultaResponse(
				providerRestTemplate.consumirServicio(new Donacion().detalleNombreFinado(request, donacionRequest).getDatos(),
						urlConsulta, authentication),
				NUMERO_FOLIO_NO_EXISTE);
	}

	@Override
	public Response<?> detalleAtaudDonado(DatosRequest request, Authentication authentication) throws IOException {
		DonacionRequest donacionRequest = mappeoObject(request);

		if (donacionRequest.getClaveFolio() == null) {
			throw new BadRequestException(HttpStatus.BAD_REQUEST, INFORMACION_INCOMPLETA);
		}

		return MensajeResponseUtil.mensajeConsultaResponse(
				providerRestTemplate.consumirServicio(new Donacion().detalleAtaudDonado(request,donacionRequest).getDatos(),
						urlConsulta, authentication),
				SIN_INFORMACION);
	}
	
	private DonacionRequest mappeoObject(DatosRequest request) {
		return new Gson().fromJson(String.valueOf(request.getDatos().get(AppConstantes.DATOS)), DonacionRequest.class);
	}

	@Override
	public Response<?> insertAtaudDonado(DatosRequest request, Authentication authentication) throws IOException {
		Gson gson = new Gson();
		DonacionRequest donacionRequest = mappeoObject(request);
		UsuarioDto usuarioDto = gson.fromJson((String) authentication.getPrincipal(), UsuarioDto.class);
		Response<?> response = providerRestTemplate.consumirServicio(new Donacion().insertarDonacion(donacionRequest, usuarioDto).getDatos(),urlCrearMultiple,authentication);
		if(200 == response.getCodigo()) {
			response = providerRestTemplate.consumirServicio(new Donacion().actualizarStockArticulo(donacionRequest, usuarioDto),urlActualizarMultiple,authentication);
		}
		return MensajeResponseUtil.mensajeResponse(response , ATAUD_REGISTRADO);
	}
	
	@Override
	public Response<?> generarDocumentoAceptacionControl(DatosRequest request, Authentication authentication) throws IOException {
		Map<String, Object> envioDatos = new Donacion().generarPlantillaAceptacionControlPDF(new Gson().fromJson(String.valueOf(request.getDatos().get(AppConstantes.DATOS)), PlantillaAceptacionControlRequest.class),nombrePdfAceptacionControl);
		return MensajeResponseUtil.mensajeConsultaResponse(providerRestTemplate.consumirServicioReportes(envioDatos, urlReportes, authentication)
				, ERROR_AL_DESCARGAR_DOCUMENTO);
	}

}
