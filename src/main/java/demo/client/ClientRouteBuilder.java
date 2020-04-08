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
	DateTimeBean timeBean() {
		return new DateTimeBean();
	}
	
	@Bean
	DeHasher deHasher() {
		return new DeHasher();
	}

	@Override
	public void configure() throws Exception {
		
		from("timer://dateTimer?period=5000")
			.filter(simple("${random(10)} < 1"))
			.to("http://localhost:8080/hashtime/get")
			.unmarshal().json(JsonLibrary.Jackson, String.class)
			.bean("deHasher")
			.to("bean:timeBean?method=setDateTime(${body})");
			
		restConfiguration().component("jetty")
			.bindingMode(RestBindingMode.off)
			.port(8081);		
		
		rest("/")
			.consumes("text/html")
			.produces("text/html")
			.get("/time")
				.route()
				.log("Client call ")
				.bean("timeBean", "getDateTime")
				.setHeader(Exchange.CONTENT_TYPE, simple("text/html"))
				.setBody(simple("<html><body>Servertid er ${body} </body></html>"));

	}

}
