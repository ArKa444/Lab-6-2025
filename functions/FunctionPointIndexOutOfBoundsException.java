package functions;

public class FunctionPointIndexOutOfBoundsException extends IndexOutOfBoundsException {
    public FunctionPointIndexOutOfBoundsException() {//исключение без специального сообщения
        super();//констр.родителя
    }

    public FunctionPointIndexOutOfBoundsException(String message) {//когда нужно указать конкретную причину ошибки
        super(message);
    }
}