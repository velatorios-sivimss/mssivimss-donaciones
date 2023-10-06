package com.imss.sivimss.donaciones.service.impl;

import java.io.IOException;
import java.util.Map;
import java.util.HashMap;
import java.util.logging.Level;

import javax.xml.bind.DatatypeConverter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.imss.sivimss.donaciones.beans.ConsultaDonado;
import com.imss.sivimss.donaciones.beans.Donacion;
import com.imss.sivimss.donaciones.beans.SalidaDonacion;
import com.imss.sivimss.donaciones.model.request.ConsultaDonadoRequest;
import com.imss.sivimss.donaciones.model.request.PlantillaAceptacionControlRequest;
import com.imss.sivimss.donaciones.model.request.PlantillaControlSalidaDonacionRequest;
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


	@Autowired
	private LogUtil logUtil;
	
	@Value("${formato_fecha}")
	private String formatoFecha;
	

	@Value("${plantilla.consulta-donacion}")
	private String nombrePdfReportes;

	@Value("${endpoints.ms-reportes}")
	private String urlReportes;

	@Value("${plantilla.aceptacion-control-ataudes-donacion}")
	private String nombrePdfAceptacionControl;
	
	@Value("${plantilla.control-salida-ataudes-donacion}")
	private String nombrePdfControlSalida;
	
	
	@Autowired
	private ProviderServiceRestTemplate providerRestTemplate;

	ConsultaDonado consultarDonado = new ConsultaDonado();

	private Response<Object> response;
	
	private static final Double VERSION =5.2;

	private static final String GENERA_DOCUMENTO = "Genera_Documento";
	private static final String NO_SE_ENCONTRO_INFORMACION = "45"; // No se encontró información relacionada a tu
																	// búsqueda.
	private static final String ERROR_AL_DESCARGAR_DOCUMENTO = "64"; // Error en la descarga del documento.Intenta
																		// nuevamente.
	private static final String ERROR_QUERY = "Error al ejecutar el query: ";
	private static final String CONSULTA = "consulta";
	private static final String CU064_NOMBRE = "ConsultarDonados: ";
	private static final String CONSULTAR_PAGINADO = "/paginado";
	private static final String CONSULTA_GENERICA = "/consulta";
	private static final String CONULTA_FILTROS = "consu-filtrodonados: ";
	private static final String GENERAR_DOCUMENTO = "generarDocumento: ";
	private static final String CONSULTAR_DONADOS = "consultar-donados: " ;

	@Override
	public Response<Object> consultarDonados(DatosRequest request, Authentication authentication) throws IOException {
		Map<String, Object> envioDatos = consultarDonado.consultarDonados(request, formatoFecha).getDatos();
		try {
			log.info(CU064_NOMBRE + CONSULTAR_DONADOS + queryDecoded(envioDatos));
			logUtil.crearArchivoLog(Level.INFO.toString(), CU064_NOMBRE + CONSULTAR_DONADOS + this.getClass().getSimpleName(), this.getClass().getPackage().toString(), "consultarDonados", CONSULTA, authentication);
			response = providerRestTemplate.consumirServicio(envioDatos, urlModCatalogos + CONSULTAR_PAGINADO, authentication);
			return MensajeResponseUtil.mensajeConsultaResponse(response, NO_SE_ENCONTRO_INFORMACION);
		} catch (Exception e) {
			log.error( CU064_NOMBRE +CONSULTAR_DONADOS +ERROR_QUERY +  queryDecoded(envioDatos));
			logUtil.crearArchivoLog(Level.WARNING.toString(), CU064_NOMBRE + CONSULTAR_DONADOS + this.getClass().getSimpleName(),
					this.getClass().getPackage().toString(), ERROR_QUERY +  queryDecoded(envioDatos), CONSULTA, authentication);
			throw new IOException("52", e.getCause());
		}
	}

	@Override
	public Response<Object> consultaFiltroDonado(DatosRequest request, Authentication authentication) throws IOException {

		Gson gson = new Gson();
		String datosJson = String.valueOf(request.getDatos().get(AppConstantes.DATOS));
		ConsultaDonadoRequest consultaDonadoRequest = gson.fromJson(datosJson, ConsultaDonadoRequest.class);
		consultarDonado = new ConsultaDonado(consultaDonadoRequest);

		Map<String, Object> envioDatos = new HashMap<>();
		logUtil.crearArchivoLog(Level.INFO.toString(), CU064_NOMBRE + CONULTA_FILTROS +this.getClass().getSimpleName(),
				this.getClass().getPackage().toString(), "consultaFiltroDonado", CONSULTA, authentication);
		if (consultaDonadoRequest.getDonadoPor() == null) {
			envioDatos = consultarDonado.consultarDonados(request, formatoFecha).getDatos();
		} else if (consultaDonadoRequest.getDonadoPor().equals("1")) { // Instituto
			envioDatos = consultarDonado.consultarFiltroDonadosSalida(request, formatoFecha).getDatos();
		} else if (consultaDonadoRequest.getDonadoPor().equals("2")) { // ODS
			envioDatos = consultarDonado.consultarFiltroDonadosEntrada(request, formatoFecha).getDatos();
		}
	
		try {
			log.info( CU064_NOMBRE + CONULTA_FILTROS + queryDecoded(envioDatos));
			response = providerRestTemplate.consumirServicio(envioDatos, urlModCatalogos + CONSULTAR_PAGINADO,
					authentication);
			return MensajeResponseUtil.mensajeConsultaResponse(response, NO_SE_ENCONTRO_INFORMACION);
		} catch (Exception e) {
			log.error( CU064_NOMBRE + CONULTA_FILTROS + ERROR_QUERY + queryDecoded(envioDatos));
			logUtil.crearArchivoLog(Level.WARNING.toString(), CU064_NOMBRE + CONULTA_FILTROS + this.getClass().getSimpleName(),
					this.getClass().getPackage().toString(), ERROR_QUERY + queryDecoded(envioDatos), CONSULTA, authentication);
			throw new IOException("52", e.getCause());
		}
	}


	@Override
	public Response<Object> generarDocumento(DatosRequest request, Authentication authentication) throws IOException {
		Gson gson = new Gson();
		String datosJson = String.valueOf(request.getDatos().get(AppConstantes.DATOS));
		ConsultaDonadoRequest consultaDonadoRequest = gson.fromJson(datosJson, ConsultaDonadoRequest.class);
		consultarDonado = new ConsultaDonado(consultaDonadoRequest);
		ReporteDto reporteDto = gson.fromJson(datosJson, ReporteDto.class);
		Map<String, Object> envioDatos = new HashMap<>();
		if (consultaDonadoRequest.getDonadoPor() == null) {
			envioDatos = consultarDonado.generarReportePDF(reporteDto, nombrePdfReportes,0, formatoFecha);
		} else if (consultaDonadoRequest.getDonadoPor().equals("1")) { // Instituto
			envioDatos = consultarDonado.generarReportePDF(reporteDto, nombrePdfReportes,1, formatoFecha);
		} else if (consultaDonadoRequest.getDonadoPor().equals("2")) { // ODS
			envioDatos = consultarDonado.generarReportePDF(reporteDto, nombrePdfReportes,2, formatoFecha);
		}
		String queryDecoded1 = envioDatos.get("query1").toString();
		String queryUnion = envioDatos.get("union").toString();
		String queryDecoded2 = envioDatos.get("query2").toString();
		try {
			log.info( CU064_NOMBRE + GENERAR_DOCUMENTO + queryDecoded1 + queryUnion + queryDecoded2);
			logUtil.crearArchivoLog(Level.INFO.toString(), CU064_NOMBRE + GENERAR_DOCUMENTO + this.getClass().getSimpleName(),
					this.getClass().getPackage().toString(), "generarDocumento", GENERA_DOCUMENTO, authentication);
			response = providerRestTemplate.consumirServicioReportes(envioDatos, urlReportes, authentication);
			return MensajeResponseUtil.mensajeConsultaResponse(response, ERROR_AL_DESCARGAR_DOCUMENTO);
		} catch (Exception e) {
			log.error( CU064_NOMBRE + GENERAR_DOCUMENTO + ERROR_QUERY + queryDecoded1 + queryUnion + queryDecoded2);
			logUtil.crearArchivoLog(Level.WARNING.toString(), CU064_NOMBRE + GENERAR_DOCUMENTO + this.getClass().getSimpleName(),
					this.getClass().getPackage().toString(), ERROR_QUERY + queryDecoded1 + queryUnion + queryDecoded2, GENERA_DOCUMENTO,
					authentication);
			throw new IOException("52", e.getCause());
		}

	}
	private String queryDecoded (Map<String, Object> envioDatos ) {
		return new String(DatatypeConverter.parseBase64Binary(envioDatos.get(AppConstantes.QUERY).toString()));
	
	}

	@Override
	public Response<Object> generarDocumentoEntrada(DatosRequest request, Authentication authentication) throws IOException {
		Gson gson = new Gson();
		String datosJson = String.valueOf(request.getDatos().get(AppConstantes.DATOS));
		ConsultaDonadoRequest consultaDonadoRequest = gson.fromJson(datosJson, ConsultaDonadoRequest.class);
		consultarDonado = new ConsultaDonado(consultaDonadoRequest);
		ReporteDto reporteDto = gson.fromJson(datosJson, ReporteDto.class);
		String queryDecoded = "";
		Map<String, Object> envioDatos = new HashMap<>();
		try {
				envioDatos = consultarDonado.generarQueryReporteEntrada(request, reporteDto).getDatos();
			response = providerRestTemplate.consumirServicio(envioDatos, urlModCatalogos + CONSULTA_GENERICA, authentication);
			queryDecoded = queryDecoded(envioDatos);
			log.info(CU064_NOMBRE + GENERAR_DOCUMENTO + queryDecoded);
			logUtil.crearArchivoLog(Level.INFO.toString(),
					CU064_NOMBRE + GENERAR_DOCUMENTO + this.getClass().getSimpleName(), this.getClass().getPackage().toString(), "generarDocumentoEntrada", GENERA_DOCUMENTO, authentication);
			datosJson = responseToJson(response.getDatos().toString());
				PlantillaAceptacionControlRequest plantillaAceptacionControlRequest = gson.fromJson(datosJson, PlantillaAceptacionControlRequest.class);
				plantillaAceptacionControlRequest.setVersion(VERSION);
				envioDatos = new Donacion().generarPlantillaAceptacionControlPDF(plantillaAceptacionControlRequest,	nombrePdfAceptacionControl, 0);
			envioDatos.put("tipoReporte", reporteDto.getTipoReporte());
			return MensajeResponseUtil.mensajeConsultaResponse(providerRestTemplate.consumirServicioReportes(envioDatos, urlReportes, authentication)
					, ERROR_AL_DESCARGAR_DOCUMENTO);
		} catch (Exception e) {
			log.error( CU064_NOMBRE + GENERAR_DOCUMENTO + ERROR_QUERY + queryDecoded);
			logUtil.crearArchivoLog(Level.WARNING.toString(), CU064_NOMBRE + GENERAR_DOCUMENTO + this.getClass().getSimpleName(),
					this.getClass().getPackage().toString(), ERROR_QUERY + queryDecoded, GENERA_DOCUMENTO,
					authentication);
			throw new IOException("52", e.getCause());
		}

	}

	@Override
	public Response<Object> generarDocumentoSalida(DatosRequest request, Authentication authentication) throws IOException {
		Gson gson = new Gson();
		String datosJson = String.valueOf(request.getDatos().get(AppConstantes.DATOS));
		ConsultaDonadoRequest consultaDonadoRequest = gson.fromJson(datosJson, ConsultaDonadoRequest.class);
		consultarDonado = new ConsultaDonado(consultaDonadoRequest);
		ReporteDto reporteDto = gson.fromJson(datosJson, ReporteDto.class);
		String queryDecoded = "";
		Map<String, Object> envioDatos = new HashMap<>();
		try {
				envioDatos = consultarDonado.generarQueryReporteSalida(request, reporteDto, formatoFecha).getDatos();
			response = providerRestTemplate.consumirServicio(envioDatos, urlModCatalogos + CONSULTA_GENERICA, authentication);
			queryDecoded = queryDecoded(envioDatos);
			log.info(CU064_NOMBRE + GENERAR_DOCUMENTO + queryDecoded);
			logUtil.crearArchivoLog(Level.INFO.toString(),
					CU064_NOMBRE + GENERAR_DOCUMENTO + this.getClass().getSimpleName(), this.getClass().getPackage().toString(), "generarDocumentoSalida", GENERA_DOCUMENTO, authentication);
			datosJson = responseToJson(response.getDatos().toString());
				PlantillaControlSalidaDonacionRequest plantillaControlSalidaDonacionRequest = gson.fromJson(datosJson, PlantillaControlSalidaDonacionRequest.class);
				plantillaControlSalidaDonacionRequest.setVersion(VERSION);
				envioDatos = new SalidaDonacion().generarPlantillaControlSalidaDonacionPDF(plantillaControlSalidaDonacionRequest, nombrePdfControlSalida);
			
			envioDatos.put("tipoReporte", reporteDto.getTipoReporte());
			envioDatos.put("version", VERSION );
			return MensajeResponseUtil.mensajeConsultaResponse(providerRestTemplate.consumirServicioReportes(envioDatos, urlReportes, authentication)
					, ERROR_AL_DESCARGAR_DOCUMENTO);
		} catch (Exception e) {
			log.error( CU064_NOMBRE + GENERAR_DOCUMENTO + ERROR_QUERY + queryDecoded);
			logUtil.crearArchivoLog(Level.WARNING.toString(), CU064_NOMBRE + GENERAR_DOCUMENTO + this.getClass().getSimpleName(),
					this.getClass().getPackage().toString(), ERROR_QUERY + queryDecoded, GENERA_DOCUMENTO,
					authentication);
			throw new IOException("52", e.getCause());
		}

	}
	private String responseToJson(String str){
		str = str.replace("=", "\":\"");
		str = str.replace(", ", "\",\"");
		str = str.replace("}]", "\"}");
		str = str.replace("[{", "{\"");
		return str;
	}
}
