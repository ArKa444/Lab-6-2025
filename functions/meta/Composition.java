package functions.meta;

import functions.Function;

//композиция ф-ий: f(x) = g(z(x))
public class Composition implements Function {
    private Function outer;  //внешняя ф-ия g
    private Function inner;  //внутренняя функция z
    
    public Composition(Function outer, Function inner) {
        this.outer = outer;
        this.inner = inner;
    }
    
    //о.о. совпадает с о.о. внутренней ф-ии
    public double getLeftDomainBorder() {
        return inner.getLeftDomainBorder();
    }
    
    public double getRightDomainBorder() {
        return inner.getRightDomainBorder();
    }
    
    public double getFunctionValue(double x) {
        if (x < getLeftDomainBorder() || x > getRightDomainBorder()) {
            return Double.NaN;
        }
        double innerValue = inner.getFunctionValue(x);//вычисл.хнач. внутренней ф-ии в точке х
        //проверка, что innerValue в о.о. внешней ф-ии
        if (innerValue < outer.getLeftDomainBorder() || 
            innerValue > outer.getRightDomainBorder()) {
            return Double.NaN;
        }
        return outer.getFunctionValue(innerValue);//знач. внешней ф-ии от результата внутреней
    }
}
//о.о - область определения