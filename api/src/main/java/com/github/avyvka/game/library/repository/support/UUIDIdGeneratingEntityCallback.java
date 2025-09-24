package com.github.avyvka.game.library.repository.support;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mapping.PersistentPropertyAccessor;
import org.springframework.data.mapping.context.MappingContext;
import org.springframework.data.r2dbc.mapping.OutboundRow;
import org.springframework.data.r2dbc.mapping.event.BeforeSaveCallback;
import org.springframework.data.relational.core.mapping.RelationalPersistentEntity;
import org.springframework.data.relational.core.mapping.RelationalPersistentProperty;
import org.springframework.data.relational.core.sql.SqlIdentifier;
import org.springframework.lang.NonNull;
import org.springframework.r2dbc.core.Parameter;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
public class UUIDIdGeneratingEntityCallback implements BeforeSaveCallback<Object> {

    private final MappingContext<RelationalPersistentEntity<?>, ? extends RelationalPersistentProperty> context;

    @Autowired
    public UUIDIdGeneratingEntityCallback(
            MappingContext<RelationalPersistentEntity<?>, ? extends RelationalPersistentProperty> context
    ) {
        this.context = context;
    }

    @NonNull
    @Override
    public Mono<Object> onBeforeSave(@NonNull Object entity, @NonNull OutboundRow row, @NonNull SqlIdentifier table) {

        Assert.notNull(entity, "Entity must not be null");

        RelationalPersistentEntity<?> persistentEntity = context.getRequiredPersistentEntity(entity.getClass());

        if (!persistentEntity.hasIdProperty()) {
            return Mono.just(entity);
        }

        RelationalPersistentProperty property = persistentEntity.getRequiredIdProperty();
        PersistentPropertyAccessor<Object> accessor = persistentEntity.getPropertyAccessor(entity);

        if (!persistentEntity.isNew(entity) || !UUID.class.isAssignableFrom(property.getType())) {
            return Mono.just(entity);
        }

        var id = UUID.randomUUID();

        row.append(property.getColumnName(), Parameter.from(id));
        accessor.setProperty(property, id);

        return Mono.just(accessor.getBean()).defaultIfEmpty(entity);
    }
}