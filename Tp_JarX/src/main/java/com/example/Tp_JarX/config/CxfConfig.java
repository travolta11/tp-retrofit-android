package com.example.Tp_JarX.config;

import org.apache.cxf.Bus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.Tp_JarX.ws.CompteSoapService;

@Configuration
public class CxfConfig {

	private CompteSoapService compteSoapService;
	private Bus bus;

	public CxfConfig(CompteSoapService compteSoapService, Bus bus) {
		super();
		this.compteSoapService = compteSoapService;
		this.bus = bus;
	}
	
	@Bean
	public EndpointImpl endpoint() {
		EndpointImpl endpoint = new EndpointImpl(bus, compteSoapService);
		endpoint.publish("/ws");
		return endpoint;
	}

}
