package miniproject.crypto.services;

import java.io.Reader;
import java.io.StringReader;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.json.JsonValue;

@Service
public class apiService {

    @Value("${API_KEY}")
    private String apiKey;

    private final String app = "restfulApiPractice";

    private ResponseEntity<String> fetch(String url) {
        RestTemplate template = new RestTemplate();
        RequestEntity<Void> req = RequestEntity.get(url).build();

        try {
            ResponseEntity<String> res = template.exchange(req, String.class);
            return res;
        } catch (Exception e) {
            System.err.print(e);
            return null;
        }
    }

    public JsonObject fetchApiSymbol(String symbol, String fiat) {
        String apiStr = "https://min-api.cryptocompare.com/data/price";
        String url = UriComponentsBuilder.fromUriString(apiStr)
                .queryParam("fsym", symbol)
                .queryParam("tsyms", fiat)
                .queryParam("api_key", apiKey)
                .queryParam("extraParams", app)
                .toUriString();

        // Fetch API
        ResponseEntity<String> res = fetch(url);

        // Get response:
        String payload = res.getBody();

        // Read response:
        Reader reader = new StringReader(payload);
        JsonReader jr = Json.createReader(reader);

        // Manipulate response:
        JsonObject responseObj = jr.readObject();
        return responseObj;
    }

    public Set<String> getBcList() {
        String apiStr = "https://min-api.cryptocompare.com/data/blockchain/list";
        String url = UriComponentsBuilder.fromUriString(apiStr)
                .queryParam("api_key", apiKey)
                .queryParam("extraParams", app)
                .toUriString();

        // Fetch API
        ResponseEntity<String> res = fetch(url);

        // Get response:
        String payload = res.getBody();
        Reader reader = new StringReader(payload);
        JsonReader jr = Json.createReader(reader);

        // Manipulate response:
        JsonObject response = jr.readObject();
        JsonObject data = response.getJsonObject("Data");
        Set<String> setOfKeys = data.keySet();
        return setOfKeys;
    }

    public JsonObject getSignal(String symbol) {
        String apiStr = "https://min-api.cryptocompare.com/data/tradingsignals/intotheblock/latest";
        String url = UriComponentsBuilder.fromUriString(apiStr)
                .queryParam("fsym", symbol)
                .queryParam("api_key", apiKey)
                .queryParam("extraParams", app)
                .toUriString();

        ResponseEntity<String> res = fetch(url);
        String payload = res.getBody();
        Reader reader = new StringReader(payload);
        JsonReader jr = Json.createReader(reader);

        JsonObject response = jr.readObject();
        JsonObject data = response.getJsonObject("Data");
        JsonValue dataSymbol = data.get("symbol");
        JsonObject inOutVar = data.getJsonObject("inOutVar");
        JsonValue sentiment = inOutVar.get("sentiment");

        JsonObject responseObj = Json.createObjectBuilder()
                .add("symbol", dataSymbol)
                .add("sentiment", sentiment)
                .build();
        return responseObj;
    }

}
