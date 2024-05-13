package web.mates.arriendatufinca.exceptions;

public class DuplicateEmailException extends RuntimeException{
    public DuplicateEmailException(String message) {
        super(message);
    }
}
