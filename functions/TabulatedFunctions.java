package functions;

import java.io.*;

public class TabulatedFunctions {
    //Конструктор(запрещаем создание объектов)
    private TabulatedFunctions() {
        throw new AssertionError("Нельзя создавать объекты класса TabulatedFunctions");
    }
    
    public static TabulatedFunction tabulate(Function function, double leftX, double rightX, int pointsCount) {
        //проверяем, что границы табулирования входят в о.о. ф-ии
        if (leftX < function.getLeftDomainBorder() || rightX > function.getRightDomainBorder()) {
            throw new IllegalArgumentException("Границы табулирования [" + leftX + ", " + rightX + "] " + "выходят за область определения функции [" + function.getLeftDomainBorder() + ", " + function.getRightDomainBorder() + "]");
        }
        
        if (pointsCount < 2) {
            throw new IllegalArgumentException("Количество точек должно быть не менее 2");
        }
        
        //создаем массив значений Y
        double[] values = new double[pointsCount];
        double step = (rightX - leftX) / (pointsCount - 1);
        
        //заполняем значения ф-ии в точках
        for (int i = 0; i < pointsCount; i++) {
            double x = leftX + i * step;
            values[i] = function.getFunctionValue(x);
        }
        
        //возвращаем табулированную функцию 
        return new ArrayTabulatedFunction(leftX, rightX, values);
    }
    
    //Вывод табулированной функции в байтовый поток(pointsCount, x1, y1, x2, y2,...)
    public static void outputTabulatedFunction(TabulatedFunction function, OutputStream out) {
        try (DataOutputStream dos = new DataOutputStream(out)) {
            int pointsCount = function.getPointsCount();
            dos.writeInt(pointsCount); //записываем кол-во точек
           
            //записываем координаты всех точек
            for (int i = 0; i < pointsCount; i++) {
                dos.writeDouble(function.getPointX(i));
                dos.writeDouble(function.getPointY(i));
            }
            
            dos.flush();
            //поток НЕ закрываем - это ответственность вызывающего кода
        } catch (IOException e) {
            // Преобразуем checked exception в unchecked
            throw new RuntimeException("Ошибка вывода функции", e);
        }
    }
    
    //ввод табулированной функции из байтового потока
    public static TabulatedFunction inputTabulatedFunction(InputStream in) {
        try (DataInputStream dis = new DataInputStream(in)) {
            int pointsCount = dis.readInt(); // читаем количество точек
            
            // Читаем координаты точек
            FunctionPoint[] points = new FunctionPoint[pointsCount];
            for (int i = 0; i < pointsCount; i++) {
                double x = dis.readDouble();
                double y = dis.readDouble();
                points[i] = new FunctionPoint(x, y);
            }
            
            // Создаем табулированную функцию
            return new ArrayTabulatedFunction(points);
            
        } catch (IOException e) {
            throw new RuntimeException("Ошибка ввода функции", e);
        }
    }
    
    //Запись табулированной функции в символьный поток
    public static void writeTabulatedFunction(TabulatedFunction function, Writer out) {
        PrintWriter writer = new PrintWriter(new BufferedWriter(out));
        int pointsCount = function.getPointsCount();
        writer.print(pointsCount + " "); // записываем количество точек
        
        // Записываем координаты через пробелы
        for (int i = 0; i < pointsCount; i++) {
            writer.print(function.getPointX(i) + " " + function.getPointY(i) + " ");
        }
        
        writer.flush();
        // Поток НЕ закрываем
    }
    
    //Чтение табулированной функции из символьного потока
    public static TabulatedFunction readTabulatedFunction(Reader in) {
        try (BufferedReader reader = new BufferedReader(in)) {
            StreamTokenizer tokenizer = new StreamTokenizer(reader);
            tokenizer.parseNumbers(); // говорим, что числа - это числа
            
            // Читаем количество точек
            if (tokenizer.nextToken() != StreamTokenizer.TT_NUMBER) {
                throw new RuntimeException("Ожидалось количество точек");
            }
            int pointsCount = (int) tokenizer.nval;
            
            // Читаем координаты точек
            FunctionPoint[] points = new FunctionPoint[pointsCount];
            for (int i = 0; i < pointsCount; i++) {
                if (tokenizer.nextToken() != StreamTokenizer.TT_NUMBER) {
                    throw new RuntimeException("Ожидалась координата X");
                }
                double x = tokenizer.nval;
                
                if (tokenizer.nextToken() != StreamTokenizer.TT_NUMBER) {
                    throw new RuntimeException("Ожидалась координата Y");
                }
                double y = tokenizer.nval;
                
                points[i] = new FunctionPoint(x, y);
            }
            
            return new ArrayTabulatedFunction(points);
            
        } catch (IOException e) {
            throw new RuntimeException("Ошибка чтения функции", e);
        }
    }
}