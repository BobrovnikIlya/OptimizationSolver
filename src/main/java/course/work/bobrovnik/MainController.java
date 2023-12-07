package course.work.bobrovnik;

import course.work.bobrovnik.Hook.HookMethods;
import course.work.bobrovnik.Simplex.CompleteSimplex;
import course.work.bobrovnik.Simplex.SimplexMethod;
import course.work.bobrovnik.Hook.CompleteHook;
import course.work.bobrovnik.graphics.CreatorComplete;
import course.work.bobrovnik.main.ModuleListWrapper;
import course.work.bobrovnik.main.Task;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class MainController {
    @GetMapping("/Home")
    public ModelAndView getStart() {
        System.out.println("Инициализация начальной страницы...");
        ModelAndView start = new ModelAndView("Home");
        Task task = new Task();
        start.addObject("task", task);
        System.out.println("Отображение начальной страницы...");
        return start;
    }
    @PostMapping("/redirect")
    public ModelAndView redirect(@RequestParam String option, @ModelAttribute Task task, HttpSession session) {
        System.out.println("Произведение расчетов...");
        task.setListWrapper();
        if (option.equals("Simplex")) {
            System.out.println("Запуск Симплекс-метода...");
            CompleteSimplex completeSimplex = SimplexMethod.getCompleteTask(task);
            System.out.println("Сохранение результатов...");
            session.setAttribute("task",task);
            session.setAttribute("simplex",completeSimplex);
            System.out.println("Результаты сохранены. Переход к отображению результатов");
            return new ModelAndView("redirect:/Simplex");
        } else if (option.equals("Hook")) {
            System.out.println("Запуск метода Хука-Дживса...");
            CompleteHook completeHook =  HookMethods.getResult(task);
            System.out.println("Сохранение результатов...");
            session.setAttribute("task",task);
            session.setAttribute("hook",completeHook);
            System.out.println("Результаты сохранены. Переход к отображению результатов");
            return new ModelAndView("redirect:/Hook");
        }
        System.out.println("Не выбран метод...");
        return new ModelAndView("Home");
    }
    @GetMapping("/Simplex")
    public ModelAndView simplexPage(HttpSession session) {
        System.out.println("Выгрузка результатов");
        System.out.println("Выгрузка условий задачи");
        Task task = (Task) session.getAttribute("task");
        System.out.println("Выгрузка ответа симплекс-метода");
        CompleteSimplex simplex = (CompleteSimplex) session.getAttribute("simplex");
        ModelAndView simpl = new ModelAndView("Simplex");
        simpl.addObject("function", CreatorComplete.getFunction(task));
        simpl.addObject("constraints", CreatorComplete.getConstraints(task));
        simpl.addObject("simplex", CreatorComplete.getSimplexSolution(simplex));
        simpl = CreatorComplete.setTask(simpl,task);
        System.out.println("Отображение результатов");
        return simpl;
    }
    @GetMapping("/Hook")
    public ModelAndView hookPage(HttpSession session) {
        System.out.println("Выгрузка результатов");
        System.out.println("Выгрузка условий задачи");
        Task task = (Task) session.getAttribute("task");
        System.out.println("Выгрузка ответа Хука-Дживса");
        CompleteHook completehook = (CompleteHook) session.getAttribute("hook");
        ModelAndView hook = new ModelAndView("Hook");
        hook.addObject("function", CreatorComplete.getFunction(task));
        hook.addObject("constraints", CreatorComplete.getConstraints(task));
        hook = CreatorComplete.getHookSolution(hook,completehook);
        hook = CreatorComplete.setTask(hook,task);
        System.out.println("Отображение результатов");
        return hook;
    }
    @GetMapping("/About")
    public ModelAndView aboutPage() {
        ModelAndView about = new ModelAndView("About");
        return about;
    }
}
