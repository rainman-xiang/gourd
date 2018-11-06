package org.tieland.gourd.core.annotation;

import java.lang.annotation.*;

/**
 * @author zhouxiang
 * @date 2018/10/24 13:59
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface GourdListener {

    /**
     * queue
     * @return
     */
    String queue();

    /**
     * group
     * @return
     */
    String group() default "";
}
