package functions;

import threads.Task;
import threads.SimpleGenerator;
import threads.SimpleIntegrator;
import threads.Semaphore;
import threads.Generator;
import threads.Integrator;
import functions.basic.Log;
import functions.basic.Exp;
import java.util.Random;

public class Main {
    
    public static void main(String[] args) throws InterruptedException {
        //тест метода интегрирования
        System.out.println(" ЗАДАНИЕ 1");
        testIntegration();
        
        //тест последоват. версия
        System.out.println("\n ЗАДАНИЕ 2");
        nonThread();
        
        //тест простых многопоточ. версий
        System.out.println("\n ЗАДАНИЕ 3");
        simpleThreads();
        
        //тест многопоточная версия с семафорами
        System.out.println("\n ЗАДАНИЕ 4");
        complicatedThreads();
    }
    
    //задание 1: тест метода integrate,вычис. интеграл для экс. на отр. от 0 до 1
    //определяем шаг для точности 1e-7
    public static void testIntegration() {
        //создаем экспонен. ф-ию
        Function expFunc = new Exp();
        
        //значение интеграла e^x от 0 до 1
        double theoretical = Math.E - 1;
        
        //вычисление с шагом 0.001
        double step = 0.001;
        double result = Functions.integrate(expFunc, 0, 1, step);
        
        System.out.println("Интеграл e^x от 0 до 1:");
        System.out.println("При шаге " + step + ": " + result);
        System.out.println("Теоретическое: " + theoretical);
        System.out.println("Разница: " + Math.abs(result - theoretical));
        System.out.println();
        
        //поиск шага для точности 1e-7
        System.out.println("Поиск шага для точности 1e-7:");
        
        step = 1.0; //начинаем с шага 1
        int iterations = 0;
        double calculated;
        final double tochnost = 1e-7;
        
        do {
            calculated = Functions.integrate(expFunc, 0, 1, step);
            step /= 2.0; //уменьшаем шаг в 2 раза
            iterations++;
            
            //защита от бесконечного цикла
            if (iterations > 50) break;
            
        } while (Math.abs(calculated - theoretical) > tochnost);
        
        double validStep = step * 2; //последний валидный шаг
        
        System.out.println("Найден шаг: " + validStep);
        System.out.println("Значение: " + calculated);
        System.out.println("Отклонение: " + Math.abs(calculated - theoretical));
        System.out.println("Делений шага: " + iterations);
        
        //проверка с найденным шагом
        System.out.println("\nПроверка точности:");
        double finalResult = Functions.integrate(expFunc, 0, 1, validStep);
        double finalError = Math.abs(finalResult - theoretical);
        
        if (finalError <= tochnost) {
            System.out.println("Точность 1e-7 достигнута");
        } else {
            System.out.println(" Точность не достигнута");
        }
    }
    
    //задание 2: последовательная версия программы (без потоков)
    public static void nonThread() {
        //создаем объект задания
        Task task = new Task();
        
        //устанавливаем количество заданий (минимум 100)
        task.setNumberTask(100);
        
        //создаем генератор случайных чисел
        Random random = new Random();
        
        //выполняем задания в цикле
        for (int i = 0; i < task.getNumberTask(); i++) {
            // создаем логарифмическую ф-ию со случайным основанием от 1 до 10
            double base = 1 + random.nextDouble() * 9;
            Log logFunc = new Log(base);
            task.setFunction(logFunc);
            
            // левая граница: случайная от 0 до 100
            double left = random.nextDouble() * 100;
            task.setLeft(left);
            
            // правая граница: случайная от 100 до 200
            double right = 100 + random.nextDouble() * 100;
            task.setRight(right);
            
            // шаг дискретизации: случайный от 0 до 1
            double step = random.nextDouble();
            task.setStep(step);
            
            // выводим сообщение Source с параметрами задания
            System.out.println("Source " + left + " " + right + " " + step);
            
            try {
                // вычисляем значение интеграла
                double result = Functions.integrate(task.getFunction(), task.getLeft(), task.getRight(), task.getStep());
                
                // выводим сообщение Result с параметрами и результатом
                System.out.println("Result " + left + " " + right + " " + step + " " + result);
                
            } catch (IllegalArgumentException e) {
                //если возникла ошибка (например, область определения)
                System.out.println("Result " + left + " " + right + " " + step + " ERROR: " + e.getMessage());
            }
        }
    }
    
    //задание 3: простая многопоточная версия
    public static void simpleThreads() {
        //создаем объект задания
        Task task = new Task();
        
        //устанавливаем количество заданий (минимум 100)
        task.setNumberTask(100);
        
        //создаем потоки
        Thread generatorThread = new Thread(new SimpleGenerator(task));
        Thread integratorThread = new Thread(new SimpleIntegrator(task));
        
        //запускаем потоки
        generatorThread.start();
        integratorThread.start();
        
        //ждем завершения обоих потоков
        try {
            generatorThread.join();
            integratorThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        
    }
    
    //задание 4: улучшенная многопоточная версия с семафорами
    public static void complicatedThreads() throws InterruptedException {
        //создаем объект задания
        Task task = new Task();
        
        //устанавливаем количество заданий (минимум 100)
        task.setNumberTask(100);
        
        //создаем семафор (по примеру из лекции)
        Semaphore semaphore = new Semaphore();
        
        //создаем потоки
        Generator generator = new Generator(task, semaphore);
        Integrator integrator = new Integrator(task, semaphore);
        
        //запускаем потоки
        generator.start();
        integrator.start();
        
        //ждем 50 мс 
        Thread.sleep(50);
        
        //прерываем потоки
        generator.interrupt();
        integrator.interrupt();
        
        //ждем завершения потоков
        generator.join();
        integrator.join();
        
        
    }
}