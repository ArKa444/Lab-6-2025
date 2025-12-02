package threads;

import functions.Function;

public class Task {
    private Function function; //ссылка на объект интег. ф-ии
    private double left;       //левая граница
    private double right;      //правая граница
    private double step;       //шаг
    private int numberTask;    //поле, хранящее кол-во выполняемых заданий
    
    //ГЕТТЕРЫ
    //возвр. ссылку на объект ф-ии
    public Function getFunction() {
        return function;
    }
    //возвр. знач. левой границы
    public double getLeft() {
        return left;
    }
    //возвр. знач. правой границы
    public double getRight() {
        return right;
    }
    //возвр. знач. шага
    public double getStep() {
        return step;
    }
    //возвр. кол-во заданий, которые надо выполнить
    public int getNumberTask() {
        return numberTask;
    }
    
    //СЕТТЕРЫ
    
    public void setFunction(Function function) {
        this.function = function;
    }
    
    public void setLeft(double left) {
        this.left = left;
    }
    
    public void setRight(double right) {
        this.right = right;
    }
    
    public void setStep(double step) {
        this.step = step;
    }
    
    public void setNumberTask(int numberTask) {
        this.numberTask = numberTask;
    }
}