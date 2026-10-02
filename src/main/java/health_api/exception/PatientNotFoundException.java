package health_api.exception;

public class PatientNotFoundException extends RuntimeException{
    public PatientNotFoundException(String error){
        super(error);
    }
}
