package org.domain.models.validators.session;

import com.google.inject.Injector;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.GuiceInjectorSingleton;
import org.dataaccess.session.ISessionRepository;
import org.domain.models.Session;

/**
 * This class is the validator for the ValidSession annotation.
 */
public class SessionValidator implements ConstraintValidator<ValidSession, Session> {

    /**
     * The session repository.
     */
    ISessionRepository sessionRepository;

    /**
     * This method initializes the session repository.
     * @param constraintAnnotation The annotation.
     */
    @Override
    public void initialize(ValidSession constraintAnnotation) {

        Injector injector = GuiceInjectorSingleton.INSTANCE.getInjector();
        if (injector != null) {
            sessionRepository = injector.getInstance(ISessionRepository.class);
        }
    }

    /**
     * This method checks if the session is valid.
     * @param value The session.
     * @param context The context.
     * @return True if the session is valid, false otherwise.
     */
    @Override
    public boolean isValid(Session value, ConstraintValidatorContext context) {
        return value.getType() != Session.Type.COURSE || value.getHalfYear() != null;
    }
}

