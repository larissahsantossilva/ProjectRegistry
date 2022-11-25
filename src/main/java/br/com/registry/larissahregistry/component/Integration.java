package br.com.registry.larissahregistry.component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import com.fasterxml.jackson.databind.ObjectMapper;
import br.com.registry.larissahregistry.entity.dto.CertificateDto;
import com.fasterxml.jackson.core.type.TypeReference;
import org.json.JSONArray;
import org.springframework.stereotype.Component;

@Component
public class Integration {
    
    public List<CertificateDto> getCertificate() throws MalformedURLException, IOException {

        String url = "https://docketdesafiobackend.herokuapp.com/api/v1/certidoes";
        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();

        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");

        if (conn.getResponseCode() != 200) {
            System.out.println("Erro " + conn.getResponseCode());
        }

        BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream()), "UTF-8"));

        String output = "";
        String line;
        while ((line = br.readLine()) != null) {
            output += line;
        }

        conn.disconnect();

        JSONArray objArray = new JSONArray(output);
        ObjectMapper mapper = new ObjectMapper();
        List<CertificateDto> certificateList = mapper.readValue(objArray.toString(), new TypeReference<List<CertificateDto>>() {});

        return certificateList;

    }
    
}
