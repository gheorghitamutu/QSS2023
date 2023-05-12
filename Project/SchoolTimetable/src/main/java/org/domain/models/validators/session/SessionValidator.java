package org.domain.models.validators.session;

import com.google.inject.Injector;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.GuiceInjectorSingleton;
import org.dataaccess.session.ISessionRepository;
import org.domain.models.Session;

public class SessionValidator implements ConstraintValidator<ValidSession, Session> {

    ISessionRepository sessionRepository;

    @Override
    public void initialize(ValidSession constraintAnnotation) {

        Injector injector = GuiceInjectorSingleton.INSTANCE.getInjector();
        if (injector != null) {
            sessionRepository = injector.getInstance(ISessionRepository.class);
        }
    }

    @Override
    public boolean isValid(Session value, ConstraintValidatorContext context) {

        return value.getType() != Session.Type.COURSE || value.getHalfYear() != null;
    }
}

