package com.imss.sivimss.donaciones.service.impl;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;

import javax.xml.bind.DatatypeConverter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.imss.sivimss.donaciones.beans.Donacion;
import com.imss.sivimss.donaciones.beans.SalidaDonacion;
import com.imss.sivimss.donaciones.exception.BadRequestException;
import com.imss.sivimss.donaciones.model.request.AgregarArticuloRequest;
import com.imss.sivimss.donaciones.model.request.DonacionRequest;
import com.imss.sivimss.donaciones.model.request.PlantillaControlSalidaDonacionRequest;
import com.imss.sivimss.donaciones.model.request.UsuarioDto;
import com.imss.sivimss.donaciones.service.SalidaDonacionService;
import com.imss.sivimss.donaciones.util.AppConstantes;
import com.imss.sivimss.donaciones.util.DatosRequest;
import com.imss.sivimss.donaciones.util.LogUtil;
import com.imss.sivimss.donaciones.util.MensajeResponseUtil;
import com.imss.sivimss.donaciones.util.ProviderServiceRestTemplate;
import com.imss.sivimss.donaciones.util.Response;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SalidaDonacionServiceImpl implements SalidaDonacionService {
	
	
	private static final String ERROR_AL_DESCARGAR_DOCUMENTO= "64"; // Error en la descarga del documento.Intenta nuevamente.
	private static final String ERROR_AL_EJECUTAR_EL_QUERY = "Error al ejecutar el query ";
	private static final String FALLO_AL_EJECUTAR_EL_QUERY = "Fallo al ejecutar el query: ";
	private static final String REGISTRADO_CORRECTAMENTE = "118"; // Se ha registrado correctamente el registro de salida de ataúdes de donación.
	private static final String INFORMACION_INCOMPLETA = "Informacion incompleta";
	private static final String ERROR_INFORMACION = "52";  // Error al consultar la información.
	private static final String YA_NO_HAY_STOCK = "15";  // Ya no hay stock de este artículo.
	private static final String R_F_C_NO_VALIDO = "33"; // R.F.C. no valido.
	private static final String SIN_INFORMACION = "45";  // No se encontró información relacionada a tu búsqueda.
	private static final String CURP_NO_VALIDO = "34"; // CURP no valido.
	private static final String NOM_PERSONA = "nomPersona";
	private static final String CONSULTA = "consulta";
	private static final String ALTA = "alta";
	
	@Value("${endpoints.mod-catalogos}")
	private String urlModCatalogos;

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
	private LogUtil logUtil;

	@Autowired
	private ProviderServiceRestTemplate providerRestTemplate;
	

	@Override
	public Response<Object> detalleContratanteRfc(DatosRequest request, Authentication authentication) throws IOException {
		DonacionRequest donacionRequest = mappeoObject(request);
		try {
				logUtil.crearArchivoLog(Level.INFO.toString(),this.getClass().getSimpleName(),this.getClass().getPackage().toString()," detalle contratante rfc ", CONSULTA,authentication);
		
				if (donacionRequest.getRfc() == null) {
					throw new BadRequestException(HttpStatus.BAD_REQUEST, INFORMACION_INCOMPLETA);
				}
				
				Response<Object> response = providerRestTemplate.consumirServicioObject(new SalidaDonacion().detalleContratanteRfc(request,donacionRequest).getDatos(), urlModCatalogos.concat("/consulta"), authentication);

				if(response.getCodigo() == 200 && response.getDatos().toString().contains(NOM_PERSONA)) {
					response.setMensaje("interno");
					return response;
				} else if (response.getCodigo() == 200 && !response.getDatos().toString().contains(NOM_PERSONA)) {
					response = providerRestTemplate.consumirServicioObject("/"+urlRfc.concat(donacionRequest.getRfc()), 0);
					if(response.getCodigo() == 200 && response.getDatos().toString().toLowerCase().contains("ACTIVO".toLowerCase())) {
						response.setMensaje("externo");
						return response;
					} else {
						return MensajeResponseUtil.mensajeConsultaResponseObject(response,R_F_C_NO_VALIDO);
					}
				} 
				return MensajeResponseUtil.mensajeConsultaResponseObject(response,"5");
	    } catch (Exception e) {
	        String consulta = new SalidaDonacion().detalleContratanteRfc(request, donacionRequest).getDatos().get(AppConstantes.QUERY).toString();
	        String decoded = new String(DatatypeConverter.parseBase64Binary(consulta));
	        log.error(ERROR_AL_EJECUTAR_EL_QUERY + decoded);
	        logUtil.crearArchivoLog(Level.SEVERE.toString(), this.getClass().getSimpleName(), this.getClass().getPackage().toString(), FALLO_AL_EJECUTAR_EL_QUERY + decoded, CONSULTA, authentication);
	        throw new IOException(ERROR_INFORMACION, e.getCause());
	    }
		
	}
	
	@Override
	public Response<Object> detalleContratanteCurp(DatosRequest request, Authentication authentication) throws IOException {
		DonacionRequest donacionRequest = mappeoObject(request);
		try {
				logUtil.crearArchivoLog(Level.INFO.toString(),this.getClass().getSimpleName(),this.getClass().getPackage().toString()," detalle contratante curp ", CONSULTA,authentication);
		
				if (donacionRequest.getCurp() == null) {
					throw new BadRequestException(HttpStatus.BAD_REQUEST, INFORMACION_INCOMPLETA);
				}
				
				Response<Object> response = providerRestTemplate.consumirServicioObject(new SalidaDonacion().detalleContratanteCurp(request,donacionRequest).getDatos(),urlModCatalogos.concat("/consulta"), authentication);
				
				if(response.getCodigo() == 200 && response.getDatos().toString().contains(NOM_PERSONA)) {
					response.setMensaje("interno");
					return response;
				} else if (response.getCodigo() == 200 && !response.getDatos().toString().contains(NOM_PERSONA)) {
					response = providerRestTemplate.consumirServicioObject("/"+urlCurp.concat(donacionRequest.getCurp()), 1);
					if(response.getCodigo() == 200 && !response.getDatos().toString().toLowerCase().contains("LA CURP NO SE ENCUENTRA EN LA BASE DE DATOS".toLowerCase())) {
						response.setMensaje("externo");
						return response;
					} else {
						return MensajeResponseUtil.mensajeConsultaResponseObject(response,CURP_NO_VALIDO);
					}
				} 
				return MensajeResponseUtil.mensajeConsultaResponseObject(response,"5");
	    } catch (Exception e) {
	        String consulta = new SalidaDonacion().detalleContratanteCurp(request,donacionRequest).getDatos().get(AppConstantes.QUERY).toString();
	        String decoded = new String(DatatypeConverter.parseBase64Binary(consulta));
	        log.error(ERROR_AL_EJECUTAR_EL_QUERY + decoded);
	        logUtil.crearArchivoLog(Level.SEVERE.toString(), this.getClass().getSimpleName(), this.getClass().getPackage().toString(), FALLO_AL_EJECUTAR_EL_QUERY + decoded, CONSULTA, authentication);
	        throw new IOException(ERROR_INFORMACION, e.getCause());
	    }
	}
	
	@Override
	public Response<Object> detalleSalidaAtaudDonado(DatosRequest request, Authentication authentication) throws IOException  {
		UsuarioDto usuarioDto = new Gson().fromJson((String) authentication.getPrincipal(), UsuarioDto.class);
		try {
				logUtil.crearArchivoLog(Level.INFO.toString(),this.getClass().getSimpleName(),this.getClass().getPackage().toString()," detalle salida ataud donado ", CONSULTA,authentication);
				return MensajeResponseUtil.mensajeConsultaResponse(providerRestTemplate.consumirServicio(new SalidaDonacion().detalleSalidaAtaudDonado(request, usuarioDto).getDatos(),
						urlModCatalogos.concat("/consulta"), authentication),
					SIN_INFORMACION);
	    } catch (Exception e) {
	        String consulta = new SalidaDonacion().detalleSalidaAtaudDonado(request, usuarioDto).getDatos().get(AppConstantes.QUERY).toString();
	        String decoded = new String(DatatypeConverter.parseBase64Binary(consulta));
	        log.error(ERROR_AL_EJECUTAR_EL_QUERY + decoded);
	        logUtil.crearArchivoLog(Level.SEVERE.toString(), this.getClass().getSimpleName(), this.getClass().getPackage().toString(), FALLO_AL_EJECUTAR_EL_QUERY + decoded, CONSULTA, authentication);
	        throw new IOException(ERROR_INFORMACION, e.getCause());
	    }
	}
	
	@Override
	public Response<Object> cantidadSalidaAtaudDonado(DatosRequest request, Authentication authentication) throws IOException  {
		AgregarArticuloRequest agregarArticuloRequest = new Gson().fromJson(String.valueOf(request.getDatos().get(AppConstantes.DATOS)), AgregarArticuloRequest.class);
		try {
					logUtil.crearArchivoLog(Level.INFO.toString(),this.getClass().getSimpleName(),this.getClass().getPackage().toString()," count salida ataud donado ", CONSULTA,authentication);
			
					if (agregarArticuloRequest.getModeloArticulo() == null) {
						throw new BadRequestException(HttpStatus.BAD_REQUEST, INFORMACION_INCOMPLETA);
					}
					
					Response<Object> respuestaGenerado = providerRestTemplate.consumirServicio(new SalidaDonacion().countSalidaAtaudDonado(request, agregarArticuloRequest).getDatos(),
							urlModCatalogos.concat("/consulta"), authentication);
					if(respuestaGenerado.getCodigo() == 200 &&  (respuestaGenerado.getDatos().toString().contains("0"))) {
						return MensajeResponseUtil.mensajeConsultaResponse(respuestaGenerado, YA_NO_HAY_STOCK);
					}
					return respuestaGenerado;
	    } catch (Exception e) {
	        String consulta = new SalidaDonacion().countSalidaAtaudDonado(request, agregarArticuloRequest).getDatos().get(AppConstantes.QUERY).toString();
	        String decoded = new String(DatatypeConverter.parseBase64Binary(consulta));
	        log.error(ERROR_AL_EJECUTAR_EL_QUERY + decoded);
	        logUtil.crearArchivoLog(Level.SEVERE.toString(), this.getClass().getSimpleName(), this.getClass().getPackage().toString(), FALLO_AL_EJECUTAR_EL_QUERY + decoded, CONSULTA, authentication);
	        throw new IOException(ERROR_INFORMACION, e.getCause());
	    }
	}
	
	@Override
	public Response<Object> insertSalidaAtaudDonado(DatosRequest request, Authentication authentication) throws IOException {
		DonacionRequest donacionRequest = mappeoObject(request);
		UsuarioDto usuarioDto = new Gson().fromJson((String) authentication.getPrincipal(), UsuarioDto.class);
		try {
					logUtil.crearArchivoLog(Level.INFO.toString(),this.getClass().getSimpleName(),this.getClass().getPackage().toString()," insert salida ataud donado ", ALTA,authentication);
					
					Response<Object> response = providerRestTemplate.consumirServicio(new SalidaDonacion().insertPersona(donacionRequest, usuarioDto),urlModCatalogos.concat("/insercion/salida/donacion"),authentication);
					if(200 == response.getCodigo()) {
						response = providerRestTemplate.consumirServicio(new SalidaDonacion().actualizarStockArticulo(donacionRequest, usuarioDto),urlModCatalogos.concat("/actualizar/multiples"),authentication);
					}
					
					return MensajeResponseUtil.mensajeResponse(response, REGISTRADO_CORRECTAMENTE);
        } catch (Exception e) {
        	String consulta = new Donacion().insertarDonacion(donacionRequest, usuarioDto).getDatos().get(AppConstantes.QUERY).toString();
            String decoded = new String(DatatypeConverter.parseBase64Binary(consulta));
            log.error(ERROR_AL_EJECUTAR_EL_QUERY + decoded);
            logUtil.crearArchivoLog(Level.SEVERE.toString(), this.getClass().getSimpleName(), this.getClass().getPackage().toString(), FALLO_AL_EJECUTAR_EL_QUERY + decoded, ALTA, authentication);
            throw new IOException("5", e.getCause());
        }
	}
	
	private DonacionRequest mappeoObject(DatosRequest request) {
		return new Gson().fromJson(String.valueOf(request.getDatos().get(AppConstantes.DATOS)), DonacionRequest.class);
	}


	@Override
	public Response<Object> generarDocumentoControlSalidaDonacion(DatosRequest request, Authentication authentication)
			throws IOException {
		try {
				logUtil.crearArchivoLog(Level.INFO.toString(),this.getClass().getSimpleName(),this.getClass().getPackage().toString()," generar documento control salida donacion ", CONSULTA,authentication);
				Map<String, Object> envioDatos = new SalidaDonacion().generarPlantillaControlSalidaDonacionPDF(new Gson().fromJson(String.valueOf(request.getDatos().get(AppConstantes.DATOS)), PlantillaControlSalidaDonacionRequest.class),nombrePdfControlSalida);
				return MensajeResponseUtil.mensajeConsultaResponse(providerRestTemplate.consumirServicioReportes(envioDatos, urlReportes, authentication)
						, ERROR_AL_DESCARGAR_DOCUMENTO);
        } catch (Exception e) {
        	log.error("Error.. {}", e.getMessage());
            logUtil.crearArchivoLog(Level.SEVERE.toString(), this.getClass().getSimpleName(), this.getClass().getPackage().toString(), "Fallo al ejecutar la plantilla : " + e.getMessage(), CONSULTA, authentication);
            throw new IOException("64", e.getCause());
        }
	}

}