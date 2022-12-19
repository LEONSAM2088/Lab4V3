package com.example.lab4v3.controller;

import com.example.lab4v3.PointCredentialsRequest;
import com.example.lab4v3.model.Point;
import com.example.lab4v3.repository.PointRepository;
import com.example.lab4v3.service.CheckPointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/point")
public class PointController {
    @Autowired
    PointRepository pointRepository;
    @Autowired
    CheckPointService checkPointService;

    @GetMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true", allowedHeaders = "*")
    public ResponseEntity<?> getPoint() {

        List<Point> points = pointRepository.findAll();


        return ResponseEntity.ok()
                .body(points);
    }
    @DeleteMapping(path = "all")
    @CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true", allowedHeaders = "*")
    public ResponseEntity<?> deleteAllPoints() {


        pointRepository.deleteAll();

        return ResponseEntity.ok().build();
    }
    @PostMapping(path = "/check")
    @CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true", allowedHeaders = "*")
    public ResponseEntity<?> checkPoint(@RequestBody PointCredentialsRequest point) {

        Point pointFromRepository = new Point(point);

        pointFromRepository.setInArea(checkPointService.checkInArea(pointFromRepository));
        pointRepository.save(pointFromRepository);
        return ResponseEntity.ok()
                .body(pointFromRepository);
    }
}
