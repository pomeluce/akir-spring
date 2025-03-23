package org.pomeluce.akir.common.core.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.*;
import com.querydsl.jpa.impl.JPAQuery;
import jakarta.annotation.Nullable;
import org.pomeluce.akir.common.utils.ArrayUtils;
import org.pomeluce.akir.common.utils.ObjectUtils;
import org.pomeluce.akir.common.utils.StringUtils;

import java.util.Date;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author : marcus
 * @version : 1.0
 * @date : 2024/8/12 20:59
 * @className : SelectBooleanBuilder
 * @description : 动态查询构造器
 */
public class SelectBooleanBuilder {

    private final BooleanBuilder builder;

    public SelectBooleanBuilder() {
        this.builder = new BooleanBuilder();
    }

    public static SelectBooleanBuilder builder() {
        return new SelectBooleanBuilder();
    }

    public BooleanBuilder build() {
        return builder;
    }

    public SelectBooleanBuilder and(@Nullable Predicate predicate) {
        builder.and(predicate);
        return this;
    }

    public SelectBooleanBuilder andAnyOf(Predicate... args) {
        builder.andAnyOf(args);
        return this;
    }

    public SelectBooleanBuilder andNot(Predicate predicate) {
        builder.andNot(predicate);
        return this;
    }

    public SelectBooleanBuilder or(@Nullable Predicate predicate) {
        builder.or(predicate);
        return this;
    }

    public SelectBooleanBuilder orAllOf(Predicate... args) {
        builder.orAllOf(args);
        return this;
    }

    public SelectBooleanBuilder orNot(Predicate predicate) {
        builder.or(predicate.not());
        return this;
    }

    public SelectBooleanBuilder notEmptyEq(String param, StringPath path) {
        if (StringUtils.isNotBlank(param)) builder.and(path.eq(param));
        return this;
    }

    public SelectBooleanBuilder notEmptyEq(Boolean param, BooleanPath path) {
        if (ObjectUtils.isNotEmpty(param)) builder.and(path.eq(param));
        return this;
    }

    public <T extends Number & Comparable<?>> SelectBooleanBuilder notEmptyEq(T param, NumberPath<T> path) {
        if (ObjectUtils.isNotEmpty(param)) builder.and(path.eq(param));
        return this;
    }

    public <T extends Enum<T>> SelectBooleanBuilder notEmptyEq(T param, EnumPath<T> path) {
        if (ObjectUtils.isNotEmpty(param)) builder.and(path.eq(param));
        return this;
    }

    public <T extends Date & Comparable<Date>> SelectBooleanBuilder notEmptyEq(T param, DatePath<T> path) {
        if (ObjectUtils.isNotEmpty(param)) builder.and(path.eq(param));
        return this;
    }

    public <T extends Date & Comparable<Date>> SelectBooleanBuilder notEmptyEq(T param, DateTimePath<T> path) {
        if (ObjectUtils.isNotEmpty(param)) builder.and(path.eq(param));
        return this;
    }

    public SelectBooleanBuilder notEmptyIn(String[] params, StringPath path) {
        if (ArrayUtils.isNotEmpty(params)) builder.and(path.in(params));
        return this;
    }

    public <T extends Number & Comparable<?>> SelectBooleanBuilder notEmptyIn(T[] params, NumberPath<T> path) {
        if (ArrayUtils.isNotEmpty(params)) builder.and(path.in(params));
        return this;
    }

    public <T extends Enum<T>> SelectBooleanBuilder notEmptyIn(T[] param, EnumPath<T> path) {
        if (ArrayUtils.isNotEmpty(param)) builder.and(path.in(param));
        return this;
    }

    public SelectBooleanBuilder notEmptyEqOrIn(String param, StringPath path, JPAQuery<String> query) {
        if (StringUtils.isNotBlank(param)) builder.and(path.eq(param).or(path.in(query)));
        return this;
    }

    public <T extends Number & Comparable<?>> SelectBooleanBuilder notEmptyEqOrIn(T param, NumberPath<T> path, JPAQuery<T> query) {
        if (ObjectUtils.isNotEmpty(param)) builder.and(path.eq(param).or(path.in(query)));
        return this;
    }

    public <T extends Enum<T>> SelectBooleanBuilder notEmptyEqOrIn(T param, EnumPath<T> path, JPAQuery<T> query) {
        if (ObjectUtils.isNotEmpty(param)) builder.and(path.eq(param).or(path.in(query)));
        return this;
    }

    public SelectBooleanBuilder notEmptyExpression(BooleanExpression expression) {
        if (ObjectUtils.isNotEmpty(expression)) builder.and(expression);
        return this;
    }

    public SelectBooleanBuilder notEmptyExpressionAnyOf(BooleanExpression... expressions) {
        if (ObjectUtils.isNotEmpty(expressions)) builder.andAnyOf(expressions);
        return this;
    }

    public SelectBooleanBuilder notEmptyLike(String param, StringPath path) {
        if (StringUtils.isNotBlank(param)) builder.and(path.like("%" + param + "%"));
        return this;
    }

    public SelectBooleanBuilder notEmptyNotLik(String param, StringPath path) {
        if (StringUtils.isNotBlank(param)) builder.and(path.notLike("%" + param + "%"));
        return this;
    }

    public <T extends Date & Comparable<Date>> SelectBooleanBuilder notEmptyDateTimeBefore(T param, DateTimePath<T> path) {
        if (ObjectUtils.isNotEmpty(param)) builder.and(path.goe(param));
        return this;
    }

    public <T extends Date & Comparable<Date>> SelectBooleanBuilder notEmptyDateTimeAfter(T param, DateTimePath<T> path) {
        if (ObjectUtils.isNotEmpty(param)) builder.and(path.loe(param));
        return this;
    }

    public SelectBooleanBuilder with(Consumer<SelectBooleanBuilder> consumer) {
        if (ObjectUtils.isNotEmpty(consumer)) consumer.accept(this);
        return this;
    }

    public SelectBooleanBuilder custom(Supplier<Predicate> supplier) {
        builder.and(supplier.get());
        return this;
    }
}
