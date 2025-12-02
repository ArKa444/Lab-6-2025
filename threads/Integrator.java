package threads;

import functions.Functions;

public class Integrator extends Thread {
    private Task task;
    private Semaphore semaphore;
    
    public Integrator(Task task, Semaphore semaphore) {
        this.task = task;
        this.semaphore = semaphore;
    }
    
    @Override
    public void run() {
        try {
            for (int i = 0; i < task.getNumberTask(); i++) {
                //проверка прерывания
                if (isInterrupted()) {
                    throw new InterruptedException("Integrator прерван");
                }
                
                //начинаем чтение (ждем, пока генератор запишет данные)
                semaphore.beginRead();
                
                //проверяем, что функция задана
                if (task.getFunction() != null) {
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
                
                //заканчиваем чтение (теперь можно писать)
                semaphore.endRead();
                
                
                try {
                    Thread.sleep(15);
                } catch (InterruptedException e) {
                    break;
                }
            }
        } catch (InterruptedException e) {
            System.out.println("Integrator прерван: " + e.getMessage());
	    return;
        }
    }
}