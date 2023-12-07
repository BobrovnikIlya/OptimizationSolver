package course.work.bobrovnik.Hook;

import course.work.bobrovnik.graphics.Point;
import lombok.Data;

import java.util.List;

@Data
public class CompleteHook {
    Point startPoint;
    Point endPoint;
    List<List<Double>> listForZGraphic;
    List<Double> listForXGraphic;
    List<Double> listForYGraphic;
    List<Point> path;
    List<Point> constrainPoints;

}