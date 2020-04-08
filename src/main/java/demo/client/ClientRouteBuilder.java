package demo.client;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@Configuration
public class ClientRouteBuilder extends RouteBuilder {
	@Bean
	DeHasher deHasher() {
		return new DeHasher();
	}

	@Override
	public void configure() throws Exception {
		restConfiguration().component("jetty")
			.bindingMode(RestBindingMode.off)
			.port(8081);

		rest("/")
			.consumes("text/html")
			.produces("text/html")
			.get("/time")
				.route()
				.setBody(simple(""))
				.to("http://localhost:8080/hashtime/get?bridgeEndpoint=true")
				.unmarshal().json(JsonLibrary.Jackson, String.class)
				.log("${body}")
				.bean("deHasher")
				.setHeader(Exchange.CONTENT_TYPE, simple("text/html"))
				.setBody(simple("<html><body>Servertid er ${body} </body></html>"));

	}

}
