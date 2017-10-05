package com.serverless;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import clojure.java.api.Clojure;
import clojure.lang.IFn;

public class Handler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

	private static final Logger LOG = Logger.getLogger(Handler.class);
	private static final IFn load_string = Clojure.var("clojure.core", "load-string");
	private static final IFn evaluator = (IFn) load_string.invoke("(fn [input context src] (pr-str ((load-string src) input context)))");
	

	@Override
	public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
		String responseBody = (String) evaluator.invoke(input, context, input.get("body"));
		int statusCode = 200;
		Map<String, String> headers = new HashMap<String, String>();
		boolean base64Encoded = false;
		return new ApiGatewayResponse(statusCode, responseBody, headers, base64Encoded);
	}
	
	public static void main(String[] args) throws Exception {
		System.out.println(evaluator.invoke(null, null, "(fn [input context] input)"));
	}
}
