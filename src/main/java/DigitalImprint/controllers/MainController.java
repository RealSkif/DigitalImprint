package DigitalImprint.controllers;

import DigitalImprint.models.LocationData;
import DigitalImprint.service.LocationDataService;
import eu.bitwalker.useragentutils.UserAgent;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;

@Controller
@RequestMapping("/index")
public class MainController {

    private final LocationDataService locationDataService;

    @Autowired
    public MainController(LocationDataService locationDataService) {
        this.locationDataService = locationDataService;
    }

    @GetMapping
    public String home() {
        return "index";
    }

    @PostMapping
    public String locate(HttpServletRequest request,
                         @RequestBody LocationData locationData) throws MessagingException {
        UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
        locationData.setIpAddress(request.getRemoteAddr());
        locationData.setOs(String.valueOf(userAgent.getOperatingSystem()));
        locationData.setBrowserName(String.valueOf(userAgent.getBrowser()));
        locationData.setBrowserVersion(String.valueOf(userAgent.getBrowserVersion()));
        locationData.setRequestTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                .format(System.currentTimeMillis()));
        locationData.setGoogleMapsLink("https://www.google.com/maps/search/?api=1&query="
                + locationData.getLatitude() + "," + locationData.getLongitude());
        locationData.setYandexMapsLink("https://yandex.ru/maps/?pt="
                + locationData.getLongitude() + "%2C" + locationData.getLatitude() + "&z=15&l=map");
        locationData.setAppleMapsLink("http://maps.apple.com/?q="
                + locationData.getLatitude() + "," + locationData.getLongitude());
        locationData.setExtremum("https://gis.extremum.org/#z=16&c="
                + locationData.getLatitude() + "," + locationData.getLongitude() + "&l=bS/gHyb&p="
                + locationData.getLatitude() + "," + locationData.getLongitude());
        if (locationData.getLatitude() == null) {
            locationData.setLatitude("Геолокация отключена на стороне пользователя");
            locationData.setLongitude("Геолокация отключена на стороне пользователя");
        }
        String json = locationDataService.locationDataToJson(locationData);
        locationDataService.sendEmailWithJson(json);
        return "index";
    }
  /* После того, как будет создана бд, этот метод раскомментировать, одноименный выше удалить
    @PostMapping
    public String locate(HttpServletRequest request,
                         @RequestBody LocationData locationData) throws MessagingException {
        UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
        locationData.setIpAddress(request.getRemoteAddr());
        locationData.setOs(String.valueOf(userAgent.getOperatingSystem()));
        locationData.setBrowserName(String.valueOf(userAgent.getBrowser()));
        locationData.setBrowserVersion(String.valueOf(userAgent.getBrowserVersion()));
        locationData.setRequestTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                .format(System.currentTimeMillis()));
        locationData.setGoogleMapsLink("https://www.google.com/maps/search/?api=1&query="
                + locationData.getLatitude() + "," + locationData.getLongitude());
        locationData.setYandexMapsLink("https://yandex.ru/maps/?pt="
                + locationData.getLongitude() + "%2C" + locationData.getLatitude() + "&z=15&l=map");
        locationData.setAppleMapsLink("http://maps.apple.com/?q="
                + locationData.getLatitude() + "," + locationData.getLongitude());
        if (locationData.getLatitude() == null) {
            locationData.setLatitude("Геолокация отключена на стороне пользователя");
            locationData.setLongitude("Геолокация отключена на стороне пользователя");
        }
        locationDataService.save(locationData);
        return "home";
    }*/


}

