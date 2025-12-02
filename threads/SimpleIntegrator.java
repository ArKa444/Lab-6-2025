package threads;

import functions.Functions;

public class SimpleIntegrator implements Runnable {
    private Task task;  //ссылка на объект задания
    
    //Конструктор (получает и сохраняет ссылку на Task)
    public SimpleIntegrator(Task task) {
        this.task = task;
    }
    
    @Override
    public void run() {
        //выполняем цикл столько же раз, сколько заданий нужно обраб.
        for (int i = 0; i < task.getNumberTask(); i++) {
            //блокируем объект task на время чтения параметров
            synchronized (task) {
                //проверка, что ф-ия уже задана (избавляемся от NullPointerException)
                if (task.getFunction() != null) { //есть ф-ия для вычисления
                    double left = task.getLeft();
                    double right = task.getRight();
                    double step = task.getStep();
                    
                    try {
                        double result = Functions.integrate(task.getFunction(), left, right, step);
                        System.out.printf("Result %.6f %.6f %.6f %.6f%n", left, right, step, result);
                        
                    } catch (IllegalArgumentException e) {
                        System.out.printf("Result %.6f %.6f %.6f ERROR: %s%n", left, right, step, e.getMessage());
                    }
                }
                
            }
            
            
            try {
                Thread.sleep(15);
            } catch (InterruptedException e) {
                break;
            }
        }
    }
}