package com.imss.sivimss.donaciones.service.impl;

import java.io.IOException;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.imss.sivimss.donaciones.beans.Donacion;
import com.imss.sivimss.donaciones.exception.BadRequestException;
import com.imss.sivimss.donaciones.model.request.DonacionRequest;
import com.imss.sivimss.donaciones.model.request.RolRequest;
import com.imss.sivimss.donaciones.model.request.UsuarioDto;
import com.imss.sivimss.donaciones.model.request.UsuarioRequest;
import com.imss.sivimss.donaciones.service.DonacionService;
import com.imss.sivimss.donaciones.util.AppConstantes;
import com.imss.sivimss.donaciones.util.DatosRequest;
import com.imss.sivimss.donaciones.util.MensajeResponseUtil;
import com.imss.sivimss.donaciones.util.ProviderServiceRestTemplate;
import com.imss.sivimss.donaciones.util.Response;

@Service
public class DonacionServiceImpl  implements DonacionService {

	private static final Logger log = LoggerFactory.getLogger(DonacionServiceImpl.class);
	
	private static final String AGREGADO_CORRECTAMENTE = "30"; // Agregado correctamente.
	private static final String MODIFICADO_CORRECTAMENTE = "18";  // Modificado correctamente.
	private static final String DESACTIVADO_CORRECTAMENTE = "19";  // Desactivado correctamente.
	private static final String SIN_INFORMACION = "45";  // No se encontró información relacionada a tu búsqueda.
	private static final String ACTIVADO_CORRECTAMENTE = "69";  // Activado correctamente. 
	private static final String NUMERO_FOLIO_NO_EXISTE = "85";  // El número de folio no existe. Verifica tu información.
	
	private static final String INFORMACION_INCOMPLETA = "Informacion incompleta";

	@Value("${endpoints.dominio-consulta}")
	private String urlConsulta;
	
	@Value("${endpoints.dominio-consulta-paginado}")
	private String urlConsultaPaginado;
	
	@Value("${endpoints.dominio-crear}")
	private String urlCrear;
	
	@Value("${endpoints.dominio-actualizar}")
	private String urlActualizar;
	
	@Value("${formato_fecha}")
	private String formatoFecha;

	@Autowired
	private ProviderServiceRestTemplate providerRestTemplate;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Override
	public Response<?> consultarRoles(DatosRequest request, Authentication authentication) throws IOException {
		Donacion rol= new Donacion();
		return MensajeResponseUtil.mensajeConsultaResponse( providerRestTemplate.consumirServicio(rol.obtenerRoles(request, formatoFecha).getDatos(), urlConsultaPaginado,
				authentication), SIN_INFORMACION );
	}
	
	@Override
	public Response<?> buscarFiltrosRol(DatosRequest request, Authentication authentication) throws IOException {
		Gson gson = new Gson();

		String datosJson = String.valueOf(request.getDatos().get(AppConstantes.DATOS));
		UsuarioRequest usuarioRequest = gson.fromJson(datosJson, UsuarioRequest.class);
		
		Donacion rol = new Donacion(usuarioRequest);

		return MensajeResponseUtil.mensajeConsultaResponse(providerRestTemplate.consumirServicio(rol.buscarFiltrosRol(request, rol, formatoFecha).getDatos(), urlConsultaPaginado,
				authentication), SIN_INFORMACION);
	}

	@Override
	public Response<?> detalleNombreContratante(DatosRequest request, Authentication authentication) throws IOException {
		Gson gson = new Gson();

		String datosJson = String.valueOf(request.getDatos().get(AppConstantes.DATOS));

		DonacionRequest donacionRequest = gson.fromJson(datosJson, DonacionRequest.class);
		
		if (donacionRequest.getClaveFolio()== null) {
			throw new BadRequestException(HttpStatus.BAD_REQUEST, INFORMACION_INCOMPLETA);
		}
		
		Donacion donacion = new Donacion(donacionRequest);
		
		return MensajeResponseUtil.mensajeConsultaResponse(providerRestTemplate.consumirServicio(donacion.detalleNombreContratante(request, formatoFecha).getDatos(), urlConsulta,
				authentication), NUMERO_FOLIO_NO_EXISTE);
	}
	
	@Override
	public Response<?> detalleNombreFinado(DatosRequest request, Authentication authentication) throws IOException {
		Gson gson = new Gson();

		String datosJson = String.valueOf(request.getDatos().get(AppConstantes.DATOS));

		DonacionRequest donacionRequest = gson.fromJson(datosJson, DonacionRequest.class);
		
		if (donacionRequest.getClaveFolio()== null) {
			throw new BadRequestException(HttpStatus.BAD_REQUEST, INFORMACION_INCOMPLETA);
		}
		
		Donacion donacion = new Donacion(donacionRequest);
		
		return MensajeResponseUtil.mensajeConsultaResponse(providerRestTemplate.consumirServicio(donacion.detalleNombreFinado(request, formatoFecha).getDatos(), urlConsulta,
				authentication), NUMERO_FOLIO_NO_EXISTE);
	}
	
	@Override
	public Response<?> detalleAtaudDonado(DatosRequest request, Authentication authentication) throws IOException {
		Gson gson = new Gson();

		String datosJson = String.valueOf(request.getDatos().get(AppConstantes.DATOS));

		DonacionRequest donacionRequest = gson.fromJson(datosJson, DonacionRequest.class);
		
		if (donacionRequest.getClaveFolio()== null) {
			throw new BadRequestException(HttpStatus.BAD_REQUEST, INFORMACION_INCOMPLETA);
		}
		
		Donacion donacion = new Donacion(donacionRequest);
		
		return MensajeResponseUtil.mensajeConsultaResponse(providerRestTemplate.consumirServicio(donacion.detalleAtaudDonado(request, formatoFecha).getDatos(), urlConsulta,
				authentication), NUMERO_FOLIO_NO_EXISTE);
	}

	@Override
	public Response<?> agregarRol(DatosRequest request, Authentication authentication) throws IOException {
		Gson gson = new Gson();

		String datosJson = String.valueOf(request.getDatos().get(AppConstantes.DATOS));
		
		UsuarioDto usuarioDto = gson.fromJson((String) authentication.getPrincipal(), UsuarioDto.class);

		RolRequest rolRequest = gson.fromJson(datosJson, RolRequest.class);
		Donacion rol = new Donacion(rolRequest);
		rol.setClaveAlta(usuarioDto.getIdUsuario().toString());
		return MensajeResponseUtil.mensajeResponse( providerRestTemplate.consumirServicio(rol.insertar().getDatos(), urlCrear,authentication), AGREGADO_CORRECTAMENTE);
	}

	@Override
	public Response<?> actualizarRol(DatosRequest request, Authentication authentication) throws IOException {
		Gson gson = new Gson();

		String datosJson = String.valueOf(request.getDatos().get(AppConstantes.DATOS));
		UsuarioDto usuarioDto = gson.fromJson((String) authentication.getPrincipal(), UsuarioDto.class);

		RolRequest rolRequest = gson.fromJson(datosJson, RolRequest.class);
		if (rolRequest.getIdRol() == null) {
			throw new BadRequestException(HttpStatus.BAD_REQUEST, INFORMACION_INCOMPLETA);
		}
		Donacion rol = new Donacion(rolRequest);
		rol.setClaveModifica(usuarioDto.getIdUsuario().toString());
		return MensajeResponseUtil.mensajeResponse(providerRestTemplate.consumirServicio(rol.actualizar().getDatos(), urlActualizar,
				authentication), MODIFICADO_CORRECTAMENTE);
	}
	
	@Override
	public Response<?> cambiarEstatusRol(DatosRequest request, Authentication authentication) throws IOException {
		Gson gson = new Gson();

		String datosJson = String.valueOf(request.getDatos().get(AppConstantes.DATOS));
		UsuarioDto usuarioDto = gson.fromJson((String) authentication.getPrincipal(), UsuarioDto.class);

		RolRequest rolRequest = gson.fromJson(datosJson, RolRequest.class);
		if (rolRequest.getIdRol() == null) {
			throw new BadRequestException(HttpStatus.BAD_REQUEST, INFORMACION_INCOMPLETA);
		}
		Donacion rol = new Donacion(rolRequest);
		rol.setClaveModifica(usuarioDto.getIdUsuario().toString());
		
		if (rol.getEstatusRol()  == 1) {
			return MensajeResponseUtil.mensajeResponse(providerRestTemplate.consumirServicio(rol.cambiarEstatus().getDatos(), urlActualizar,
					authentication), ACTIVADO_CORRECTAMENTE);
		} else {
			return MensajeResponseUtil.mensajeResponse(providerRestTemplate.consumirServicio(rol.cambiarEstatus().getDatos(), urlActualizar,
					authentication), DESACTIVADO_CORRECTAMENTE);
		}
	}


}