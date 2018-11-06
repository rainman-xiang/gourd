package org.tieland.gourd.core.send;

import lombok.ToString;
import org.tieland.gourd.common.Gourd;

/**
 * @author zhouxiang
 * @date 2018/10/24 8:51
 */
@ToString
public final class GourdContext {

    /**
     * gourd
     */
    private Gourd gourd;

    public GourdContext(final Gourd gourd){
        this.gourd = gourd;
    }

    public Gourd getGourd() {
        return gourd;
    }
}
