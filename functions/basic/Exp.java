package functions.basic;

import functions.Function;

//экспонента: f(x) = e^x
public class Exp implements Function {
    
    //область определения: все действительные числа(-оо,+оо)
    public double getLeftDomainBorder() {//возвращает левую границу о.о
        return Double.NEGATIVE_INFINITY;//-оо
    }
    
    public double getRightDomainBorder() {//возвращает правую границу о.о
        return Double.POSITIVE_INFINITY;//+оо
    }
    
    //вычисление значения ф-ии в точке x
    public double getFunctionValue(double x) {
        return Math.exp(x);  //e^x
    }
}
