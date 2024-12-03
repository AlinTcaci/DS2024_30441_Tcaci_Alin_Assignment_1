package ds2024.monitoring.controller;

import ds2024.monitoring.dtos.LimitsDTO;
import ds2024.monitoring.service.LimitsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/limits")
public class LimitsController {

    private final LimitsService limitsService;

    @Autowired
    public LimitsController(LimitsService limitsService) {
        this.limitsService = limitsService;
    }

    @GetMapping("/getAllLimits")
    public ResponseEntity<List<LimitsDTO>> getAllLimits() {
        List<LimitsDTO> limits = limitsService.getAllLimits();
        return new ResponseEntity<>(limits, HttpStatus.OK);
    }

}
