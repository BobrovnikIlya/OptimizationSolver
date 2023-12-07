package course.work.bobrovnik.main;

import lombok.Data;

@Data
public class Task {
/*    private int priceA = 500;
    private int priceB = 10000;
    private int price = 100000;
    private int radioSales = 2;
    private int tvSales = 25;*/
    private int priceA = 0;
    private int priceB = 0;
    private int price = 0;
    private int radioSales = 0;
    private int tvSales = 0;
    private ModuleListWrapper listWrapper;
    public void setListWrapper(){
        System.out.println("Создание ограничений...");
        listWrapper = new ModuleListWrapper(priceA, priceB, price, radioSales, tvSales);
    }
}
