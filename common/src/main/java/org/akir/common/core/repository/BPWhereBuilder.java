package org.akir.common.core.repository;

import com.blazebit.persistence.CriteriaBuilder;
import org.akir.common.utils.ObjectUtils;
import org.akir.common.utils.StringUtils;

import java.util.Collection;
import java.util.Date;

/**
 * @param <T> 实体类型
 * @author : marcus
 * @version : 1.0
 * @date : 2025-06-07 20:59
 * @className : BPWhereBuilder
 * @description : 动态查询构造器
 * BPWhereBuilder：Blaze-Persistence CriteriaBuilder 的动态条件拼接器，
 * 构造时传入 (CriteriaBuilder<T>, alias), 内部直接拼 "alias.fieldName"
 * <p>
 * Examples：
 * <blockquote><pre>
 * CriteriaBuilder<User> cb = cbf.create(em, User.class, "u");
 * BPWhereBuilder.<User>builder(cb, "u")
 *     .notEmptyEq(user.getId(), "id")
 *     .notEmptyLike(user.getAccount(), "account")
 *     .notEmptyIn(user.getStatuses(), "status")
 *     .build();
 * </pre></blockquote>
 */
public class BPWhereBuilder<T> {

    /**
     * Blaze-Persistence 的 CriteriaBuilder<T>, 已包含 from(xxx, alias)
     */
    private final CriteriaBuilder<T> cb;

    /**
     * 别名, 比如 "u"
     */
    private final String alias;

    private BPWhereBuilder(CriteriaBuilder<T> cb, String alias) {
        this.cb = cb;
        this.alias = alias;
    }

    /**
     * 静态工厂: 传入已创建好 from + alias 的 cb, 以及对应的 alias 字符串
     *
     * @param cb  Blaze-Persistence 的 CriteriaBuilder<T>
     * @param <T> 实体类型
     * @return BPWhereBuilder<T> 实例
     */
    public static <T> BPWhereBuilder<T> builder(CriteriaBuilder<T> cb) {
        return new BPWhereBuilder<>(cb, ((BlazePersistenceAware) cb).getAlias());
    }

    /**
     * 如果 param != null, 则拼 “AND alias.fieldName = param”
     * 例如 alias="u", field="id", 最终调用 cb.where("u.id").eq(param)
     */
    public BPWhereBuilder<T> notEmptyEq(Object param, String field) {
        if (ObjectUtils.isNotEmpty(param)) cb.where(alias + "." + field).eq(param);
        return this;
    }

    /**
     * 如果 paramStr 不为空串，则拼 AND alias.field LIKE :param
     * 最终调用: .like().value("%xxx%").noEscape()
     */
    public BPWhereBuilder<T> notEmptyLike(String paramStr, String field) {
        return notEmptyLike(paramStr, field, true);
    }

    /**
     * 如果 paramStr 不为空串，则拼 AND alias.field LIKE :param, 支持大小写敏感开关
     * 最终调用: .like().value("%xxx%").noEscape()
     *
     * @param caseSensitive true 表示大小写敏感, false 表示不敏感(内部会 UPPER())
     */
    public BPWhereBuilder<T> notEmptyLike(String paramStr, String field, boolean caseSensitive) {
        if (StringUtils.isNotBlank(paramStr)) cb.where(alias + "." + field).like(caseSensitive).value("%" + paramStr.trim() + "%").noEscape();
        return this;
    }

    /**
     * 如果 paramStr 不为空串，则拼 AND alias.field LIKE '%param%'
     * 最终调用: .like().expression("'%xxx%'").noEscape()
     */
    public BPWhereBuilder<T> notEmptyLikeExpression(String paramStr, String field) {
        return notEmptyLikeExpression(paramStr, field, true);
    }

    /**
     * 如果 paramStr 不为空串，则拼 AND alias.field LIKE '%param%', 支持大小写敏感开关
     * 最终调用: .like().expression("'%xxx%'").noEscape()
     *
     * @param caseSensitive true 表示大小写敏感, false 表示不敏感(内部会 UPPER())
     */
    public BPWhereBuilder<T> notEmptyLikeExpression(String paramStr, String field, boolean caseSensitive) {
        if (StringUtils.isNotBlank(paramStr)) cb.where(alias + "." + field).like(caseSensitive).expression("'%" + paramStr.trim() + "%'").noEscape();
        return this;
    }

    /**
     * 如果 paramStr 不为空串, 则拼 AND alias.field NOT LIKE :param
     * 最终调用: .notLike(false).value("%xxx%").noEscape()
     */
    public BPWhereBuilder<T> notEmptyNotLike(String paramStr, String field) {
        return notEmptyNotLike(paramStr, field, true);
    }

    /**
     * 如果 paramStr 不为空串, 则拼 AND alias.field NOT LIKE :param, 支持大小写敏感开关
     * 最终调用: .notLike(false).value("%xxx%").noEscape()
     *
     * @param caseSensitive true 表示大小写敏感, false 表示不敏感(内部会 UPPER())
     */
    public BPWhereBuilder<T> notEmptyNotLike(String paramStr, String field, boolean caseSensitive) {
        if (StringUtils.isNotBlank(paramStr)) cb.where(alias + "." + field).notLike(caseSensitive).value("%" + paramStr.trim() + "%").noEscape();
        return this;
    }

    /**
     * 如果 paramStr 不为空串, 则拼 AND alias.field NOT LIKE '%param%'
     * 最终调用: .notLike(false).expression("'%xxx%'").noEscape()
     */
    public BPWhereBuilder<T> notEmptyNotLikeExpression(String paramStr, String field) {
        return notEmptyNotLikeExpression(paramStr, field, true);
    }

    /**
     * 如果 paramStr 不为空串, 则拼 AND alias.field NOT LIKE '%param%', 支持大小写敏感开关
     * 最终调用: .notLike(false).expression("'%xxx%'").noEscape()
     *
     * @param caseSensitive true 表示大小写敏感, false 表示不敏感(内部会 UPPER())
     */
    public BPWhereBuilder<T> notEmptyNotLikeExpression(String paramStr, String field, boolean caseSensitive) {
        if (StringUtils.isNotBlank(paramStr)) cb.where(alias + "." + field).notLike(caseSensitive).expression("'%" + paramStr.trim() + "%'").noEscape();
        return this;
    }

    /**
     * 如果集合 paramCol 非空, 则拼 "AND alias.field IN (:paramCol)"
     */
    public <E> BPWhereBuilder<T> notEmptyIn(Collection<E> paramCol, String field) {
        if (paramCol != null && !paramCol.isEmpty()) cb.where(alias + "." + field).in(paramCol);
        return this;
    }

    /**
     * 如果 startDate != null, 则拼 "AND alias.field >= startDate"
     */
    public BPWhereBuilder<T> notEmptyDateAfter(Date startDate, String field) {
        if (startDate != null) cb.where(alias + "." + field).gt(startDate);
        return this;
    }

    /**
     * 如果 endDate != null, 则拼 "AND alias.field <= endDate"
     */
    public BPWhereBuilder<T> notEmptyDateBefore(Date endDate, String field) {
        if (endDate != null) cb.where(alias + "." + field).lt(endDate);
        return this;
    }

    /**
     * 用于传入任意复杂条件: 例如 OR、子查询等
     * .custom(cb -> cb.where("u.score").gt(100).or("u.level").eq(5))
     */
    public BPWhereBuilder<T> custom(ConditionConsumer<T> consumer) {
        if (consumer != null) consumer.apply(cb);
        return this;
    }

    /**
     * 构建完成后, 把 CriteriaBuilder<T> 返回给调用方做后续的 orderBy/分页/getResultList()
     */
    public CriteriaBuilder<T> build() {
        return this.cb;
    }

    /**
     * 自定义条件拼接器的函数式接口
     */
    @FunctionalInterface
    public interface ConditionConsumer<T> {
        void apply(CriteriaBuilder<T> cb);
    }
}
