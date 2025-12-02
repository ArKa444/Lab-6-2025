package functions.meta;

import functions.Function;
//масштабирование функции: f(x) = scaleY * g(scaleX * x)
public class Scale implements Function {
    private Function function;  //исходная функция
    private double scaleX;      //коэфф. масштабирования по x
    private double scaleY;      //коэфф. масштабирования по y
    
    public Scale(Function function, double scaleX, double scaleY) {
        this.function = function;
        this.scaleX = scaleX;
        this.scaleY = scaleY;
    }
    
    //о.о. масштабируется по х
    public double getLeftDomainBorder() {
        if (scaleX > 0) {
            return function.getLeftDomainBorder() / scaleX;
        } else if (scaleX < 0) {
            return function.getRightDomainBorder() / scaleX;
        } else {
            return Double.NaN; //scaleX = 0 - ф-ия не определена
        }
    }
    
    public double getRightDomainBorder() {
        if (scaleX > 0) {
            return function.getRightDomainBorder() / scaleX;
        } else if (scaleX < 0) {
            return function.getLeftDomainBorder() / scaleX;
        } else {
            return Double.NaN;
        }
    }
    
    public double getFunctionValue(double x) {
        if (x < getLeftDomainBorder() || x > getRightDomainBorder()) {
            return Double.NaN;
        }
        return scaleY * function.getFunctionValue(scaleX * x);
    }
}