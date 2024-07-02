package by.tms.webServer;

import by.tms.model.Operation;
import by.tms.service.OperationService;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CalculatorHttpHandler implements HttpHandler {
     public OperationService operationService = new OperationService();
    @Override
    public void handle(HttpExchange exchange) throws IOException {

        String query = exchange.getRequestURI().getQuery();
        Map<String, String> stringStringMap = parseQuery(query);
        double num1 = Double.parseDouble(stringStringMap.get("num1"));
        double num2 = Double.parseDouble(stringStringMap.get("num2"));
        String type = stringStringMap.get("type");

        Operation operation = new Operation(num1, num2, type);

        Operation action = operationService.execute(operation);

        String resultStr = "Result = %s".formatted(action.getResult());

        exchange.sendResponseHeaders(200, resultStr.length());

        exchange.getResponseBody().write(resultStr.getBytes());

        exchange.getResponseBody().close();
    }

    private Map<String, String> parseQuery(String query) {
        Map<String, String> result = new HashMap<>();
        String[] strings = query.split("&");
        for (String s : strings) {
            String[] keyValue = s.split("=");
            result.put(keyValue[0], keyValue[1]);
        }
        return result;
    }
}
