package functions;

public class InappropriateFunctionPointException extends Exception {//класс наследуется от Exception
    public InappropriateFunctionPointException() {//создает искл.без сообщ.
        super(); //вызывает конструктор родителя(Exception)
    }
    
    public InappropriateFunctionPointException(String message) {//искл. с текстом ошибки
        super(message);
    }
}