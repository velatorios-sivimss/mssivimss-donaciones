package com.imss.sivimss.donaciones.service.impl;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;

import javax.xml.bind.DatatypeConverter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.imss.sivimss.donaciones.beans.ConsultaDonado;
import com.imss.sivimss.donaciones.model.request.ConsultaDonadoRequest;
import com.imss.sivimss.donaciones.model.request.ReporteDto;
import com.imss.sivimss.donaciones.service.ConsultaDonadosService;
import com.imss.sivimss.donaciones.util.AppConstantes;
import com.imss.sivimss.donaciones.util.DatosRequest;
import com.imss.sivimss.donaciones.util.LogUtil;
import com.imss.sivimss.donaciones.util.MensajeResponseUtil;
import com.imss.sivimss.donaciones.util.ProviderServiceRestTemplate;
import com.imss.sivimss.donaciones.util.Response;

import lombok.extern.slf4j.Slf4j;


@Service
@Slf4j
public class ConsultaDonadoServiceImpl implements ConsultaDonadosService {

	@Value("${endpoints.mod-catalogos}")
	private String urlModCatalogos;
	
	private static final String CONSULTA = "/consulta";
	private static final String CONSULTA_PAGINADO = "/paginado";

	@Autowired
	private LogUtil logUtil;
	
	@Value("${formato_fecha}")
	private String formatoFecha;
	

	@Value("${endpoints.pdf-reporteDonados}")
	private String nombrePdfReportes;

	@Value("${endpoints.ms-reportes}")
	private String urlReportes;

	@Autowired
	private ProviderServiceRestTemplate providerRestTemplate;

	ConsultaDonado consultarDonado = new ConsultaDonado();

	private Response<?> response;

	private static final String GENERA_DOCUMENTO = "Genera_Documento";
	private static final String NO_SE_ENCONTRO_INFORMACION = "45"; // No se encontró información relacionada a tu
																	// búsqueda.
	private static final String ERROR_AL_DESCARGAR_DOCUMENTO= "64"; // Error en la descarga del documento.Intenta nuevamente.
	private static final String ERROR_QUERY = "Error al ejecutar el query ";

	@Override
	public Response<?> consultarDonados(DatosRequest request, Authentication authentication)
			throws IOException {
		try {
			logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName()+ ".consultarDonados", this.getClass().getPackage().toString(), "consultarDonados", CONSULTA, authentication);
			response = providerRestTemplate.consumirServicio(consultarDonado.consultarDonados(request, formatoFecha).getDatos(),
				urlModCatalogos+CONSULTA_PAGINADO, authentication);
			return MensajeResponseUtil.mensajeConsultaResponse( response, NO_SE_ENCONTRO_INFORMACION);
		} catch (Exception e) {
			String consulta = consultarDonado.consultarDonados(request, formatoFecha).getDatos().get(AppConstantes.QUERY).toString();
			String decoded = new String(DatatypeConverter.parseBase64Binary(consulta));
			log.error( ERROR_QUERY+ decoded);
			logUtil.crearArchivoLog(Level.WARNING.toString(), this.getClass().getSimpleName(), this.getClass().getPackage().toString(), ERROR_QUERY + decoded, CONSULTA,
					authentication);
			throw new IOException("52", e.getCause());
		}
	}

	
	@Override
	public Response<?> consultaFiltroDonado(DatosRequest request, Authentication authentication)
			throws IOException {

		Gson gson = new Gson();
		String datosJson = String.valueOf(request.getDatos().get(AppConstantes.DATOS));
		ConsultaDonadoRequest consultaDonadoRequest = gson.fromJson(datosJson, ConsultaDonadoRequest.class);
		consultarDonado = new ConsultaDonado(consultaDonadoRequest);

			Map<String, Object> datos = null;
			logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName()+ ".consultaFiltroDonado", this.getClass().getPackage().toString(), "consultaFiltroDonado", CONSULTA, authentication);
			if(consultaDonadoRequest.getDonadoPor() == null) {
				datos = consultarDonado.consultarDonados(request, formatoFecha).getDatos();
			}else if(consultaDonadoRequest.getDonadoPor().equals("1") ){
				datos = consultarDonado.consultarFiltroDonadosSalida(request, formatoFecha).getDatos();
			}else if(consultaDonadoRequest.getDonadoPor().equals("2") ){
				datos = consultarDonado.consultarFiltroDonadosEntrada(request, formatoFecha).getDatos();
			}
		try {
			response = providerRestTemplate.consumirServicio(datos, urlModCatalogos+CONSULTA_PAGINADO, authentication);
			return MensajeResponseUtil.mensajeConsultaResponse( response, NO_SE_ENCONTRO_INFORMACION);
		} catch (Exception e) {
			String consulta = datos.get(AppConstantes.QUERY).toString();
			String decoded = new String(DatatypeConverter.parseBase64Binary(consulta));
			log.error( ERROR_QUERY+ decoded);
			logUtil.crearArchivoLog(Level.WARNING.toString(), this.getClass().getSimpleName(), this.getClass().getPackage().toString(), ERROR_QUERY+ decoded, CONSULTA,
					authentication);
			throw new IOException("52", e.getCause());
		}
	}
	

	@Override
	public Response<?> consultarDelegacion(DatosRequest request, Authentication authentication)
			throws IOException {
		Map<String, Object> envioDatos = consultarDonado.obtenerDelegaciones(request).getDatos();
		
		try {
			logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName()+ ".consultarDelegacion", this.getClass().getPackage().toString(), "consultarDelegacion", CONSULTA, authentication);
			response = providerRestTemplate.consumirServicio(envioDatos,
							urlModCatalogos+CONSULTA, authentication);
			return MensajeResponseUtil.mensajeConsultaResponse( response, NO_SE_ENCONTRO_INFORMACION);
		} catch (Exception e) {
			String consulta = envioDatos.get(AppConstantes.QUERY).toString();
			String decoded = new String(DatatypeConverter.parseBase64Binary(consulta));
			log.error( ERROR_QUERY+ decoded);
			logUtil.crearArchivoLog(Level.WARNING.toString(), this.getClass().getSimpleName(), this.getClass().getPackage().toString(), ERROR_QUERY + decoded, CONSULTA,
					authentication);
			throw new IOException("52", e.getCause());
		}
	}
	@Override
	public Response<?> consultarVelatorio(DatosRequest request, Authentication authentication)
			throws IOException {

		Map<String, Object> envioDatos = consultarDonado.obtenerVelatorio(request).getDatos();

		try {
			logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName()+ ".consultarVelatorio", this.getClass().getPackage().toString(), "consultarVelatorio", CONSULTA, authentication);
			response = providerRestTemplate.consumirServicio(envioDatos,
							urlModCatalogos+CONSULTA, authentication);
			return MensajeResponseUtil.mensajeConsultaResponse( response, NO_SE_ENCONTRO_INFORMACION);
		} catch (Exception e) {
			String consulta = envioDatos.get(AppConstantes.QUERY).toString();
			String decoded = new String(DatatypeConverter.parseBase64Binary(consulta));
			log.error( ERROR_QUERY+ decoded);
			logUtil.crearArchivoLog(Level.WARNING.toString(), this.getClass().getSimpleName(), this.getClass().getPackage().toString(), ERROR_QUERY + decoded, CONSULTA,
					authentication);
			throw new IOException("52", e.getCause());
		}
	}

	@Override
	public Response<?> consultarNivel(DatosRequest request, Authentication authentication)
			throws IOException {

		Map<String, Object> envioDatos = consultarDonado.obtenerNivel(request).getDatos();

		try {
			logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName()+ ".consultarNivel", this.getClass().getPackage().toString(), "consultarNivel", CONSULTA, authentication);
			response = providerRestTemplate.consumirServicio(envioDatos,
							urlModCatalogos+CONSULTA, authentication);
			return MensajeResponseUtil.mensajeConsultaResponse( response, NO_SE_ENCONTRO_INFORMACION);
		} catch (Exception e) {
			String consulta = envioDatos.get(AppConstantes.QUERY).toString();
			String decoded = new String(DatatypeConverter.parseBase64Binary(consulta));
			log.error( ERROR_QUERY+ decoded);
			logUtil.crearArchivoLog(Level.WARNING.toString(), this.getClass().getSimpleName(), this.getClass().getPackage().toString(), ERROR_QUERY + decoded, CONSULTA,
					authentication);
			throw new IOException("52", e.getCause());
		}
	}

	@Override
	public Response<?> generarDocumento(DatosRequest request, Authentication authentication)throws IOException {
		Gson gson = new Gson();
		String datosJson = String.valueOf(request.getDatos().get(AppConstantes.DATOS));
		ConsultaDonadoRequest consultaDonadoRequest = gson.fromJson(datosJson, ConsultaDonadoRequest.class);
		consultarDonado = new ConsultaDonado(consultaDonadoRequest);
		ReporteDto reporteDto= gson.fromJson(datosJson, ReporteDto.class);
		Map<String, Object> envioDatos = consultarDonado.generarReportePDF(reporteDto,nombrePdfReportes);
		try {
			logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName()+ ".generarDocumento", this.getClass().getPackage().toString(), "generarDocumento", GENERA_DOCUMENTO, authentication);
			response = providerRestTemplate.consumirServicioReportes(envioDatos, urlReportes, authentication);
			return MensajeResponseUtil.mensajeConsultaResponse(response, ERROR_AL_DESCARGAR_DOCUMENTO);
		} catch (Exception e) {
			String consulta = envioDatos.get("condicion").toString();
			String decoded = new String(DatatypeConverter.parseBase64Binary(consulta));
			log.error(ERROR_QUERY + decoded);
			logUtil.crearArchivoLog(Level.WARNING.toString(), this.getClass().getSimpleName(), this.getClass().getPackage().toString(), ERROR_QUERY + consulta, GENERA_DOCUMENTO,
					authentication);
			throw new IOException("52", e.getCause());
		}
		
	}
}
