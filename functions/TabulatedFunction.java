package functions;

public interface TabulatedFunction extends Function, Cloneable {//шаблон
    int getPointsCount();//возвр. кол-во точек

    FunctionPoint getPoint(int index);//возвр. точку к индексу
    
    void setPoint(int index, FunctionPoint point) throws InappropriateFunctionPointException;//заменяет точку по индексу на новую

    double getPointX(int index);//возвр. Х коорд.точки

    void setPointX(int index, double x) throws InappropriateFunctionPointException;//изменяет только X коор. точки

    double getPointY(int index);//возвр. Y коорд.точки

    void setPointY(int index, double y);//изменяет только Y коор. точки

    void deletePoint(int index);//удаляет точку по индексу

    void addPoint(FunctionPoint point) throws InappropriateFunctionPointException;//добавляет новую точку в ф-ию

    Object clone(); //метод clone в интерфейс
}