package ke.co.nectar.user.service.user.impl.exceptions;

public class EmailExistsException extends Exception {

    public EmailExistsException(String message) {
        super(message);
    }
}
