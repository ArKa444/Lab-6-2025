package functions;

import java.io.Serializable;

public class FunctionPoint implements Serializable {
    private static final long serialVersionUID = 1L;
    private double x;  //коорд. x точки
    private double y;  //коорд. y точки
    private static final double EPSILON = 1e-10;

    //конструктор1(создаем точку с заданными коорд. x и y)
    public FunctionPoint(double x, double y) {
        this.x = x;    //сохраняем переданный x
        this.y = y;    //сохраняем переданный y
    }

    //конструктор2(создаем копию сущ. точки)
    public FunctionPoint(FunctionPoint point) {
        this.x = point.x;  //берем x из исходной точки
        this.y = point.y;  //берем y из исходной точки
    }

    //конструктор3(создаем точку в начале координат (0,0))
    public FunctionPoint() {
        this.x = 0.0;
        this.y = 0.0;
    }

    //ГЕТТЕРЫ(методы для получения значений)
    //получаем значение x
    public double getX() {
        return x;  //возвращаем коорд. x
    }

    //получаем значение y
    public double getY() {
        return y;  //возвращаем коорд. y
    }

    //СЕТТЕРЫ(методы для установки значений)

    //устанавливаем новое значение x
    public void setX(double x) {
        this.x = x;  //меняем коорд. x
    }

    //устанавливаем новое значение y
    public void setY(double y) {
        this.y = y;  //меняем коорд. y
    }

    //метод возвращает текстовое предст. точки в формате (x; y)
    @Override
    public String toString() {
        return "(" + x + "; " + y + ")";
    }
	//метод сравнивает тек. точку с другой точкой на р-во коорд.
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FunctionPoint)) return false;
        FunctionPoint that = (FunctionPoint) o;
        return Math.abs(this.x - that.x) < EPSILON && 
               Math.abs(this.y - that.y) < EPSILON;
    }
	//метод преобразует double в long биты и комбинирует их через XOR
    @Override
    public int hashCode() {
        long xBits = Double.doubleToLongBits(x);
        long yBits = Double.doubleToLongBits(y);
        return (int)(xBits ^ (xBits >>> 32)) ^ (int)(yBits ^ (yBits >>> 32));
    }
	//метод создает и возвращает копию текущей точки
    @Override
    public Object clone() {
        return new FunctionPoint(this.x, this.y);
    }
}