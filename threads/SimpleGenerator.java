package threads;

import functions.Function;
import functions.basic.Log;
import java.util.Random;

public class SimpleGenerator implements Runnable {
    private Task task;  //ссылка на объект задания
    
    //Конструктор (получает и сохраняет ссылку на Task)
    public SimpleGenerator(Task task) {
        this.task = task;
    }
    
    @Override
    public void run() {
        Random random = new Random();
        
        //выполняем цикл столько же раз, сколько заданий нужно сгенерировать
        for (int i = 0; i < task.getNumberTask(); i++) {
            //блок. объект task на время генерации задания
            synchronized (task) {
                //создаем лог. ф-ию с ранд. основанием (от 1 до 10)
                double base = 1 + random.nextDouble() * 9;
                Log logFunc = new Log(base);
                
                //генерируем случайные параметры
                double left = random.nextDouble() * 100;
                double right = 100 + random.nextDouble() * 100;
                double step = random.nextDouble();
                
                //устанавливаем параметры в объект задания
                task.setFunction(logFunc);
                task.setLeft(left);
                task.setRight(right);
                task.setStep(step);
                
                //выводим сообщение о сгенерированных параметрах
                System.out.printf("Source %.6f %.6f %.6f%n", left, right, step);
            }
            
            try {
                Thread.sleep(15);
            } catch (InterruptedException e) {
                break;
            }
        }
    }
}
