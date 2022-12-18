package com.example.lab4v3.service;

import com.example.lab4v3.model.Point;
import org.springframework.stereotype.Service;

@Service
public class CheckPointService {
    public boolean checkInArea(Point point) {
        float X = point.getX();
        float Y = point.getY();
        float R = point.getR();

        // circle
        if(X>=0 && Y>=0){
            return X * X + Y * Y <= R * R;
        }
        // square
        if(Y<=0 && X>=0) {
            return Y>=-R && X<=R/2;
        }
        // triangle
        if(X<=0 && Y <=0)
            return Y >= X / 2 - R / 2;
        return false;
    }
}
