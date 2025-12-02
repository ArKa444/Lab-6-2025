package functions;

import java.io.*;

public class LinkedListTabulatedFunction implements TabulatedFunction, Externalizable {
    private static final long serialVersionUID = 1L;

    //внутренний класс для узла списка
    private static class FunctionNode implements Serializable {
        private static final long serialVersionUID = 1L;
        FunctionPoint point;  //данные точки
        FunctionNode prev;    //ссылка на предыдущий узел
        FunctionNode next;    //ссылка на следующий узел

        //конструктор узла
        FunctionNode(FunctionPoint point, FunctionNode prev, FunctionNode next) {
            this.point = point;
            this.prev = prev;
            this.next = next;
        }

        //конструктор для сериализации
        FunctionNode() {
        }
    }
    //поля класса
    private FunctionNode head;       //голова списка (не содержит данных)
    private int pointsCount;         //кол-во точек
    private static final double EPSILON = 1e-10;

    //вспомогательные поля для оптимизации доступа
    private FunctionNode lastNode;   //последний используемый узел
    private int lastIndex;           //индекс последнего используемого узла

    //Конструктор для Externalizable
    public LinkedListTabulatedFunction() {
    }

    //Конструктор1
    public LinkedListTabulatedFunction(double leftX, double rightX, int pointsCount) {
        //проверка параметров конструктора
        if (leftX >= rightX - EPSILON) {
            throw new IllegalArgumentException("Левая граница должна быть меньше правой");
        }
        if (pointsCount < 2) {
            throw new IllegalArgumentException("Количество точек должно быть не менее 2");
        }

        initList(); //создаем пустой список
        double move = (rightX - leftX) / (pointsCount - 1);

        //создаем точки
        for (int i = 0; i < pointsCount; i++) {
            double x = leftX + i * move;
            //addNodeToTail() создает новый узел и добавляет в конец
            addNodeToTail().point = new FunctionPoint(x, 0.0);
        }
    }

    //Конструктор2(с заданными У)
    public LinkedListTabulatedFunction(double leftX, double rightX, double[] values) {
        //проверка параметров конструктора
        if (leftX >= rightX - EPSILON) {
            throw new IllegalArgumentException("Левая граница должна быть меньше правой");
        }
        if (values.length < 2) {
            throw new IllegalArgumentException("Количество точек должно быть не менее 2");
        }

        initList(); //создаем пустой список
        double move = (rightX - leftX) / (values.length - 1);

        //создаем точки с заданными значениями y
        for (int i = 0; i < values.length; i++) {
            double x = leftX + i * move;
            addNodeToTail().point = new FunctionPoint(x, values[i]);
        }
    }

    //Конструктор3 с массивом точек
    public LinkedListTabulatedFunction(FunctionPoint[] points) {
        if (points.length < 2) {
            throw new IllegalArgumentException("Количество точек должно быть не менее 2");
        }

        //проверка упорядоченности точек
        for (int i = 1; i < points.length; i++) {
            if (points[i].getX() - points[i-1].getX() <= -EPSILON) {
                throw new IllegalArgumentException("Точки должны быть упорядочены по возрастанию X");
            }
        }

        initList();

        //добавляем точки в список
        for (FunctionPoint point : points) {
            addNodeToTail().point = new FunctionPoint(point);
        }
    }

    //создание и инициализация пустого списка
    private void initList() {
        head = new FunctionNode(null, null, null);
        head.prev = head;  //циклический список
        head.next = head;
        pointsCount = 0;
        lastNode = head;
        lastIndex = -1;
    }

    //получить узел по индексу (оптимизированный доступ)
    private FunctionNode getNodeByIndex(int index) {
        //проверка индекса
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException("Индекс " + index + " вне границ [0, " + (pointsCount-1) + "]");
        }

        //проверяем можно ли использовать lastNode для оптимизации
        if (lastIndex != -1) {
            int diff = Math.abs(index - lastIndex);
            //если ближе идти от lastNode чем от головы
            if (diff < index && diff < pointsCount - index) {
                FunctionNode current = lastNode;
                if (index > lastIndex) {
                    //двигаемся вперед
                    for (int i = lastIndex; i < index; i++) {
                        current = current.next;
                    }
                } else {
                    //двигаемся назад
                    for (int i = lastIndex; i > index; i--) {
                        current = current.prev;
                    }
                }
                lastNode = current; //запоминаем для следующих вызовов
                lastIndex = index;
                return current;
            }
        }

        //иначе идем от головы
        FunctionNode current = head.next;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }
        lastNode = current;
        lastIndex = index;
        return current;
    }

    //добавить узел в конец списка
    private FunctionNode addNodeToTail() {
        FunctionNode newNode = new FunctionNode(null, head.prev, head);//создаем новый узел
        head.prev.next = newNode;//посл узел ссылается на новый
        head.prev = newNode;//голова ссылается на новый
        pointsCount++;
        lastNode = newNode;
        //запоминаем новый узел и индекс
        lastIndex = pointsCount - 1;
        return newNode;
    }

    //добавить узел по индексу
    private FunctionNode addNodeByIndex(int index) {
        //проверка индекса
        if (index < 0 || index > pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException("Индекс " + index + " вне границ [0, " + pointsCount + "]");
        }
        //находим след. узел котопый будет след. после него
        FunctionNode nextNode = (index == pointsCount) ? head : getNodeByIndex(index);
        FunctionNode prevNode = nextNode.prev;//узел который будет предыдущим
        //создаем новый узел
        FunctionNode newNode = new FunctionNode(null, prevNode, nextNode);
        //перекидываем ссылки
        prevNode.next = newNode;
        nextNode.prev = newNode;
        pointsCount++;

        //обновляем lastNode если нужно
        if (index <= lastIndex) {
            lastIndex++;
        }

        return newNode;
    }

    //удалить узел по индексу
    private FunctionNode deleteNodeByIndex(int index) {
        //проверка индекса
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException("Индекс " + index + " вне границ [0, " + (pointsCount-1) + "]");
        }
        //находим удаляемый узел
        FunctionNode nodeToDelete = getNodeByIndex(index);
        //отделяем узел из цепочки
        nodeToDelete.prev.next = nodeToDelete.next;
        nodeToDelete.next.prev = nodeToDelete.prev;
        pointsCount--;

        //обновляем lastNode если нужно
        if (index == lastIndex) {
            lastNode = head;//удаляем узел который запомнили
            lastIndex = -1;
        } else if (index < lastIndex) {
            lastIndex--;//сдвиг индекс
        }

        return nodeToDelete;
    }

    //реализация методов Externalizable
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeInt(pointsCount);

        FunctionNode current = head.next;
        for (int i = 0; i < pointsCount; i++) {
            out.writeDouble(current.point.getX());
            out.writeDouble(current.point.getY());
            current = current.next;
        }
    }

    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        pointsCount = in.readInt();

        if (pointsCount < 2) {
            throw new IOException("Некорректное количество точек: " + pointsCount);
        }

        initList();

        //читаем и создаем точки
        for (int i = 0; i < pointsCount; i++) {
            double x = in.readDouble();
            double y = in.readDouble();
            addNodeToTail().point = new FunctionPoint(x, y);
        }
    }

    //МЕТОДЫ таб. ф-ии
    public double getLeftDomainBorder() {
        if (pointsCount == 0) return Double.NaN;
        return head.next.point.getX();//х первого узла после головы
    }

    public double getRightDomainBorder() {
        if (pointsCount == 0) return Double.NaN;
        return head.prev.point.getX();//х узла перед головой
    }

    public int getPointsCount() {//возвращает текущее количество точек в списке
        return pointsCount;
    }

    public FunctionPoint getPoint(int index) {//возвращаем копию точки из узла
        return new FunctionPoint(getNodeByIndex(index).point);
    }

    public void setPoint(int index, FunctionPoint point) throws InappropriateFunctionPointException {
        //находим узел который будем изменять
        FunctionNode node = getNodeByIndex(index);
        FunctionPoint newPoint = new FunctionPoint(point);//создаем копию

        //проверка порядка по x
        if (index > 0 && newPoint.getX() - node.prev.point.getX() <= EPSILON) {//проверка левого
            throw new InappropriateFunctionPointException("X новой точки должен быть больше предыдущего");
        }
        if (index < pointsCount-1 && node.next.point.getX() - newPoint.getX() <= EPSILON) {//проверка правого
            throw new InappropriateFunctionPointException("X новой точки должен быть меньше следующего");
        }

        node.point = newPoint;//заменяем точку в узле
    }

    public double getPointX(int index) {//возв.х коорд.
        return getNodeByIndex(index).point.getX();
    }

    public void setPointX(int index, double x) throws InappropriateFunctionPointException {
        FunctionNode node = getNodeByIndex(index);

        //проверка порядка по x
        if (index > 0 && x - node.prev.point.getX() <= EPSILON) {
            throw new InappropriateFunctionPointException("Новый X должен быть больше предыдущего");
        }
        if (index < pointsCount-1 && node.next.point.getX() - x <= EPSILON) {
            throw new InappropriateFunctionPointException("Новый X должен быть меньше следующего");
        }

        node.point.setX(x); //изменяем только х координату, у остается прежним
    }

    public double getPointY(int index) { //возвр.У коорд.
        return getNodeByIndex(index).point.getY();
    }

    public void setPointY(int index, double y) {//изменяем У
        getNodeByIndex(index).point.setY(y);
    }

    public void deletePoint(int index) {
        //проверка минимального количества точек
        if (pointsCount < 3) {
            throw new IllegalStateException("Нельзя удалить точку: должно остаться минимум 2 точки");
        }
        deleteNodeByIndex(index);
    }

    public void addPoint(FunctionPoint point) throws InappropriateFunctionPointException {
        //проверка на дубликат X
        FunctionNode current = head.next;
        for (int i = 0; i < pointsCount; i++) {
            if (Math.abs(point.getX() - current.point.getX()) < EPSILON) {
                throw new InappropriateFunctionPointException("Точка с X=" + point.getX() + " уже есть");
            }
            current = current.next;
        }

        //ищем позицию для вставки (сохраняем порядок по воз. x)
        int position = 0;
        current = head.next;
        while (position < pointsCount && current.point.getX() < point.getX()) {
            current = current.next;
            position++;
        }
        //создаем новый узел на найденной позиции
        FunctionNode newNode = addNodeByIndex(position);
        newNode.point = new FunctionPoint(point);//копия точки
    }

    public double getFunctionValue(double x) {//если список пуст или ошибка границы
        if (pointsCount == 0 || x < getLeftDomainBorder() - EPSILON || x > getRightDomainBorder() + EPSILON) {
            return Double.NaN;
        }

        //ищем отрезок где x
        FunctionNode current = head.next;//первая точка
        for (int i = 0; i < pointsCount - 1; i++) {
            double x1 = current.point.getX();
            double x2 = current.next.point.getX();
            double y1 = current.point.getY();
            double y2 = current.next.point.getY();

            //если x совпадает с x1 (в пределах эпсилон)
            if (Math.abs(x - x1) < EPSILON) {
                return y1; //возвращ соотв. У
            }

            //если x совпадает с x2 (в пределах эпсилон)
            if (Math.abs(x - x2) < EPSILON) {
                return y2; //возвращ соотв. У
            }

            //если x в текущем отрезке
            if (x >= x1 - EPSILON && x <= x2 + EPSILON) {
                //лин. интерполяция
                return y1 + (y2 - y1) * (x - x1) / (x2 - x1);
            }
            current = current.next;
        }

        return Double.NaN;
    }

    //метод возвращает строковое представление функции в формате {(x1; y1), (x2; y2), ...}
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        FunctionNode current = head.next;
        for (int i = 0; i < pointsCount; i++) {
            sb.append(current.point.toString()); //используем toString точки
            if (i < pointsCount - 1) sb.append(", "); //разделитель между точками
            current = current.next;
        }
        sb.append("}");
        return sb.toString();
    }

    //метод сравнивает текущую функцию с другой функцией на равенство точек (оптимизирован через прямой доступ к узлам)
    @Override
    public boolean equals(Object o) {
        //если тот же объект в памяти
        if (this == o) return true;
        if (!(o instanceof TabulatedFunction)) return false;//проверка типа
        
        //оптимизация(если сравниваем с LinkedListTabulatedFunction)
        if (o instanceof LinkedListTabulatedFunction) {
            LinkedListTabulatedFunction other = (LinkedListTabulatedFunction) o;
            
            //проверка:разное кол-во точек -> не равны
            if (this.pointsCount != other.pointsCount) return false;
            
            //оптим. сравнение: прямой обход узлов обоих списков
            FunctionNode currentThis = this.head.next;    //начинаем с первого узла этого списка
            FunctionNode currentOther = other.head.next;  //начинаем с первого узла другого списка
            
            for (int i = 0; i < pointsCount; i++) {
                //сравниваем точки из узлов
                if (!currentThis.point.equals(currentOther.point)) return false;
                
                //переходим к следующим узлам в обоих списках
                currentThis = currentThis.next;
                currentOther = currentOther.next;
            }
            return true;
        } 
        else {
            TabulatedFunction other = (TabulatedFunction) o;
            
            //проверка кол-ва точек
            if (this.getPointsCount() != other.getPointsCount()) return false;
            
            //сравниваем через getPoint()
            for (int i = 0; i < pointsCount; i++) {
                if (!this.getPoint(i).equals(other.getPoint(i))) return false;//getPoint() создает копию точки
            }
            return true;
        }
    }

    //метод вычисляет хэш-код функции на основе хэш-кодов всех точек и их количества
    //включает количество точек чтобы различать функции с одинаковыми точками но разным количеством
    @Override
    public int hashCode() {
        int result = pointsCount;  //начинаем с количества точек
        FunctionNode current = head.next;
        for (int i = 0; i < pointsCount; i++) {
            result ^= current.point.hashCode();  //комбинируем хэш точки через XOR
            current = current.next;
        }
        return result;
    }

    //метод создает глубокую копию тек. ф-ии через "пересборку" списка
    @Override
    public Object clone() {
        if (pointsCount == 0) return new LinkedListTabulatedFunction();  //если список пуст - возвращаем пустой
        
        //создаем массив клон точек из текущего списка
        FunctionPoint[] pointsArray = new FunctionPoint[pointsCount];
        FunctionNode current = head.next;
        for (int i = 0; i < pointsCount; i++) {
            pointsArray[i] = (FunctionPoint) current.point.clone();  //клонируем каждую точку
            current = current.next;
        }
        
        //создаем новую ф-ию используя конструктор с массивом точек
        return new LinkedListTabulatedFunction(pointsArray);
    }
}