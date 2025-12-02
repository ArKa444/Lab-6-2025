package functions.meta;

import functions.Function;
//cумма ф-ий: f(x) = f1(x) + f2(x)
public class Sum implements Function {
    private Function f1;  //первая функция
    private Function f2;  //вторая функция
    
    //Конструктор получает две функции
    public Sum(Function f1, Function f2) {
        this.f1 = f1;
        this.f2 = f2;
    }
    
    //область определения - пересечение о.о. f1 и f2
    public double getLeftDomainBorder() {
        return Math.max(f1.getLeftDomainBorder(), f2.getLeftDomainBorder());
    }
    
    public double getRightDomainBorder() {
        return Math.min(f1.getRightDomainBorder(), f2.getRightDomainBorder());
    }
    
    //знач. суммы ф-ий в точке x
    public double getFunctionValue(double x) {
        //проверка, что x в о.о.
        if (x < getLeftDomainBorder() || x > getRightDomainBorder()) {
            return Double.NaN;
        }
        return f1.getFunctionValue(x) + f2.getFunctionValue(x);
    }
}
