package web.mates.arriendatufinca.exceptions;

public class InvalidBookingStatusException extends RuntimeException{
    public InvalidBookingStatusException(String message){
        super(message);
    }
}
