package pl.recruitment.songsratingservice.domain.exception;

public class SystemTechnicalException extends RuntimeException {

  public SystemTechnicalException(String message, Throwable originException) {
    super(message, originException);
  }
}
