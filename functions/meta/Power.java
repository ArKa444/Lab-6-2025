package functions.meta;

import functions.Function;
//f(x) = [g(x)]^power
public class Power implements Function {
    private Function baseFunction;  //базовая ф-ия
    private double power;           //степень
    
    public Power(Function baseFunction, double power) {
        this.baseFunction = baseFunction;
        this.power = power;
    }
    
    //о.о. совпадает с о.о. базовой ф-ии
    public double getLeftDomainBorder() {
        return baseFunction.getLeftDomainBorder();
    }
    
    public double getRightDomainBorder() {
        return baseFunction.getRightDomainBorder();
    }
    
    public double getFunctionValue(double x) {
        if (x < getLeftDomainBorder() || x > getRightDomainBorder()) {
            return Double.NaN;
        }
        double baseValue = baseFunction.getFunctionValue(x);//вычисляем значение базовой ф-ии в точке x
        return Math.pow(baseValue, power);//возводим полученное знач. в заданную степень
    }
}
//о.о.- область определения
