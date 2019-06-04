package com.estt.tool.test.service;

import static org.mockito.Mockito.when;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import com.estt.tool.test.domain.EndPointConfig;
import com.estt.tool.test.domain.Response;
import com.estt.tool.test.logic.ConsumeEndPointLogic;
import com.estt.tool.test.redis.helper.BaseRedisHelper;
import com.estt.tool.test.utility.Utility;
import com.estt.tool.test.validator.ResponseValidator;

/**
 * Created by saurabh.yagnik on 2016/10/31.
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = com.estt.tool.test.AppConfigTest.class, loader = AnnotationConfigContextLoader.class)
public class EndPointSmokeTestServiceImplTest {

	@Rule
	public MockitoRule mockitoRule = MockitoJUnit.rule();

	@InjectMocks
	private EndPointSmokeTestService endPointSmokeTestService;

	@Mock
	private ConsumeEndPointLogic consumeEndPointLogic;

	@Mock
	private ResponseValidator responseValidator;
	
	@Mock
	private BaseRedisHelper<Integer> redisHelper;

	private List<EndPointConfig> endPoints = new ArrayList<>();

	private List<String> errorCodes = new ArrayList<>();

	private String errorFile = "/error_status_test.txt";

	private String successFile = "/error_status_test.txt";

	@Before
	public void setup() {
		EndPointConfig endPointConfig = new EndPointConfig();
		endPointConfig.setMethodType("POST");
		endPointConfig.setUrl("http://test/endpoint");
		endPointConfig.setRequestHeaders("Content-Type=\"application-json\"");
		endPointConfig.setRequestBody("[{\"x\":\"y\"}]");
		endPointConfig.setRequestParam("x=y&a=b");
		endPointConfig.setExpectedResponseCode("200");
		endPoints.add(endPointConfig);

		errorCodes = new ArrayList<String>(Arrays.asList(new String[] { "AA", "BB" }));

		String currentPath = Paths.get(".").toAbsolutePath().normalize().toString();
		errorFile = currentPath.concat(errorFile);
		successFile = currentPath.concat(successFile);
	}

	@Test
	public void testEndPointsWithNoResponse() throws Exception {
		List<String> errorCodes = new ArrayList<String>(Arrays.asList(new String[] { "AA", "BB" }));
		Response endPointResponse = null;
		Map<String, String> endPointConfigParameters = Utility.mapEndPointParameters(endPoints.get(0));
		Map<String, String> requestParameters = endPointSmokeTestService.prepareGetRequestParameters(endPoints.get(0));
		List<String> headersList = new ArrayList<String>();
		headersList.addAll(Arrays.asList(endPointConfigParameters.get("headersString").split(",")));
		String requestBody = endPointConfigParameters.get("data");
		when(consumeEndPointLogic.consume(endPointConfigParameters, requestParameters, headersList, requestBody))
				.thenReturn(endPointResponse);
		endPointSmokeTestService.testEndPoints(endPoints, errorCodes, errorFile, "test");
	}

	@Test
	public void testEndPointsWithTextPlainResponse() throws Exception {
		Response endPointResponse = new Response();
		endPointResponse.setStatusCode("200");
		endPointResponse.setResponseBody("body");
		endPointResponse.setResponseHeader("content-type=text/plain");
		Map<String, String> endPointConfigParameters = Utility.mapEndPointParameters(endPoints.get(0));
		Map<String, String> requestParameters = endPointSmokeTestService.prepareGetRequestParameters(endPoints.get(0));
		List<String> headersList = new ArrayList<String>();
		headersList.addAll(Arrays.asList(endPointConfigParameters.get("headersString").split(",")));
		String requestBody = endPointConfigParameters.get("data");
		when(consumeEndPointLogic.consume(endPointConfigParameters, requestParameters, headersList, requestBody))
				.thenReturn(endPointResponse);
		endPointSmokeTestService.testEndPoints(endPoints, errorCodes, errorFile, "test");
	}

	@Test
	public void testEndPointsWithXMLResponse() throws Exception {
		Response endPointResponse = new Response();
		endPointResponse.setStatusCode("200");
		endPointResponse.setResponseBody("<xml></xml>");
		endPointResponse.setResponseHeader("content-type=application/xml");
		Map<String, String> endPointConfigParameters = Utility.mapEndPointParameters(endPoints.get(0));
		Map<String, String> requestParameters = endPointSmokeTestService.prepareGetRequestParameters(endPoints.get(0));
		List<String> headersList = new ArrayList<String>();
		headersList.addAll(Arrays.asList(endPointConfigParameters.get("headersString").split(",")));
		String requestBody = endPointConfigParameters.get("data");
		when(consumeEndPointLogic.consume(endPointConfigParameters, requestParameters, headersList, requestBody))
				.thenReturn(endPointResponse);
		endPointSmokeTestService.testEndPoints(endPoints, errorCodes, errorFile, "test");
	}

	@Test
	public void testEndPointsWithJsonResponse() throws Exception {
		Response endPointResponse = new Response();
		endPointResponse.setStatusCode("200");
		endPointResponse.setResponseBody("{\"error\":\"PA001\"}");
		endPointResponse.setResponseHeader("content-type=application/json");
		Map<String, String> endPointConfigParameters = Utility.mapEndPointParameters(endPoints.get(0));
		Map<String, String> requestParameters = endPointSmokeTestService.prepareGetRequestParameters(endPoints.get(0));
		List<String> headersList = new ArrayList<String>();
		headersList.addAll(Arrays.asList(endPointConfigParameters.get("headersString").split(",")));
		String requestBody = endPointConfigParameters.get("data");
		when(consumeEndPointLogic.consume(endPointConfigParameters, requestParameters, headersList, requestBody))
				.thenReturn(endPointResponse);
		endPointSmokeTestService.testEndPoints(endPoints, errorCodes, errorFile, "test");
		// assertTrue(response != null);
	}

	@Test
	public void testEndPointsWithNo200Status() throws Exception {
		Response endPointResponse = new Response();
		endPointResponse.setStatusCode("400");
		endPointResponse.setResponseBody("{\"error\":\"PA001\"}");
		endPointResponse.setResponseHeader("content-type=application/json");
		Map<String, String> endPointConfigParameters = Utility.mapEndPointParameters(endPoints.get(0));
		Map<String, String> requestParameters = endPointSmokeTestService.prepareGetRequestParameters(endPoints.get(0));
		List<String> headersList = new ArrayList<String>();
		headersList.addAll(Arrays.asList(endPointConfigParameters.get("headersString").split(",")));
		String requestBody = endPointConfigParameters.get("data");
		when(consumeEndPointLogic.consume(endPointConfigParameters, requestParameters, headersList, requestBody))
				.thenReturn(endPointResponse);
		endPointSmokeTestService.testEndPoints(endPoints, errorCodes, errorFile, "test");
		// assertTrue(response != null);
	}
}
