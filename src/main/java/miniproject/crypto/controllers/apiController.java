package miniproject.crypto.controllers;

import java.io.IOException;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;

import miniproject.crypto.services.apiService;
import miniproject.crypto.services.RepoService;

@RestController
@RequestMapping(path = "/api")
public class apiController {

    @Autowired
    private apiService apiSvc;

    @Autowired
    private RepoService repoSvc;

    @GetMapping(path = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getAll() {
        Set<String> setOfKeys = apiSvc.getBcList();
        JsonArrayBuilder arrOfKeys = Json.createArrayBuilder(setOfKeys);
        JsonObject responseObj = Json.createObjectBuilder()
                .add("data", arrOfKeys)
                .build();
        return new ResponseEntity<String>(responseObj.toString(), HttpStatus.OK);
    }

    @GetMapping(path = "/{symbol}/{fiat}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getSymbol(
            @PathVariable(value = "symbol") String symbol,
            @PathVariable(value = "fiat") String fiat) {
        JsonObject response = apiSvc.fetchApiSymbol(symbol, fiat);
        String responseStr = response.toString();
        return new ResponseEntity<String>(responseStr, HttpStatus.OK);
    
    }

    @GetMapping(path = "/signal", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getSignal(
            @RequestParam(name = "symbol") String symbol) {
        // String responseObj = apiSvc.getSignal(symbol).toString();
        String responseObj = repoSvc.getSentiment(symbol).toString();
        return new ResponseEntity<String>(responseObj, HttpStatus.OK);
    }

    @PostMapping(path = "/results")
    public void forwarder(
            @RequestBody MultiValueMap<String, String> form,
            HttpServletResponse response) throws IOException {
        String symbol = form.getFirst("symbol");
        String fiat = form.getFirst("fiat");
        response.sendRedirect("/api/%s/%s".formatted(symbol, fiat));
    }
}
