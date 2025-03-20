package org.pomeluce.akir.core.types;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.FactoryExpressionBase;
import com.querydsl.core.types.Visitor;
import jakarta.annotation.Nullable;

import java.util.*;

/**
 * @author : marcus
 * @version : 1.0
 * @date : 2024/8/11 17:53
 * @className : QResultMap
 * @description : querydsl 集合类型 projection
 */
public class ProjectionsQMap extends FactoryExpressionBase<Map<String, ?>> {
    private final List<Expression<?>> args;

    public @SuppressWarnings(value = {"unchecked", "rawtypes"}) ProjectionsQMap(Expression<?>... args) {
        super((Class) Map.class);
        this.args = Collections.unmodifiableList(Arrays.asList(args));
    }

    @Override
    public List<Expression<?>> getArgs() {
        return args;
    }

    @Override
    public @Nullable Map<String, ?> newInstance(Object... args) {
        Map<String, Object> result = new HashMap<>(args.length);
        for (int i = 0; i < args.length; i++) {
            String key = this.getArgs().get(i).toString();
            key = key.contains(" as ") ? key.split(" as ")[1] : key.split("\\.")[1];
            result.put(key, args[i]);
        }
        return result;
    }

    @Override
    public <R, C> @Nullable R accept(Visitor<R, C> visitor, @Nullable C context) {
        return visitor.visit(this, context);
    }

    public static ProjectionsQMap builder(Expression<?>... args) {
        return new ProjectionsQMap(args);
    }
}
