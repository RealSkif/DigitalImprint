package DigitalImprint.models;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.mail.internet.MimeMessage;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "locationData")
public class LocationData {
    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    @Column(name = "latitude")
    private String latitude;
    @Column(name = "longitude")
    private String longitude;
    @Column(name = "googleMapsLink")
    private String googleMapsLink;
    @Column(name = "yandexMapsLink")
    private String yandexMapsLink;
    @Column(name = "appleMapsLink")
    private String appleMapsLink;
    @Column(name = "extremum")
    private String extremum;
    @Column(name = "ipAddress")
    private String ipAddress;
    @Column(name = "os")
    private String os;
    @Column(name = "browserName")
    private String browserName;
    @Column(name = "browserVersionString")
    private String browserVersion;
    @Column(name = "requestTime")
    private String requestTime;

}

