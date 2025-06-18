package org.akir.common.utils.beans;

import java.io.Serializable;
import java.util.Set;

/**
 * @author : marcus
 * @version : 1.0
 * @date : 2023/11/12下午3:42
 * @className : BeanCopyOptions
 * @description : bean 拷贝选项
 */
public final class BeanCopyOptions implements Serializable {
    Class<?> editable;
    boolean ignoreNullValue;
    boolean override = true;
    private Set<String> ignoreProperties;

    private BeanCopyOptions() {
    }

    private BeanCopyOptions(Class<?> editable, boolean ignoreNullValue, String... ignoreProperties) {
        this.editable = editable;
        this.ignoreNullValue = ignoreNullValue;
        this.setIgnoreProperties(ignoreProperties);
    }

    /**
     * 获取 bean 拷贝选项对象
     *
     * @return 返回一个 {@link BeanCopyOptions} 类型的 bean 拷贝选项对象
     */
    public static BeanCopyOptions instance() {
        return new BeanCopyOptions();
    }

    /**
     * 获取 bean 拷贝选项对象
     *
     * @param editable         限制类或接口 {@link Class}
     * @param ignoreNullValue  是否忽略 null 值 {@link Boolean}
     * @param ignoreProperties 忽略的属性名称列表 {@link String}
     * @return 返回一个 {@link BeanCopyOptions} 类型的 bean 拷贝选项对象
     */
    public static BeanCopyOptions instance(Class<?> editable, boolean ignoreNullValue, String... ignoreProperties) {
        return new BeanCopyOptions(editable, ignoreNullValue, ignoreProperties);
    }

    /**
     * 判断是否忽略某个属性
     *
     * @param property 要判断是否忽略的属性 {@link String}
     * @return 返回一个 {@link Boolean} 类型的对象属性忽略结果
     */
    public boolean hasIgnoreProperty(String property) {
        return ignoreProperties != null && ignoreProperties.contains(property);
    }

    /**
     * 忽略 null 值, 当源对象属性值为 null 时忽略拷贝到目标对象
     *
     * @return 返回一个 {@link BeanCopyOptions} 类型的 bean 拷贝选项对象
     */
    public BeanCopyOptions ignoreNullValue() {
        return setIgnoreNullValue(true);
    }

    /**
     * 设置目标对象的限制类或接口
     *
     * @param editable 限制类或接口 {@link Class}
     * @return 返回一个 {@link BeanCopyOptions} 类型的 bean 拷贝选项对象
     */
    public BeanCopyOptions setEditable(Class<?> editable) {
        this.editable = editable;
        return this;
    }

    /**
     * 设置是否忽略 null 值, 当源对象属性值为 null 时是否拷贝到目标对象
     * <br>
     * <p> {@code true}: 忽略 {@code null} 值, {@code false}: 注入 {@code null} 值</p>
     * <br>
     *
     * @param ignoreNullValue 是否忽略 null 值 {@link Boolean}
     * @return 返回一个 {@link BeanCopyOptions} 类型的 bean 拷贝选项对象
     */
    public BeanCopyOptions setIgnoreNullValue(boolean ignoreNullValue) {
        this.ignoreNullValue = ignoreNullValue;
        return this;
    }

    /**
     * 设置忽略的属性名称列表
     *
     * @param ignoreProperties 忽略的属性名称列表 {@link String}
     * @return 返回一个 {@link BeanCopyOptions} 类型的 bean 拷贝选项对象
     */
    public BeanCopyOptions setIgnoreProperties(String... ignoreProperties) {
        this.ignoreProperties = Set.of(ignoreProperties);
        return this;
    }

    /**
     * 设置是否覆盖已有属性值
     * <br>
     * <p> {@code true}: 覆盖已有属性值, {@code false}: 不覆盖已有属性值</p>
     * <br>
     *
     * @param override 是否覆盖已有属性值 {@link Boolean}
     * @return 返回一个 {@link BeanCopyOptions} 类型的 bean 拷贝选项对象
     */
    public BeanCopyOptions setOverride(boolean override) {
        this.override = override;
        return this;
    }
}
