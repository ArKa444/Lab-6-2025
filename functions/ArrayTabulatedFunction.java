package functions;

import java.io.Serializable;

public class ArrayTabulatedFunction implements TabulatedFunction, Serializable {
    private static final long serialVersionUID = 1L;
    private FunctionPoint[] points; //массив для хранения точек
    private int pointsCount;        //счетчик кол-ва точек (может быть меньше длины массива!)
    private static final double EPSILON = 1e-10; // для сравнения double чисел
    
    //Конструктор1(точки от leftX до rightX, y=0)
    public ArrayTabulatedFunction(double leftX, double rightX, int pointsCount) {
        //проверка параметров конструктора
        if (leftX >= rightX - EPSILON) {
            throw new IllegalArgumentException("Левая граница должна быть меньше правой");
        }
        if (pointsCount < 2) {
            throw new IllegalArgumentException("Количество точек должно быть не менее 2");
        }
        
        //создаем массив
        this.points = new FunctionPoint[pointsCount];
        this.pointsCount = pointsCount;
        
        //вычисляем расстояние между точками
        double move=(rightX - leftX) / (pointsCount - 1);
        
        //создаем точки с равными интервалами
        for (int i = 0;i < pointsCount;i++) {
            double x = leftX + i * move;
            points[i] = new FunctionPoint(x,0.0);
        }
    }

    //Конструктор2(точки от leftX до rightX, y из values)  
    public ArrayTabulatedFunction(double leftX, double rightX, double[] values) {
        //проверка параметров конструктора
        if (leftX >= rightX - EPSILON) {
            throw new IllegalArgumentException("Левая граница должна быть меньше правой");
        }
        if (values.length < 2) {
            throw new IllegalArgumentException("Количество точек должно быть не менее 2");
        }
        
        //создаем массив
        this.points = new FunctionPoint[values.length];
        this.pointsCount = values.length;
        
        //вычисляем расcтояние между точками
        double move = (rightX - leftX) / (pointsCount - 1);
        
        //создаем точки с заданными значениями y
        for (int i = 0; i < pointsCount;i++) {
            double x = leftX + i * move;
            points[i] = new FunctionPoint(x, values[i]);
        }
    }
    
    //Конструктор3(получает сразу все точки ф-ии в виде массива)
     public ArrayTabulatedFunction(FunctionPoint[] points) {
        //проверка кол-ва точек
        if (points.length < 2) {
            throw new IllegalArgumentException("Кол-во точек должно быть не меньше 2");
        }
        
        //проверка упорядоченности точек по X
        for (int i = 1; i < points.length; i++) {
            if (points[i].getX() - points[i-1].getX() <= -EPSILON) {
                throw new IllegalArgumentException("Точки должны быть упорядочены по возрастанию X");
            }
        }
        
        //создаем массив с запасом (+5)
        this.points = new FunctionPoint[points.length + 5];
        this.pointsCount = points.length;
        
        //создаем копии точек для инкапсуляции
        for (int i = 0; i < pointsCount; i++) {
            this.points[i] = new FunctionPoint(points[i]);
        }
    }

    //МЕТОДЫ
    //метод для левой границы области определения табулированной ф-ии
    public double getLeftDomainBorder() {
        return points[0].getX(); //первая точка определяет левую границу
    }
    //метод для правой границы области определения табулированной ф-ии
    public double getRightDomainBorder() {
        return points[pointsCount - 1].getX(); //последняя точка - правую границу
    }
    //метод для количества точек
    public int getPointsCount() {
        return pointsCount; 
    }
    
    //метод для точки по индексу(возвращаем копию)
    public FunctionPoint getPoint(int index) {
        //проверка индекса
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException("Индекс " + index + " вне границ [0, " + (pointsCount-1) + "]");
        }
        return new FunctionPoint(points[index]); //возвращаем копию для защиты данных
    }
    
    //замена точки
    public void setPoint(int index, FunctionPoint point) throws InappropriateFunctionPointException {
        //проверка индекса
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException("Индекс " + index + " вне границ [0, " + (pointsCount-1) + "]");
        }
        
        //создаем копию (защаем от изменений )
        FunctionPoint newPoint = new FunctionPoint(point);
        
        //проверка чтобы не сломался порядок по возрастанию х
        // если x новой точки <= предыдущей -> исключение
        if (index > 0 && newPoint.getX() - points[index-1].getX() <= EPSILON) {
            throw new InappropriateFunctionPointException("X новой точки должен быть больше предыдущего");
        }
        // если x новой точки >= следуующей -> исключение
        if (index < pointsCount-1 && points[index+1].getX() - newPoint.getX() <= EPSILON) {
            throw new InappropriateFunctionPointException("X новой точки должен быть меньше следующего");
        }
        
        points[index] = newPoint;
    }
    
    public double getPointX(int index) {
        //проверка индекса
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException("Индекс " + index + " вне границ [0, " + (pointsCount-1) + "]");
        }
        return points[index].getX();
    }
    
    public void setPointX(int index, double x) throws InappropriateFunctionPointException {
        //проверка индекса
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException("Индекс " + index + " вне границ [0, " + (pointsCount-1) + "]");
        }
        
        //проверка порядка по x
        if (index > 0 && x - points[index-1].getX() <= EPSILON) {
            throw new InappropriateFunctionPointException("Новый X должен быть больше предыдущего");
        }
        if (index < pointsCount-1 && points[index+1].getX() - x <= EPSILON) {
            throw new InappropriateFunctionPointException("Новый X должен быть меньше следующего");
        }
        
        points[index].setX(x);
    }
    
    public double getPointY(int index) {
        //проверка индекса
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException("Индекс " + index + " вне границ [0, " + (pointsCount-1) + "]");
        }
        return points[index].getY();
    }
    
    public void setPointY(int index, double y) {
        //проверка индекса
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException("Индекс " + index + " вне границ [0, " + (pointsCount-1) + "]");
        }
        points[index].setY(y);
    }
    
    //удалить точку
    public void deletePoint(int index) {
        //проверка индекса
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException("Индекс " + index + " вне границ [0, " + (pointsCount-1) + "]");
        }
        
        //если останется меньше 2 точек(нельзя удалять)
        if (pointsCount < 3) {
            throw new IllegalStateException("Нельзя удалить точку: должно остаться минимум 2 точки");
        }
        
        //сдвиг точки влево, перезаписывая удаляемую
        if (index < pointsCount - 1) {
            System.arraycopy(points, index + 1, points, index, pointsCount - index - 1);//берём все точки справа от удаляемой и перезаписываем ими саму удаляемую точку и следующие за ней
        }
        //очищаем последний элемент (теперь он дублируется)
        points[pointsCount-1] = null;
        pointsCount--; // уменьшаем счетчик
    }
    
    //добавить точку
    public void addPoint(FunctionPoint point) throws InappropriateFunctionPointException {
        //проверка на дубликат х - точки должны иметь уникальные х
        for (int i = 0; i < pointsCount; i++) {
            if (Math.abs(point.getX() - points[i].getX()) < EPSILON) {
                throw new InappropriateFunctionPointException("Точка с X=" + point.getX() + " уже есть");
            }
        }
        
        //создаем копию точки
        FunctionPoint newPoint = new FunctionPoint(point);
        
        //если массив полный - увеличиваем его(+5 ячеек)
        if (pointsCount == points.length) {
            FunctionPoint[] newArray = new FunctionPoint[points.length + 5];
            //копируем старые точки в новый массив
            System.arraycopy(points, 0, newArray, 0, pointsCount);
            points = newArray;
        }
        
        //ищем позицию для вставки (сохраняем порядок по возрастанию х)
        int position = pointsCount; //по умолчанию - в конец
        for (int i = 0; i < pointsCount; i++) {
            if (newPoint.getX() - points[i].getX() < EPSILON) {
                position = i; //нашли куда вставлять
                break;
            }
        }
        
        //сдвигаем точки вправо чтобы освободить место для новой точки
        if (position < pointsCount) {
            System.arraycopy(points, position, points, position + 1, pointsCount - position);
        }
        //вставляем новую точку
        points[position] = newPoint;
        pointsCount++; //увеличиваем счетчик
    }
    
    //метод для вычисления значения ф-ии в точке x 
    public double getFunctionValue(double x) {
        //если x за границами-возвращаем NaN
        if (x - getLeftDomainBorder() < -EPSILON || x - getRightDomainBorder() > EPSILON) {
            return Double.NaN;
        }
        
        //поиск отрезка где находится x
        for (int i = 0; i < pointsCount - 1;i++) {
            double x1 = points[i].getX();
            double x2 = points[i + 1].getX();
            double y1 = points[i].getY();
            double y2 = points[i + 1].getY();
            
            //если x совпадает с x1 - возв. соответствующий y1
            if (Math.abs(x - x1) < EPSILON) {
                return y1;
            }
            
            //если x совпадает с x2 - возв. соответствующий y2
            if (Math.abs(x - x2) < EPSILON) {
                return y2;
            }
            
            //если x попал в текущий отрезок
            if (x - x1 >= -EPSILON && x - x2 <= EPSILON) {
                //лин. интерполяция
                return y1 + (y2 - y1) * (x - x1) / (x2 - x1);
            }
        }
        return Double.NaN;
    }

    //метод возвращает строковое пр. ф-ии {(x1; y1), (x2; y2), ...}
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        for (int i = 0; i < pointsCount; i++) {
            sb.append(points[i].toString());
            if (i < pointsCount - 1) sb.append(", ");
        }
        sb.append("}");
        return sb.toString(); 
    }
    //метод сравнивает тек. ф-ию с другой ф-ей на равенство точек(оптимизирован через прямой доступ к массиву)
    @Override
    public boolean equals(Object o) {
    	if (this == o) return true;//тот же объект
    	if (!(o instanceof TabulatedFunction)) return false;
    
    //оптимизация(прямой доступ к массиву)
    	if (o instanceof ArrayTabulatedFunction) {
            ArrayTabulatedFunction other = (ArrayTabulatedFunction) o;
            if (this.pointsCount != other.pointsCount) return false;//разное кол-во точек
        
        //прямой доступ к массивам points
            for (int i = 0; i < pointsCount; i++) {
            	if (!this.points[i].equals(other.points[i])) return false;
        }
            return true;
    } 
        //реал. для других TabulatedFunction
        else {
        TabulatedFunction other = (TabulatedFunction) o;
            if (this.getPointsCount() != other.getPointsCount()) return false;//проверка кол-ва точек
        
            for (int i = 0; i < pointsCount; i++) { //сравнение через getPoint()
                if (!this.getPoint(i).equals(other.getPoint(i))) return false; //возвр. копию
        }
            return true;
    }
}
    
    
    //метод возвращает значение хэш-кода для объекта точки
    @Override
    public int hashCode() {
        int result = pointsCount;// начало - кол-во точек
        for (int i = 0; i < pointsCount; i++) {
            result ^= points[i].hashCode(); //комб. хэш черех XOR
        }
        return result;
    }
    //метод возвращает объект-копию для объекта точки
    @Override
    public Object clone() { 
        FunctionPoint[] clonedPoints = new FunctionPoint[pointsCount]; //создаем новый массив
        for (int i = 0; i < pointsCount; i++) {
            clonedPoints[i] = (FunctionPoint) points[i].clone(); //клонируем каждую точку
        }
        return new ArrayTabulatedFunction(clonedPoints); //создаем новую ф-ию с клон. точками
    }
}