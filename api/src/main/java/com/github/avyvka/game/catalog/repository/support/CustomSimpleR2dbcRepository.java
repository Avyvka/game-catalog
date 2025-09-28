package com.github.avyvka.game.catalog.repository.support;

import com.github.avyvka.game.catalog.repository.api.CustomR2dbcRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.convert.R2dbcConverter;
import org.springframework.data.r2dbc.core.R2dbcEntityOperations;
import org.springframework.data.r2dbc.repository.support.SimpleR2dbcRepository;
import org.springframework.data.relational.core.query.Query;
import org.springframework.data.relational.repository.query.RelationalEntityInformation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import reactor.core.publisher.Flux;

@Transactional(readOnly = true)
public class CustomSimpleR2dbcRepository<T, ID> extends SimpleR2dbcRepository<T, ID> implements CustomR2dbcRepository<T, ID> {

    private final RelationalEntityInformation<T, ID> entity;

    private final R2dbcEntityOperations entityOperations;

    protected CustomSimpleR2dbcRepository(
            RelationalEntityInformation<T, ID> entity,
            R2dbcEntityOperations entityOperations,
            R2dbcConverter converter
    ) {
        super(entity, entityOperations, converter);
        this.entity = entity;
        this.entityOperations = entityOperations;
    }

    @Override
    public Flux<T> findAll(Pageable pageable) {
        Assert.notNull(pageable, "Pageable must not be null");

        var query = Query.empty()
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .sort(pageable.getSort());

        return this.entityOperations.select(query, this.entity.getJavaType());
    }
}
