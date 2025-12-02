package functions.meta;

import functions.Function;
//сдвиг ф-ии (+)
public class Shift implements Function {
    private Function function;//исходная ф-ия
    private double shiftX;//сдвиг по х
    private double shiftY;//сдвиг по у
    
    public Shift(Function function, double shiftX, double shiftY) {
        this.function = function;
        this.shiftX = shiftX;
        this.shiftY = shiftY;
    }
    
    //о.о. сдвигается по х
    public double getLeftDomainBorder() {
        return function.getLeftDomainBorder() + shiftX;
    }
    
    public double getRightDomainBorder() {
        return function.getRightDomainBorder() + shiftX;
    }
    
    public double getFunctionValue(double x) {
        if (x < getLeftDomainBorder() || x > getRightDomainBorder()) {
            return Double.NaN;
        }
        return shiftY + function.getFunctionValue(x - shiftX);
    }
}
