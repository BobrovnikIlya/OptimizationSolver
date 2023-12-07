package course.work.bobrovnik.main;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ModuleListWrapper {
    private List<Module> modules;

    public ModuleListWrapper() {
        modules = new ArrayList<>();
        modules.add(new Module(1,500,10000,100000));
        modules.add(new Module(2,-1,2,0));
        modules.add(new Module(3,1,-25,0));
    }
    public ModuleListWrapper(int a, int b, int price, int b2, int b3) {
        modules = new ArrayList<>();
        modules.add(new Module(1, a, b, price));
        modules.add(new Module(2,-1, b2,0));
        modules.add(new Module(3,1,b3*(-1),0));
    }
}
