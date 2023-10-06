package com.imss.sivimss.donaciones.service.impl;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import javax.xml.bind.DatatypeConverter;

import org.modelmapper.ModelMapper;
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
import com.imss.sivimss.donaciones.model.response.ArticuloResponse;
import com.imss.sivimss.donaciones.service.AceptacionDonacionService;
import com.imss.sivimss.donaciones.util.AppConstantes;
import com.imss.sivimss.donaciones.util.DatosRequest;
import com.imss.sivimss.donaciones.util.LogUtil;
import com.imss.sivimss.donaciones.util.MensajeResponseUtil;
import com.imss.sivimss.donaciones.util.ProviderServiceRestTemplate;
import com.imss.sivimss.donaciones.util.Response;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AceptacionDonacionServiceImpl implements AceptacionDonacionService {
	
	
	private static final String ERROR_AL_DESCARGAR_DOCUMENTO= "64"; // Error en la descarga del documento.Intenta nuevamente.
	private static final String ERROR_AL_EJECUTAR_EL_QUERY = "Error al ejecutar el query ";
	private static final String FALLO_AL_EJECUTAR_EL_QUERY = "Fallo al ejecutar el query: ";
	private static final String NUMERO_FOLIO_NO_EXISTE = "85"; // El número de folio no existe. Verifica tu información.
	private static final String INFORMACION_INCOMPLETA = "Informacion incompleta";
	private static final String ATAUD_REGISTRADO = "114"; // El ataúd ya fue registrado como donado exitosamente.
	private static final String SIN_INFORMACION = "45";  // No se encontró información relacionada a tu búsqueda.
	private static final String ERROR_INFORMACION = "52";  // Error al consultar la información.
	private static final String CONSULTA_GENERICA = "/consulta";
	private static final String CONSULTA = "consulta";
	private static final String ALTA = "alta";
	
	@Autowired
	private ProviderServiceRestTemplate providerRestTemplate;
	
	@Value("${endpoints.mod-catalogos}")
	private String urlModCatalogos;
	
	@Value("${plantilla.aceptacion-control-ataudes-donacion}")
	private String nombrePdfAceptacionControl;
	
	@Value("${endpoints.ms-reportes}")
	private String urlReportes;
	
	@Autowired
	private LogUtil logUtil;
	
	private Response<Object> response;
	
	@Autowired 
	private ModelMapper modelMapper;
	
	@Override
	public Response<Object> detalleNombreContratante(DatosRequest request, Authentication authentication)
			throws IOException {
		DonacionRequest donacionRequest = mappeoObject(request);
		try {
				logUtil.crearArchivoLog(Level.INFO.toString(),this.getClass().getSimpleName(),this.getClass().getPackage().toString()," detalle nombre contratante ", CONSULTA,authentication);
		
				if (donacionRequest.getClaveFolio() == null) {
					throw new BadRequestException(HttpStatus.BAD_REQUEST, INFORMACION_INCOMPLETA);
				}
		
				return MensajeResponseUtil.mensajeConsultaResponse(providerRestTemplate.consumirServicio(new Donacion().detalleNombreContratante(request, donacionRequest).getDatos(), urlModCatalogos.concat(CONSULTA_GENERICA), authentication),
						NUMERO_FOLIO_NO_EXISTE);
		
        } catch (Exception e) {
            String consulta = new Donacion().detalleNombreContratante(request, donacionRequest).getDatos().get(AppConstantes.QUERY).toString();
            String decoded = new String(DatatypeConverter.parseBase64Binary(consulta));
            log.error(ERROR_AL_EJECUTAR_EL_QUERY + decoded);
            logUtil.crearArchivoLog(Level.SEVERE.toString(), this.getClass().getSimpleName(), this.getClass().getPackage().toString(), FALLO_AL_EJECUTAR_EL_QUERY + decoded, CONSULTA, authentication);
            throw new IOException(ERROR_INFORMACION, e.getCause());
        }
	}

	@Override
	public Response<Object> detalleNombreFinado(DatosRequest request, Authentication authentication) throws IOException {
		DonacionRequest donacionRequest = mappeoObject(request);
		try {
				logUtil.crearArchivoLog(Level.INFO.toString(),this.getClass().getSimpleName(),this.getClass().getPackage().toString()," detalle nombre finado ", CONSULTA,authentication);
		
				if (donacionRequest.getClaveFolio() == null) {
					throw new BadRequestException(HttpStatus.BAD_REQUEST, INFORMACION_INCOMPLETA);
				}
		
				return MensajeResponseUtil.mensajeConsultaResponse(providerRestTemplate.consumirServicio(new Donacion().detalleNombreFinado(request, donacionRequest).getDatos(),urlModCatalogos.concat(CONSULTA_GENERICA), authentication),
						NUMERO_FOLIO_NO_EXISTE);
		
        } catch (Exception e) {
            String consulta = new Donacion().detalleNombreFinado(request, donacionRequest).getDatos().get(AppConstantes.QUERY).toString();
            String decoded = new String(DatatypeConverter.parseBase64Binary(consulta));
            log.error(ERROR_AL_EJECUTAR_EL_QUERY + decoded);
            logUtil.crearArchivoLog(Level.SEVERE.toString(), this.getClass().getSimpleName(), this.getClass().getPackage().toString(), FALLO_AL_EJECUTAR_EL_QUERY + decoded, CONSULTA, authentication);
            throw new IOException(ERROR_INFORMACION, e.getCause());
        }
	}
	
	@Override
	public Response<Object> detalleAceptacionDonacion(DatosRequest request, Authentication authentication) throws IOException {
		DonacionRequest donacionRequest = mappeoObject(request);
		try {
				logUtil.crearArchivoLog(Level.INFO.toString(),this.getClass().getSimpleName(),this.getClass().getPackage().toString()," detalle aceptacion donacion ", CONSULTA,authentication);
		
				if (donacionRequest.getIdVelatorio() == null) {
					throw new BadRequestException(HttpStatus.BAD_REQUEST, INFORMACION_INCOMPLETA);
				}
		
				return MensajeResponseUtil.mensajeConsultaResponse(providerRestTemplate.consumirServicio(new Donacion().detalleAceptacionDonacion(request, donacionRequest).getDatos(),urlModCatalogos.concat(CONSULTA_GENERICA), authentication),
						NUMERO_FOLIO_NO_EXISTE);
		
        } catch (Exception e) {
            String consulta = new Donacion().detalleNombreFinado(request, donacionRequest).getDatos().get(AppConstantes.QUERY).toString();
            String decoded = new String(DatatypeConverter.parseBase64Binary(consulta));
            log.error(ERROR_AL_EJECUTAR_EL_QUERY + decoded);
            logUtil.crearArchivoLog(Level.SEVERE.toString(), this.getClass().getSimpleName(), this.getClass().getPackage().toString(), FALLO_AL_EJECUTAR_EL_QUERY + decoded, CONSULTA, authentication);
            throw new IOException(ERROR_INFORMACION, e.getCause());
        }
	}

	@Override
	public Response<Object> detalleAtaudDonado(DatosRequest request, Authentication authentication) throws IOException {
		DonacionRequest donacionRequest = mappeoObject(request);
		UsuarioDto usuarioDto = new Gson().fromJson((String) authentication.getPrincipal(), UsuarioDto.class);
		try {
				logUtil.crearArchivoLog(Level.INFO.toString(),this.getClass().getSimpleName(),this.getClass().getPackage().toString()," detalle nombre finado ", CONSULTA,authentication);
				
				if (donacionRequest.getClaveFolio() == null) {
					throw new BadRequestException(HttpStatus.BAD_REQUEST, INFORMACION_INCOMPLETA);
				}
		
				return MensajeResponseUtil.mensajeConsultaResponse(providerRestTemplate.consumirServicio(new Donacion().detalleAtaudDonado(request,donacionRequest,usuarioDto).getDatos(),urlModCatalogos.concat(CONSULTA_GENERICA), authentication),
						SIN_INFORMACION);
		
        } catch (Exception e) {
            String consulta = new Donacion().detalleAtaudDonado(request, donacionRequest, usuarioDto).getDatos().get(AppConstantes.QUERY).toString();
            String decoded = new String(DatatypeConverter.parseBase64Binary(consulta));
            log.error(ERROR_AL_EJECUTAR_EL_QUERY + decoded);
            logUtil.crearArchivoLog(Level.SEVERE.toString(), this.getClass().getSimpleName(), this.getClass().getPackage().toString(), FALLO_AL_EJECUTAR_EL_QUERY + decoded, CONSULTA, authentication);
            throw new IOException(ERROR_INFORMACION, e.getCause());
        }
	}
	
	private DonacionRequest mappeoObject(DatosRequest request) {
		return new Gson().fromJson(String.valueOf(request.getDatos().get(AppConstantes.DATOS)), DonacionRequest.class);
	}

	@Override
	public Response<Object> insertAtaudDonado(DatosRequest request, Authentication authentication) throws IOException {
		DonacionRequest donacionRequest = mappeoObject(request);
		UsuarioDto usuarioDto = new Gson().fromJson((String) authentication.getPrincipal(), UsuarioDto.class);
		try {
				logUtil.crearArchivoLog(Level.INFO.toString(),this.getClass().getSimpleName(),this.getClass().getPackage().toString()," insert ataud donado ", ALTA,authentication);
				response = providerRestTemplate.consumirServicio(new Donacion().insertarDonacion(donacionRequest, usuarioDto).getDatos(),urlModCatalogos.concat("/crearMultiple"),authentication);
				if(200 == response.getCodigo()) {
					response = providerRestTemplate.consumirServicio(new Donacion().actualizarStockArticulo(donacionRequest, usuarioDto),urlModCatalogos.concat("/actualizar/multiples"),authentication);
				}
				return MensajeResponseUtil.mensajeResponse(response , ATAUD_REGISTRADO);
        } catch (Exception e) {
            String consulta = new Donacion().insertarDonacion(donacionRequest, usuarioDto).getDatos().get(AppConstantes.QUERY).toString();
            String decoded = new String(DatatypeConverter.parseBase64Binary(consulta));
            log.error(ERROR_AL_EJECUTAR_EL_QUERY + decoded);
            logUtil.crearArchivoLog(Level.SEVERE.toString(), this.getClass().getSimpleName(), this.getClass().getPackage().toString(), FALLO_AL_EJECUTAR_EL_QUERY + decoded, ALTA, authentication);
            throw new IOException("5", e.getCause());
        }
	}
	
	@Override
	public Response<Object> generarDocumentoAceptacionControl(DatosRequest request, Authentication authentication) throws IOException {
		try {
		logUtil.crearArchivoLog(Level.INFO.toString(),this.getClass().getSimpleName(),this.getClass().getPackage().toString()," generar documento aceptacion control ", CONSULTA,authentication);
		PlantillaAceptacionControlRequest plantillaAceptacionControlRequest = new Gson().fromJson(String.valueOf(request.getDatos().get(AppConstantes.DATOS)), PlantillaAceptacionControlRequest.class);
		String[] numInventarios = plantillaAceptacionControlRequest.getNumInventarios().split(",");
		response = providerRestTemplate.consumirServicio(new Donacion().obtenerFolioDonacion(request, numInventarios[0]).getDatos(),urlModCatalogos.concat(CONSULTA_GENERICA), authentication);
		if (response.getCodigo() == 200 && !response.getDatos().toString().contains("[]")) {
			List<ArticuloResponse> articuloResponse = Arrays.asList(modelMapper.map(response.getDatos(), ArticuloResponse[].class));
			Map<String, Object> envioDatos = new Donacion().generarPlantillaAceptacionControlPDF(plantillaAceptacionControlRequest, nombrePdfAceptacionControl, articuloResponse.get(0).getFolioDonacion());
			return MensajeResponseUtil.mensajeConsultaResponse(providerRestTemplate.consumirServicioReportes(envioDatos, urlReportes, authentication), ERROR_AL_DESCARGAR_DOCUMENTO);
		}
        } catch (Exception e) {
        	e.printStackTrace();
        	log.error("Error.. {}", e.getMessage());
            logUtil.crearArchivoLog(Level.SEVERE.toString(), this.getClass().getSimpleName(), this.getClass().getPackage().toString(), "Fallo al ejecutar la plantilla : " + e.getMessage(), CONSULTA, authentication);
            throw new IOException("64", e.getCause());
        }
		return response;
	}

}
