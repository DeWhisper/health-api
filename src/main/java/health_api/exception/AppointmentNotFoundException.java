package health_api.exception;

public class AppointmentNotFoundException extends RuntimeException{
    public AppointmentNotFoundException(String error){
        super(error);
    }
}
