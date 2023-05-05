package org.application.domain.models.validators.session;

import com.google.inject.Injector;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.application.GuiceInjectorSingleton;
import org.application.dataaccess.session.ISessionRepository;
import org.application.domain.models.Session;
import org.application.domain.models.Teacher;
import org.application.domain.models.Timeslot;

import java.time.Duration;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

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

