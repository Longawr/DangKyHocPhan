package com.dust.courseRegistration.generator;

import static org.hibernate.internal.util.ReflectHelper.getPropertyType;

import java.io.Serializable;
import java.lang.reflect.Member;
import java.util.EnumSet;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.hibernate.HibernateException;
import org.hibernate.SharedSessionContract;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.generator.BeforeExecutionGenerator;
import org.hibernate.generator.EventType;
import org.hibernate.generator.EventTypeSets;
import org.hibernate.id.factory.spi.CustomIdGeneratorCreationContext;

import com.dust.courseRegistration.entity.UserId;

public class UserIdGenerator implements BeforeExecutionGenerator {
    private static final long serialVersionUID = -3202980377818774891L;

    private final AtomicInteger sequence = new AtomicInteger(-1);
    private final String prefix;
    private final String sqlQuery;

    public UserIdGenerator(UserId config, Member idMember, CustomIdGeneratorCreationContext creationContext) {
        final Class<?> propertyType = getPropertyType(idMember);

        this.prefix = config.isTeacher() ? "gv" : "sv";
        String tableName = config.isTeacher() ? "teacher" : "student";
        this.sqlQuery = String.format("SELECT TOP 1 id FROM %s ORDER BY id DESC", tableName);
        if (!String.class.isAssignableFrom(propertyType)) {
            throw new HibernateException(
                    "Unanticipated return type [" + propertyType.getName() + "] for UserId conversion");
        }
    }

    @Override
    public Serializable generate(
            SharedSessionContractImplementor session, Object owner, Object currentValue, EventType eventType) {
        if (sequence.get() == -1) {

            SharedSessionContract statelessSession =
                    session.isStatelessSession() ? session.asStatelessSession() : session.getSession();

            List<String> list = statelessSession
                    .createNativeQuery(sqlQuery, String.class)
                    .setFetchSize(1)
                    .getResultList();
            this.sequence.set(list.isEmpty() ? 0 : Integer.parseInt(list.get(0)));
        }
        return this.prefix + String.format("%04d", sequence.incrementAndGet());
    }

    @Override
    public EnumSet<EventType> getEventTypes() {
        return EventTypeSets.INSERT_ONLY;
    }
}
