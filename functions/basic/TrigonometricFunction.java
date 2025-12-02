package functions.basic;

import functions.Function;

//базовый класс для всех триг. ф-ий
public abstract class TrigonometricFunction implements Function {
    
    //область определения: все действительные числа
    public double getLeftDomainBorder() {
        return Double.NEGATIVE_INFINITY;
    }
    
    public double getRightDomainBorder() {
        return Double.POSITIVE_INFINITY;
    }
    
    //базовый метод
    public abstract double getFunctionValue(double x);
}