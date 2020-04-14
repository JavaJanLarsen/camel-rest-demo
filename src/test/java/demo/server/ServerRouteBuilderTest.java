package demo.server;

import org.apache.camel.RoutesBuilder;
import org.apache.camel.builder.AdviceWithRouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.impl.JndiRegistry;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;


public class ServerRouteBuilderTest extends CamelTestSupport{

	
	@Override
	protected JndiRegistry createRegistry() throws Exception {
		JndiRegistry res = super.createRegistry();
		res.bind("hasher", new Hasher());
		return res;	}

	@Override
	protected RoutesBuilder createRouteBuilder() throws Exception {
		return new ServerRouteBuilder();
	}

	@Test
	public void test() throws Exception {
        context.getRouteDefinitions().get(0).adviceWith(context, new AdviceWithRouteBuilder() {
            @Override
            public void configure() throws Exception {
                                  mockEndpoints();
                                  this.replaceFromWith("direct:timer");
            }
        });
        MockEndpoint resultEndpoint = getMockEndpoint("mock:bean:hashBean");
        resultEndpoint.expectedMessageCount(1);
        resultEndpoint.message(0).body().isNotNull();
        resultEndpoint.message(0).body().not().equals("");
        
        sendBody("direct:timer", "");
        assertMockEndpointsSatisfied();
	}

}
