package course.work.bobrovnik.Hook;

import course.work.bobrovnik.graphics.Point;
import course.work.bobrovnik.main.Module;
import course.work.bobrovnik.main.Task;

import java.util.*;

public class HookMethods {
    private static final double EPSILON = 0.0000001;
    private static double D_X = 1;
    private static double D_Y = 1;
    private static Point point1 = new Point();
    private static List<Double> x_test = new ArrayList<>();
    private static List<Double> y_test = new ArrayList<>();
    private static List<Point> result = new ArrayList<>();
    private static List<CompleteHook> tasks;
    private static CompleteHook completeTask;

    private static Task task;

    private static boolean constraintsSatisfied(Point point) {
        for (Module module : task.getListWrapper().getModules()) {
            if (module.getKitA() * point.x + module.getKitB() * point.y > module.getTimeFund()) {
                return false;
            }
        }
        return true;
    }
    private static double f(Point point) {
        return task.getPriceA() * point.x + task.getPriceB() * point.y; // Целевая функция
    }

    public static void createGraphic() {
        System.out.println("Создание графика Хука-Дживса");
        completeTask.listForXGraphic = new ArrayList<>();
        completeTask.listForYGraphic = new ArrayList<>();
        completeTask.listForZGraphic = new ArrayList<>();
        List<List<Point>> constrainPoints = new ArrayList<>();
        for (double i = 0; i < 40; i++) {
            completeTask.listForXGraphic.add(i);
            completeTask.listForYGraphic.add(i);
        }

        for (double i = 0; i < 40; i++) {
            List<Double> tmp = new ArrayList<>();
            List<Point> points = new ArrayList<>();
            for (double j = 0; j < 40; j++) {
                if (constraintsSatisfied(new Point(i, j)))
                    points.add(new Point(i, j));
                tmp.add(f(new Point(j, i)));
            }
            if (!points.isEmpty())
                constrainPoints.add(points);
            completeTask.listForZGraphic.add(tmp);
        }
        List<Point> path = new ArrayList<>();
        constrainPoints.get(0).stream().forEach(p -> path.add(p));
        constrainPoints.stream().forEach(p -> path.add(p.get(p.size()-1)));
        Collections.reverse(constrainPoints.get(constrainPoints.size()-1));
        constrainPoints.get(constrainPoints.size()-1).forEach(p -> path.add(p));
        Collections.reverse(constrainPoints.get(constrainPoints.size()-1));
        Collections.reverse(constrainPoints);
        constrainPoints.stream().forEach(p -> path.add(p.get(0)));
        completeTask.constrainPoints = path;
    }
    public static CompleteHook getResult(Task task) {
        tasks = new ArrayList<>();
        HookMethods.task = task;
        for (int i = 0; i < 1000; i++) {
            CompleteHook completeTask1 = new CompleteHook();
            completeTask1.path = new ArrayList<>();
            point1 = new Point();
            System.out.println("Выбор случайной начальной точки");
            D_X = 1;
            D_Y = 1;
            Random random = new Random();
            point1.dx = D_X;
            point1.dy = D_Y;
            do {
                point1.x = random.nextInt((15-10)+1) + 10; // x1 в диапазоне [10, 15]
                point1.y = random.nextInt((4-2)+1) + 2; // x2 в диапазоне [0, 8]
            } while (!constraintsSatisfied(point1));

            System.out.println("Точка выбрана. Начало решения");
            completeTask1.startPoint = point1;

            completeTask1.path.add(new Point(point1.x, point1.y, f(point1)));
            while (Math.abs(D_X) > EPSILON || Math.abs(D_Y) > EPSILON) {

                Point point2 = explorerSearch(point1);
                Point point3 = new Point();
                point2.f = f(point2);
                completeTask1.path.add(point2);
                point3.x = point1.x + 2 * (point2.x - point1.x);
                point3.y = point1.y + 2 * (point2.y - point1.y);
                point3.f = f(point3);
                if (constraintsSatisfied(point3))
                    completeTask1.path.add(point3);
                if (f(point2) > f(point1)) {
                    while (searchByPattern(point3).x != point3.x || searchByPattern(point3).y != point3.y) {
                        Point point4 = searchByPattern(point3);
                        point1 = point2;
                        point2 = point4;
                        point3 = new Point();
                        point3.x = point1.x + 2 * (point2.x - point1.x);
                        point3.y = point1.y + 2 * (point2.y - point1.y);
                        point3.f = f(point3);
                        if (constraintsSatisfied(point3))
                            completeTask1.path.add(point3);
                    }
                    point1 = point2;
                }
            }
            completeTask1.endPoint = point1;
            tasks.add(completeTask1);
        }

        completeTask = tasks.stream().max(Comparator.comparingDouble(x -> f(x.endPoint))).get();
        createGraphic();
        System.out.println("Экстремум найден : " + completeTask.endPoint +"\n Значение функции в точке: " + f(completeTask.endPoint));

        return completeTask;
    }

    public static Point searchByPattern(Point point3) {
        System.out.println("Запуск поиска по образцу ");
        List<Point> points = new ArrayList<>();
        points.add(new Point(point3.x + D_X, point3.y, D_X, D_Y));
        points.add(new Point(point3.x - D_X, point3.y, -D_X, D_Y));
        points.add(new Point(point3.x, point3.y - D_Y, D_X, -D_Y));
        points.add(new Point(point3.x, point3.y + D_Y, D_X, D_Y));
        points.add(new Point(point3.x + D_X, point3.y + D_Y, D_X, D_Y));
        points.add(new Point(point3.x + D_X, point3.y - D_Y, D_X, -D_Y));
        points.add(new Point(point3.x - D_X, point3.y - D_Y, -D_X, -D_Y));
        points.add(new Point(point3.x - D_X, point3.y + D_Y, -D_X, D_Y));

        Point point4 = points.stream().filter(p -> constraintsSatisfied(p)).max(Comparator.comparingDouble(HookMethods::f)).orElse(point3);
        if (f(point4) >= f(point3)) {
            System.out.println("Поиск по образцу: переход в точку -> " + point4 +"\n Значение функции в точке: " + f(point4));
            point3 = point4;
            D_X = point4.dx;
            D_Y = point4.dy;
        }

        points.clear();
        points.add(new Point(point3.x + D_X, point3.y, D_X, D_Y));
        points.add(new Point(point3.x - D_X, point3.y, -D_X, D_Y));
        points.add(new Point(point3.x, point3.y - D_Y, D_X, -D_Y));
        points.add(new Point(point3.x, point3.y + D_Y, D_X, D_Y));
        points.add(new Point(point3.x + D_X, point3.y + D_Y, D_X, D_Y));
        points.add(new Point(point3.x + D_X, point3.y - D_Y, D_X, -D_Y));
        points.add(new Point(point3.x - D_X, point3.y - D_Y, -D_X, -D_Y));
        points.add(new Point(point3.x - D_X, point3.y + D_Y, -D_X, D_Y));
        point4 = points.stream().filter(p -> constraintsSatisfied(p)).max(Comparator.comparingDouble(HookMethods::f)).orElse(point3);
        if (f(point4) >= f(point3)) {
            System.out.println("Поиск по образцу: переход в точку -> " + point4 +"\n Значение функции в точке: " + f(point4));
            point3 = point4;
            D_X = point4.dx;
            D_Y = point4.dy;
        }
        System.out.println("Была получена точка: "+ point4);
        return point3;
    }

    public static Point explorerSearch(Point point1) {
        System.out.println("Запуск исследующего поиска");
        List<Point> points = new ArrayList<>();
        points.add(new Point(point1.x + D_X, point1.y, D_X, D_Y));
        points.add(new Point(point1.x - D_X, point1.y, -D_X, D_Y));
        points.add(new Point(point1.x, point1.y - D_Y, D_X, -D_Y));
        points.add(new Point(point1.x, point1.y + D_Y, D_X, D_Y));
        points.add(new Point(point1.x + D_X, point1.y + D_Y, D_X, D_Y));
        points.add(new Point(point1.x + D_X, point1.y - D_Y, D_X, -D_Y));
        points.add(new Point(point1.x - D_X, point1.y - D_Y, -D_X, -D_Y));
        points.add(new Point(point1.x - D_X, point1.y + D_Y, -D_X, D_Y));
        Point point2 = points.stream().filter(p -> constraintsSatisfied(p)).max(Comparator.comparingDouble(HookMethods::f)).orElse(point1);
        if (f(point2) <= f(point1)) {
            D_X /= 2;
            D_Y /= 2;
            System.out.println("Исследующий поиск: точка не найдена, уменьшение шага");
        } else {
            points.stream().forEach(p -> {
                x_test.add(p.x);
                y_test.add(p.y);
            });
            System.out.println("Исследующий поиск: переход в точку -> "+ point2  +"\n Значение функции в точке: " + f(point2));
            point1 = point2;
            D_X = point2.dx;
            D_Y = point2.dy;
            Point point3 = new Point(point1.x + D_X, point1.y + D_Y, D_X, D_Y);
            while (f(point3) > f(point1) && constraintsSatisfied(point3)) {
                System.out.println("Исследующий поиск: переход в точку -> "+ point3  +"\n Значение функции в точке: " + f(point3));
                point1 = point3;
                point3 = new Point(point1.x + D_X, point1.y + D_Y, D_X, D_Y);
            }
        }

        point1.f = f(point1);
        System.out.println("Была получена точка: "+ point1);
        return point1;
    }
}
