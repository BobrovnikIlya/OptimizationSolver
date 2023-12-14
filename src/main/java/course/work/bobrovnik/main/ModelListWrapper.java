package course.work.bobrovnik.main;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ModelListWrapper {
    private List<Model> models;
    public ModelListWrapper(int a, int b, int price, int b2, int b3) {
        models = new ArrayList<>();
        models.add(new Model(1, a, b, price));
        models.add(new Model(2,-1, b2,0));
        models.add(new Model(3,1,b3*(-1),0));
    }
}
