package com.example.lab4v3.controller;

import com.example.lab4v3.model.User;
import com.example.lab4v3.repository.PointRepository;
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
public class PointController {
    @Autowired
    PointRepository pointRepository;
    @GetMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getPoint(@AuthenticationPrincipal User user) {

        List<Point> points = pointRepository.findAll();

        return ResponseEntity.ok()
                .body(points);
    }

    @PostMapping(path = "/check")
    public ResponseEntity<?> checkPoint(@RequestBody Point point) {

        pointRepository.save(point);
        return ResponseEntity.ok()
                .body(point);
    }
}
