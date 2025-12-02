package functions.basic;

import functions.Function;
//логарифм: f(x) = log а (x)
public class Log implements Function {
    private double base;  //основание лог.
    
    //Конструктор получает основание лог.
    public Log(double base) {
        if (base <= 0 || Math.abs(base - 1.0) < 1e-10) {
            throw new IllegalArgumentException("Основание логарифма должно быть > 0 и =! 1");
        }
        this.base = base;
    }
    
    //область определения: x > 0
    public double getLeftDomainBorder() {
        return 0.0;  //лог. определен только для x > 0
    }
    
    public double getRightDomainBorder() {
        return Double.POSITIVE_INFINITY;//+оо
    }
    
    //вычисление логарифма: logₐ(x) = ln(x) / ln(a)
    public double getFunctionValue(double x) {
        if (x <= 0) {
            return Double.NaN;  //для x ≤ 0
        }
	//формула: log а (x) = ln(x) / ln(a)
        return Math.log(x) / Math.log(base);
    }
}
