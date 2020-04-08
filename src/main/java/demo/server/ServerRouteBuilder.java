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
		from("timer://dateTimer?period=60000")
			.routeId(getClass().getSimpleName())
			.setBody(simple("${date:now}"))
			.convertBodyTo(String.class)
			.log("New time ${body}")
			.bean("hasher")
			.to("bean:hashBean?method=setDateTimeHash(${body})");
		
		restConfiguration().component("jetty")
			.bindingMode(RestBindingMode.json)
			.dataFormatProperty("prettyPrint", "true")
			.port(8080);

		rest("/hashtime")
			.consumes("application/json")
			.produces("application/json")
			.get("/get")
				.route()
				.to("bean:hashBean?method=getDateTimeHash(${header.id})")
				.log("request for ${body}");

	}

}
