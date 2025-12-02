package threads;

public class Semaphore {
    private boolean canWrite = true;  //true - можно писать, false - можно читать
    //начало чтения
    public synchronized void beginRead() throws InterruptedException {
        while (canWrite) {
            wait();  //ждем, пока генератор не запишет данные
        }
    }
    
    
    //заканчиваем операцию чтения (для интегратора)
    //разрешаем запись
    public synchronized void endRead() {
        canWrite = true;
        notifyAll();  //будим все ожидающие потоки
    }
    
    //начинаем операцию записи (для генератора)
    //можно писать (canWrite = true)
     
    public synchronized void beginWrite() throws InterruptedException {
        while (!canWrite) {
            wait();  //ждем чтения данных
        }
    }
    
    //заканчиваем  записи (генератора) и разрешаем чтение
   public synchronized void endWrite() {
        canWrite = false;
        notifyAll();  //будим все ожидающие потоки
    }
}