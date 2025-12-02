package functions;

import functions.meta.*;

//вне класса - нельзя создавать объекты
public class Functions {
    //Конструктор(запрещаем создание объектов)
    private Functions() {
        throw new AssertionError("Нельзя создавать объекты класса Functions");
    }
    //возвращает объект ф-ии, полученной из исходной сдвигом вдоль осей
    public static Function shift(Function f, double shiftX, double shiftY) {
        return new Shift(f, shiftX, shiftY);
    }
    //возвращает объект ф-ии, полученной из исходной масштабированием вдоль осей
    public static Function scale(Function f, double scaleX, double scaleY) {
        return new Scale(f, scaleX, scaleY);
    }
    //возвращает объект ф-ии, являющейся заданной степенью исходной
    public static Function power(Function f, double power) {
        return new Power(f, power);
    }
    //возвращает объект ф-ии, являющейся суммой двух исходных
    public static Function sum(Function f1, Function f2) {
        return new Sum(f1, f2);
    }
    //возвращает объект ф-ии, являющейся произведением двух исходных
    public static Function mult(Function f1, Function f2) {
        return new Mult(f1, f2);
    }
    //возвращает объект ф-ии, являющейся композицией двух исходных
    public static Function composition(Function f1, Function f2) {
        return new Composition(f1, f2);
    }
    //ВЫЧИСЛЕНИЕ ИНТЕГРАЛА Ф-ИИ(МЕТОДОМ ТРАПЕЦИИ)
    public static double integrate(Function function, double left, double right, double step) {
        //интервал интег. внутри о. о. ф-ии
        if (left < function.getLeftDomainBorder() || right > function.getRightDomainBorder()) {
            throw new IllegalArgumentException("Интервал интег. вне области определения");
        }
        
        //шаг должен быть > 0
        if (step <= 0) {
            throw new IllegalArgumentException("Шаг должен быть > 0");
        }
        
        //левая граница < правой
        if (left >= right) {
            throw new IllegalArgumentException("Левая граница должна быть меньше правой");
        }
        
        double integral = 0.0;    //собираем суммы площадей всех трапеций
        double startX = left;    //тек. позиция на оси X
        
        //проходим по всей области интег.
        while (startX < right) {
            //определяем конец тек. отрезка
            //Math.min(не выйдем за правую границу)
            double next = Math.min(startX + step, right);
            
            //знач. ф-ии на концах отрезка
            double f1 = function.getFunctionValue(startX);
            double f2 = function.getFunctionValue(next);
            
            //Площадь трапеции: (f1 + f2) * (next - startX)
	    //                   --------------------------
            //                               2
	    //f1, f2 - знач. ф-ии, (next - startX) - длина основания
            integral += (f1 + f2) * (next - startX) / 2;
            
            //к следующему отрезку
            startX = next;
        }
        
        return integral;
    }
}