package com.example.lab4v3.controller;

import com.example.lab4v3.PointCredentialsRequest;
import com.example.lab4v3.model.User;
import com.example.lab4v3.repository.PointRepository;
import com.example.lab4v3.service.CheckPointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import com.example.lab4v3.model.Point;

import java.util.List;

@RestController
@RequestMapping("/api/point")
@CrossOrigin(origins = {"http://localhost:3000"}, maxAge = 4800, allowCredentials = "true")
public class PointController {
    @Autowired
    PointRepository pointRepository;
    @Autowired
    CheckPointService checkPointService;

    @GetMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getPoint(@AuthenticationPrincipal User user) {

        List<Point> points = pointRepository.findAll();


        return ResponseEntity.ok()
                .body(points);
    }

    @PostMapping(path = "/check")
    public ResponseEntity<?> checkPoint(@RequestBody PointCredentialsRequest point) {

        Point pointFromRepository = new Point(point);

        pointFromRepository.setInArea(checkPointService.checkInArea(pointFromRepository));
        pointRepository.save(pointFromRepository);
        return ResponseEntity.ok()
                .body(pointFromRepository);
    }
}
