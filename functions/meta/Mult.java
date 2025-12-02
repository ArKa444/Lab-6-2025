package functions.meta;

import functions.Function;

//f(x) = f1(x) * f2(x)
public class Mult implements Function {
    private Function f1;
    private Function f2;
    //Конструктор
    public Mult(Function f1, Function f2) {
	//сохраняем переданные ф-ии в поля класса
        this.f1 = f1;
        this.f2 = f2;
    }
    
    public double getLeftDomainBorder() {
        return Math.max(f1.getLeftDomainBorder(), f2.getLeftDomainBorder());
    }
    
    public double getRightDomainBorder() {
        return Math.min(f1.getRightDomainBorder(), f2.getRightDomainBorder());
    }
    //значение произведения функций в точке x
    public double getFunctionValue(double x) {
        if (x < getLeftDomainBorder() || x > getRightDomainBorder()) {
            return Double.NaN;//если x вне области определения
        }
        return f1.getFunctionValue(x) * f2.getFunctionValue(x);
    }
}
