package course.work.bobrovnik.main;

import lombok.Data;

@Data
public class Task {
    private int priceA = 500; //стоимость радиорекламы
    private int priceB = 10000; //стоимость телерекламы
    private int price = 100000; //распологаемый бюджет

    //объем сбыта
    private int radioSales = 2;
    private int tvSales = 25;
    private ModelListWrapper listWrapper;
    public void setListWrapper(){
        System.out.println("Создание ограничений...");
        listWrapper = new ModelListWrapper(priceA, priceB, price, radioSales, tvSales);
    }
}
