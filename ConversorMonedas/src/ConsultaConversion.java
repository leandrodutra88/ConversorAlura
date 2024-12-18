import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

// Es responsable de realizar las consultas a una API para obtener las tasas de cambio entre diferentes monedas.
public class ConsultaConversion {
    public String buscaConversion(String monedaBase, String monedaObjetivo, double cantidad) {
        try {
            URI direccion = URI.create("https://v6.exchangerate-api.com/v6/2a3bde287582a3bf7ecac969/pair/" +
                    monedaBase + "/" + monedaObjetivo + "/" + cantidad);

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(direccion)
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            String json = response.body();
            System.out.println("Respuesta de la API: " + json); // Imprimir respuesta para depuración

            JsonElement jsonElement = JsonParser.parseString(json);
            JsonObject jsonObject = jsonElement.getAsJsonObject();

            // Verificar que la clave existe antes de acceder a ella
            if (jsonObject.has("conversion_result")) {
                return jsonObject.get("conversion_result").getAsString();
            } else {
                throw new RuntimeException("No se encontró 'conversion_result' en la respuesta.");
            }

        } catch (IOException | InterruptedException e) {
            System.out.println("Ocurrió un error: ");
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }
}
