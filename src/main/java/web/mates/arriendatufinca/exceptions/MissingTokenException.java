package web.mates.arriendatufinca.exceptions;

public class MissingTokenException extends RuntimeException {
    public MissingTokenException() {
        super("No Authorization Bearer Token");
    }
}
