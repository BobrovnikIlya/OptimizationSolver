package course.work.bobrovnik.Simplex;

import course.work.bobrovnik.main.Task;

import java.text.DecimalFormat;
import java.util.*;

public class SimplexMethod {
    public SimplexMethod(double[][] arrayA, double[] arrayB, double[] c) {
        this.arrayA = arrayA;
        this.arrayB = arrayB;
        C = c;
    }
    String template = "###.##";//Шаблон форматирования
    DecimalFormat decForm = new DecimalFormat(template); //Форматирвоание чисел
    double arrayA[][]; //Коэффициенты ограничений
    double arrayB[]; //Базисные переменные

    int count = 0; //Кол-во итераций
    double C[]; //Коэффициенты целевой функции
    List<Double> del = new ArrayList<>(); //Дельты
    List<Integer> formulaC = new ArrayList<>(); //Индексы базисных переменных
    static CompleteSimplex completeTask; //Решение
    public void createFormula() {
        formulaC.clear();
        for (int i = 0; i < C.length; i++) {
            if (C[i] == 0.0) {
                formulaC.add(i);
            }
        }
    }
    //Возвращение коэффициентов целевой функции
    public Object[] getArrC() {
        List<String> row = new ArrayList<>();
        row.add("C");
        Arrays.stream(C).forEach(c -> row.add(String.valueOf(c)));
        return row.toArray();
    }
    //получение массива базисов
    public Object[] BasisArray() {
        List<String> row = new ArrayList<>();
        row.clear();
        row.add("Базис");
        for (int i = 0; i < C.length; i++) {
            row.add("x" + (i + 1));
        }
        row.add("b");
        return row.toArray();
    }

    public Object[] getDelArray() {
        List<String> row = new ArrayList<>();
        row.add("Δ");
        for (int i = 0; i < del.size(); i++) {
            row.add(String.valueOf(del.get(i)));
        }

        return row.toArray();
    }
    //Вывод таблицы
    public void print() {
        System.out.println("Итерация " + count);
        System.out.println("//////////////////////////////////////////////////////////////");
        Object[][] table = new Object[arrayA.length + 3][];
        table[0] = getArrC();
        table[1] = BasisArray();
        for (int i = 0; i < arrayA.length; i++) {
            List<String> row = new ArrayList<>();
            row.add("x" + (formulaC.get(i) + 1));
            for (int j = 0; j < arrayA[0].length; j++) {
                row.add(String.valueOf(decForm.format(arrayA[i][j])));
            }
            row.add(String.valueOf(arrayB[i]));
            table[2 + i] = row.toArray();
        }
        table[table.length - 1] = getDelArray();
        for (Object[] r : table) {
            for (Object cell : r) {
                System.out.printf("%-8s", cell);
            }
            System.out.println();
        }
        System.out.println("//////////////////////////////////////////////////////////////");
    }
    //Вычисление дельт
    public void searchDel() {
        del.clear();
        for (int i = 0; i < arrayA[0].length; i++) {
            double del = 0.0;
            for (int j = 0; j < arrayA.length; j++) {
                del += C[formulaC.get(j)] * arrayA[j][i];
            }
            del -= C[i];
            this.del.add(del);
        }
    }
    //Пересройка талицы
    public void rebuildTable() {
        int index_column = del.indexOf(del.stream().min(Double::compare).get());
        List<Double> Q = new ArrayList<>();
        for (int i = 0; i < arrayA.length; i++) {
            double tmp = arrayA[i][index_column] > 0 ? arrayB[i] / arrayA[i][index_column] : -1;
            Q.add(tmp);
        }
        int index_row = 0;
        double min = Double.MAX_VALUE;
        for (int i = 0; i < Q.size(); i++) {
            if (Q.get(i) != -1 && Q.get(i) < min) {
                min = Q.get(i);
                index_row = i;

            }
        }

        double test = arrayA[index_row][index_column];
        for (int i = 0; i < arrayA[0].length; i++) {
            arrayA[index_row][i] = arrayA[index_row][i] / test;

        }
        arrayB[index_row] = arrayB[index_row] / test;

        for (int i = 0; i < arrayA.length; i++) {
            double k = arrayA[i][index_column];
            if (i != index_row) {
                for (int j = 0; j < arrayA[0].length; j++) {

                    arrayA[i][j] = arrayA[i][j] - (arrayA[index_row][j] * k);
                }
                arrayB[i] = arrayB[i] - (arrayB[index_row] * k);
            }


        }
        formulaC.set(index_row, index_column);

    }
    //Расчеты задачи
    public void solveTask() {
        this.searchDel();
        print();
        if (del.stream().min(Double::compare).get() < 0) {
            System.out.println("План не оптимален. Перерасчет");

            this.rebuildTable();
            count++;
            this.solveTask();
        } else {
            Map<String, Integer> map = new HashMap<>();
            for (int i = 0; i < formulaC.size(); i++) {
                if (C[formulaC.get(i)] != 0.0)
                    map.put("x" + (formulaC.get(i) + 1), (int) arrayB[i]);
            }
            int n = map.size();

            for (int i = n + 1; i < Arrays.stream(C).filter(x -> x > 0.0).toArray().length + 1; i++) {
                map.put("x" + i, 0);
            }

            Map<String, Double> map1 = new LinkedHashMap<>();
            map.entrySet().stream().sorted(Map.Entry.comparingByKey()).forEach(x -> map1.put(x.getKey(), Double.valueOf(x.getValue())));
            map1.entrySet().stream().forEach(System.out::println);
            Double F = 0.0;
            for (int i = 0; i < map1.size(); i++) {
                String s = "x" + (i + 1);
                F += Arrays.stream(C).filter(x -> x > 0.0).toArray()[i] * map.get(s);
            }
            System.out.println("F=" + F);
            completeTask.setX1(map.get("x1") != null ? map.get("x1").intValue() : 0);
            completeTask.setX2(map.get("x2") != null ? map.get("x2").intValue() : 0);
            completeTask.setF(F.intValue());
        }
    }
    //Возращает решение задачи
    public static CompleteSimplex getCompleteTask(Task task) {
        double[][] arrX = new double[task.getListWrapper().getModels().size()][task.getListWrapper().getModels().size() + 2];
        int count = 2;
        for (int i = 0; i < task.getListWrapper().getModels().size(); i++) {
            arrX[i][0] = task.getListWrapper().getModels().get(i).getKitA();
            arrX[i][1] = task.getListWrapper().getModels().get(i).getKitB();
            for (int j = 2; j < task.getListWrapper().getModels().size() + 2; j++) {
                arrX[i][j] = 0;
            }
            arrX[i][count] = 1;
            count++;
        }
        double[] arrayB = task.getListWrapper().getModels().stream().mapToDouble(x -> x.getTimeFund()).toArray();
        double[] C = new double[task.getListWrapper().getModels().size() + 2];
        for (int i = 0; i < C.length; i++) {
            C[i] = 0;
        }
        C[0] = task.getPriceA();
        C[1] = task.getPriceB();
        completeTask = new CompleteSimplex();
        SimplexMethod simplex = new SimplexMethod(arrX, arrayB, C);
        simplex.createFormula();
        simplex.solveTask();
        return completeTask;
    }
}
