package DigitalImprint.service;

import DigitalImprint.models.LocationData;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import DigitalImprint.repository.LocationDataRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LocationDataService {
    private final JavaMailSender mailSender;
    private final LocationDataRepository locationDataRepository;
    @Value("${email.recipient}")
    private String recipient;
    @Autowired
    public LocationDataService(JavaMailSender mailSender, LocationDataRepository locationDataRepository) {
        this.mailSender = mailSender;
        this.locationDataRepository = locationDataRepository;
    }

    public void save(LocationData locationData) {
        locationDataRepository.save(locationData);
    }

    public List<LocationData> findAll() {
        return locationDataRepository.findAll();
    }

    public LocationData findById(long id) {
        Optional<LocationData> foundLocationData = locationDataRepository.findById(id);
        return foundLocationData.orElse(null);
    }

    public String locationDataToJson(LocationData locationData) {

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(locationData);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert LocationData to JSON", e);
        }
    }


    public void sendEmailWithJson(String json) throws jakarta.mail.MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(recipient);
        helper.setSubject("Location Data JSON");
        helper.setText(json);
        mailSender.send(message);
    }
}
