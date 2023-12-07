package course.work.bobrovnik.graphics;

public class Point {
    public double x;
    public double y;
    public double f;


    public Point(double x, double y, double dx, double dy) {
        this.x = x;
        this.y = y;
        this.dx = dx;
        this.dy = dy;
    }

    public double dx;

    @Override
    public String toString() {
        return "Сведения о точкек {" +
                "x=" + x +
                ", y=" + y +

                '}';
    }

    public double dy;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;

    }
    public Point(double x, double y,double f) {
        this.x = x;
        this.y = y;
        this.f = f;

    }

    public Point() {

    }
}
