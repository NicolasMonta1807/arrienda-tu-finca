package web.mates.arriendatufinca.exceptions;

public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException (String message) {
        super(message);
    }

    public UnauthorizedException() {
        super("Not authorized");
    }
}
