package org.tieland.gourd.core.annotation;

import java.lang.annotation.*;

/**
 * @author zhouxiang
 * @date 2018/10/24 9:08
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface GourdValve {

    ValveGrade grade() default ValveGrade.HIGH;

    enum ValveGrade{

        /**
         * 低一致
         */
        LOW,

        /**
         * 强一致
         */
        HIGH

    }

}
