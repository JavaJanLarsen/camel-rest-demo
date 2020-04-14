package demo.server;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@Configuration
public class ServerRouteBuilder extends RouteBuilder {
	@Bean
	DateTimeHashBean hashBean() {
		return new DateTimeHashBean();
	}
	
	@Bean
	Hasher hasher() {
		return new Hasher();
	}

	@Override
	public void configure() throws Exception {
		
		// Create a route that, once a minute, gets the current date/time, hashes it ans stores it in a bean 
		from("timer://dateTimer?period=60000")
			.routeId(getClass().getSimpleName())
			.setBody(simple("${date:now}"))
			.convertBodyTo(String.class)
			.log("New time ${body}")
			.bean("hasher")
			.to("bean:hashBean?method=setDateTimeHash(${body})");
		
		// Configure a jetty server for a json-based REST service
		restConfiguration().component("jetty")
			.bindingMode(RestBindingMode.json)
			.dataFormatProperty("prettyPrint", "true")
			.port(8080);

		// create a REST service that returns the current content of the date/time hash bean
		rest("/hashtime")
			.consumes("application/json")
			.produces("application/json")
			.get("/get")
				.route()
				.to("bean:hashBean?method=getDateTimeHash()")
				.log("request for ${body}");

	}

}
