package functions;

public interface Function{
    double getLeftDomainBorder();//возвр. левую границу

    double getRightDomainBorder();//вычисляет знач. ф-ии в заданной точке х

    double getFunctionValue(double x);//вычисляет знач. ф-ии в заданной точке х
}