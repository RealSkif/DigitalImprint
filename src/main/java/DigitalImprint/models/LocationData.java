package DigitalImprint.models;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.CompositeTypeRegistration;
import org.springframework.stereotype.Component;


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

    public static String locationDataToJson(LocationData locationData) {

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(locationData);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert LocationData to JSON", e);
        }
    }
}

