package threads;

import functions.Function;
import functions.basic.Log;
import java.util.Random;

public class Generator extends Thread {
    private Task task;           //ссылка на объект
    private Semaphore semaphore; //ссылка на объект семафора
    
    //Конструктор получает и сохраняет ссылки
    public Generator(Task task, Semaphore semaphore) {
        this.task = task;
        this.semaphore = semaphore;
    }
    
    @Override
    public void run() {
        Random random = new Random();
        
        try {
            //выполняем цикл столько раз, сколько заданий нужно сгенерировать
            for (int i = 0; i < task.getNumberTask(); i++) {
                //проверяемработает ли поток
                if (isInterrupted()) {
                    throw new InterruptedException("Generator прерван");
                }
                
                //начинаем операцию записи (ждем, если интегратор еще читает)
                semaphore.beginWrite();
                
                //создаем лог. ф-ию со случ. основанием(от 1 до 10)
                double base = 1 + random.nextDouble() * 9;
                Log logFunc = new Log(base);
                
                //генерируем случ. параметры
                double left = random.nextDouble() * 100;         
                double right = 100 + random.nextDouble() * 100;
                double step = random.nextDouble();
                
                //записываем параметры
                task.setFunction(logFunc);
                task.setLeft(left);
                task.setRight(right);
                task.setStep(step);
                
                //выводим сообщение о сгенерированных параметрах
                System.out.printf("Source %.6f %.6f %.6f%n", left, right, step);
                
                
                semaphore.endWrite();//заканчиваем
                
                
                try {
                    Thread.sleep(15);
                } catch (InterruptedException e) {
                    break;
                }
            }
        } catch (InterruptedException e) {
            //поток был прерван во время работы семафора
            System.out.println("Generator прерван: " + e.getMessage());
	    return;
        }
    }
}