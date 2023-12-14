package course.work.bobrovnik.main;

import course.work.bobrovnik.Hook.CompleteHook;
import course.work.bobrovnik.Simplex.CompleteSimplex;
import course.work.bobrovnik.main.Task;
import org.springframework.web.servlet.ModelAndView;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CreatorComplete {
        public static String getConstraints(Task task) {
            List<String> constraints = new ArrayList<>();
            task.getListWrapper().getModels()
                    .stream()
                    .forEach(module -> constraints.add(MessageFormat.format(" {0}∙x1 + {1}∙x2 \\leq {2}", module.getKitA(), module.getKitB(), module.getTimeFund())));
            return"""
                <p>\\[
                \\left\\{
                \\begin{array}{l}
                """
                    + String.join(",\\\\" , constraints) + """
                 \\\\
                x1, x2 \\geq 0
                \\end{array}
                \\right.
                \\]</p>
                """ ;

        }

        public static String getFunction(Task task) {
            System.out.println("Отображение математической модели");
            return MessageFormat.format("<p> \\(f(x) = {0}∙x1 + {1}∙x2  \\rightarrow \\max\\)</p>", task.getPriceA(), task.getPriceB());
        }

        public static String getSimplexSolution(CompleteSimplex simplex) {
            System.out.println("Подготовка результатов симплекс метода");
            return MessageFormat.format("""
                С учетом ограничений и целевой функции,
                фирме необходимо отпустить средства на {0} мин. радиорекламы и на {1} мин. телерекламы, 
                чтобы получить оптимальное распределение финансовых средств в размере {2} денежных едениц
                """, simplex.getX1(), simplex.getX2(), simplex.getF());
        }

        public static ModelAndView getHookSolution(ModelAndView completeTask, CompleteHook task) {
            System.out.println("Подготовка результатов Хука-Дживса");
            completeTask.addObject("hook", MessageFormat.format("""
                С учетом ограничений и целевой функции,
                фирме необходимо отпустить средства на {0} мин. радиорекламы и на {1} мин. телерекламы, 
                чтобы получить оптимальное распределение финансовых средств в размере {2} денежных едениц
                """, task.getEndPoint().x, task.getEndPoint().y, task.getEndPoint().f));

            completeTask.addObject("z_contour", task.getListForZGraphic());
            completeTask.addObject("y_contour", task.getListForYGraphic());
            completeTask.addObject("x_contour", task.getListForXGraphic());

            completeTask.addObject("x_start", new double[]{task.getStartPoint().x});
            completeTask.addObject("y_start", new double[]{task.getStartPoint().y});


            completeTask.addObject("x_end", new double[]{task.getEndPoint().x});
            completeTask.addObject("y_end", new double[]{task.getEndPoint().y});

            completeTask.addObject("x_path", task.getPath().stream().map(p -> p.x).collect(Collectors.toList()));
            completeTask.addObject("y_path", task.getPath().stream().map(p -> p.y).collect(Collectors.toList()));


            completeTask.addObject("x_constraint", task.getConstrainPoints().stream().map(p -> p.x).collect(Collectors.toList()));
            completeTask.addObject("y_constraint", task.getConstrainPoints().stream().map(p -> p.y).collect(Collectors.toList()));
            return completeTask;
        }
        public static ModelAndView setTask(ModelAndView result, Task task){
            System.out.println("Отображние условий задачи");
            result.addObject("priceA",task.getPriceA());
            result.addObject("priceB",task.getPriceB());
            result.addObject("price",task.getPrice());
            result.addObject("radioSales",task.getRadioSales());
            result.addObject("tvSales",task.getTvSales());
            return result;
        }
}
