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
	TimeBean timeBean() {
		return new TimeBean();
	}
	
	@Bean
	Hasher hasher() {
		return new Hasher();
	}

	@Override
	public void configure() throws Exception {
		from("timer://dateTimer?period=60000")
			.routeId(getClass().getSimpleName())
			.setBody(simple("date:now"))
			.convertBodyTo(String.class)
			.log("${body}")
			.bean("hasher")
			.log("${body}")
			.to("bean:timeBean?method=setDateTimeHash(${body})");
		
		restConfiguration().component("jetty")
			.bindingMode(RestBindingMode.json)
			.dataFormatProperty("prettyPrint", "true")
			.port(8080);

		rest("/hashtime")
			.consumes("application/json")
			.produces("application/json")
			.get("/get")
				.to("bean:timeBean?method=getDateTimeHash(${header.id})");

	}

}
