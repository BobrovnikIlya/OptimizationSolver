package course.work.bobrovnik.Hook;

import course.work.bobrovnik.graphics.Point;
import course.work.bobrovnik.main.Model;
import course.work.bobrovnik.main.Task;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class HookMethods {
    private static final double EPSILON = 0.0000001; //Погрешность
    private static double STEP_X = 1; //Шаг по x
    private static double STEP_Y = 1; //Шаг по y
    private static Point point = new Point(); //Исследуюмая точка
    private static List<Double> x_test = new ArrayList<>();
    private static List<Double> y_test = new ArrayList<>();
    private static List<Point> res = new ArrayList<>(); //Результат
    private static List<CompleteHook> decision; //Список возможных решений
    private static CompleteHook completeTask; //Решенная задача

    private static Task task; //Решаемая задача

    //Обработка ограничений
    private static boolean constraints(Point point) {
        for (Model model : task.getListWrapper().getModels()) {
            if (model.getKitA() * point.x + model.getKitB() * point.y > model.getTimeFund()) {
                return false;
            }
        }
        return true;
    }
    // Целевая функция
    private static double f(Point point) {
        return task.getPriceA() * point.x + task.getPriceB() * point.y;
    }
    //Создание графика
    public static void createGraph() {
        System.out.println("Создание графика Хука-Дживса");
        completeTask.listForXGraphic = IntStream.range(0, 40).asDoubleStream().boxed().collect(Collectors.toList());
        completeTask.listForYGraphic = IntStream.range(0, 40).asDoubleStream().boxed().collect(Collectors.toList());
        completeTask.listForZGraphic = new ArrayList<>();
        List<List<Point>> constrainPoints = new ArrayList<>();

        //Определение размера графика
        for (double i = 0; i < 40; i++) {
            List<Double> tmp = new ArrayList<>();
            List<Point> points = new ArrayList<>();
            for (double j = 0; j < 40; j++) {
                if (constraints(new Point(i, j)))
                    points.add(new Point(i, j));
                tmp.add(f(new Point(j, i)));
            }
            if (!points.isEmpty())
                constrainPoints.add(points);
            completeTask.listForZGraphic.add(tmp);
        }
        //Отображение пути на графике
        List<Point> path = new ArrayList<>();
        constrainPoints.get(0).forEach(path::add);
        constrainPoints.forEach(p -> path.add(p.get(p.size()-1)));
        Collections.reverse(constrainPoints.get(constrainPoints.size()-1));
        constrainPoints.get(constrainPoints.size()-1).forEach(path::add);
        Collections.reverse(constrainPoints.get(constrainPoints.size()-1));
        Collections.reverse(constrainPoints);
        constrainPoints.forEach(p -> path.add(p.get(0)));
        completeTask.constrainPoints = path;
    }

        public static CompleteHook solveResult(Task task) {
        decision = new ArrayList<>();
        HookMethods.task = task;
        for (int i = 0; i < 1000; i++) {
            CompleteHook completeTask1 = new CompleteHook();
            completeTask1.path = new ArrayList<>();
            point = new Point();
            System.out.println("Выбор случайной начальной точки");
            STEP_X = 1;
            STEP_Y = 1;
            Random random = new Random();
            point.dx = STEP_X;
            point.dy = STEP_Y;
            do {
                point.x = random.nextInt((15-10)+1) + 10;
                point.y = random.nextInt((4-2)+1) + 2;
            } while (!constraints(point));

            System.out.println("Начало решения");
            completeTask1.startPoint = point;

            completeTask1.path.add(new Point(point.x, point.y, f(point)));
            while (Math.abs(STEP_X) > EPSILON || Math.abs(STEP_Y) > EPSILON) {

                Point point2 = explorerSearch(point);
                Point point3 = new Point();
                point2.f = f(point2);
                completeTask1.path.add(point2);
                point3.x = point.x + 2 * (point2.x - point.x);
                point3.y = point.y + 2 * (point2.y - point.y);
                point3.f = f(point3);
                if (constraints(point3))
                    completeTask1.path.add(point3);
                if (f(point2) > f(point)) {
                    while (searchByTemplate(point3).x != point3.x || searchByTemplate(point3).y != point3.y) {
                        Point point4 = searchByTemplate(point3);
                        point = point2;
                        point2 = point4;
                        point3 = new Point();
                        point3.x = point.x + 2 * (point2.x - point.x);
                        point3.y = point.y + 2 * (point2.y - point.y);
                        point3.f = f(point3);
                        if (constraints(point3))
                            completeTask1.path.add(point3);
                    }
                    point = point2;
                }
            }
            completeTask1.endPoint = point;
            decision.add(completeTask1);
        }

        completeTask = decision.stream().max(Comparator.comparingDouble(x -> f(x.endPoint))).get();
        createGraph();
        System.out.println("Экстремум: " + completeTask.endPoint +"\n Значение функции в точке: " + f(completeTask.endPoint));

        return completeTask;
    }
    //Поиск по шаблону
    public static Point searchByTemplate(Point point3) {
        System.out.println("Запуск поиска по шаблону ");
        List<Point> points = new ArrayList<>();
        points.add(new Point(point3.x + STEP_X, point3.y, STEP_X, STEP_Y));
        points.add(new Point(point3.x - STEP_X, point3.y, -STEP_X, STEP_Y));
        points.add(new Point(point3.x, point3.y - STEP_Y, STEP_X, -STEP_Y));
        points.add(new Point(point3.x, point3.y + STEP_Y, STEP_X, STEP_Y));
        points.add(new Point(point3.x + STEP_X, point3.y + STEP_Y, STEP_X, STEP_Y));
        points.add(new Point(point3.x + STEP_X, point3.y - STEP_Y, STEP_X, -STEP_Y));
        points.add(new Point(point3.x - STEP_X, point3.y - STEP_Y, -STEP_X, -STEP_Y));
        points.add(new Point(point3.x - STEP_X, point3.y + STEP_Y, -STEP_X, STEP_Y));

        Point point4 = points.stream().filter(p -> constraints(p)).max(Comparator.comparingDouble(HookMethods::f)).orElse(point3);
        if (f(point4) >= f(point3)) {
            System.out.println("Поиск по шаблону: точка: " + point4 +"\n Функция: " + f(point4));
            point3 = point4;
            STEP_X = point4.dx;
            STEP_Y = point4.dy;
        }
        points.clear();
        points.add(new Point(point3.x + STEP_X, point3.y, STEP_X, STEP_Y));
        points.add(new Point(point3.x - STEP_X, point3.y, -STEP_X, STEP_Y));
        points.add(new Point(point3.x, point3.y - STEP_Y, STEP_X, -STEP_Y));
        points.add(new Point(point3.x, point3.y + STEP_Y, STEP_X, STEP_Y));
        points.add(new Point(point3.x + STEP_X, point3.y + STEP_Y, STEP_X, STEP_Y));
        points.add(new Point(point3.x + STEP_X, point3.y - STEP_Y, STEP_X, -STEP_Y));
        points.add(new Point(point3.x - STEP_X, point3.y - STEP_Y, -STEP_X, -STEP_Y));
        points.add(new Point(point3.x - STEP_X, point3.y + STEP_Y, -STEP_X, STEP_Y));
        point4 = points.stream().filter(p -> constraints(p)).max(Comparator.comparingDouble(HookMethods::f)).orElse(point3);
        if (f(point4) >= f(point3)) {
            System.out.println("Поиск по шаблону: точка: " + point4 +"\n Функция: " + f(point4));
            point3 = point4;
            STEP_X = point4.dx;
            STEP_Y = point4.dy;
        }
        System.out.println("Получена точка: "+ point4);
        return point3;
    }

    public static Point explorerSearch(Point point1) {
        System.out.println("Запуск исследующего поиска");
        List<Point> points = new ArrayList<>();
        points.add(new Point(point1.x + STEP_X, point1.y, STEP_X, STEP_Y));
        points.add(new Point(point1.x - STEP_X, point1.y, -STEP_X, STEP_Y));
        points.add(new Point(point1.x, point1.y - STEP_Y, STEP_X, -STEP_Y));
        points.add(new Point(point1.x, point1.y + STEP_Y, STEP_X, STEP_Y));
        points.add(new Point(point1.x + STEP_X, point1.y + STEP_Y, STEP_X, STEP_Y));
        points.add(new Point(point1.x + STEP_X, point1.y - STEP_Y, STEP_X, -STEP_Y));
        points.add(new Point(point1.x - STEP_X, point1.y - STEP_Y, -STEP_X, -STEP_Y));
        points.add(new Point(point1.x - STEP_X, point1.y + STEP_Y, -STEP_X, STEP_Y));
        Point point2 = points.stream().filter(p -> constraints(p)).max(Comparator.comparingDouble(HookMethods::f)).orElse(point1);
        if (f(point2) <= f(point1)) {
            STEP_X /= 2;
            STEP_Y /= 2;
            System.out.println("Исследующий поиск: точка не найдена, уменьшение шага");
        } else {
            points.stream().forEach(p -> {
                x_test.add(p.x);
                y_test.add(p.y);
            });
            System.out.println("Исследующий поиск: точка: "+ point2  +"\n Функция: " + f(point2));
            point1 = point2;
            STEP_X = point2.dx;
            STEP_Y = point2.dy;
            Point point3 = new Point(point1.x + STEP_X, point1.y + STEP_Y, STEP_X, STEP_Y);
            while (f(point3) > f(point1) && constraints(point3)) {
                System.out.println("Исследующий поиск: точка: "+ point3  +"\n Функция: " + f(point3));
                point1 = point3;
                point3 = new Point(point1.x + STEP_X, point1.y + STEP_Y, STEP_X, STEP_Y);
            }
        }

        point1.f = f(point1);
        System.out.println("Результирующая точка: "+ point1);
        return point1;
    }
}
