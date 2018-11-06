package org.tieland.gourd.common;

import com.google.common.base.Strings;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.tieland.gourd.common.support.ObjectConverterFactory;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * @author zhouxiang
 * @date 2018/10/24 9:17
 */
@ToString
@EqualsAndHashCode
public final class Gourd implements Serializable {

    private static final long serialVersionUID = 6130166304773807844L;

    /**
     * id
     */
    private String id;

    /**
     * 内容-json
     */
    private String payload;

    /**
     * 目的地
     */
    private Destination destination;

    public String getId() {
        return id;
    }

    public String getPayload() {
        return payload;
    }

    public Destination getDestination() {
        return destination;
    }

    public static Builder builder(){
        return new Builder();
    }

    public static Recover recover(){
        return new Recover();
    }

    public static class Builder<T extends Serializable> {

        private String id;

        private T payload;

        private Destination destination;

        public Builder payload(T payload){
            this.payload = payload;
            return this;
        }

        public Builder destination(Destination destination){
            this.destination = destination;
            return this;
        }

        public Builder id(String id){
            this.id = id;
            return this;
        }

        public Gourd build(){
            Gourd gourd = new Gourd();

            if(Strings.isNullOrEmpty(id)){
                gourd.id = UUID.randomUUID().toString();
            }

            if(Objects.isNull(payload)){
                throw new IllegalArgumentException(" payload must has a not null value ");
            }

            if(payload instanceof String){
                gourd.payload = ObjectConverterFactory.getInstance().get().from(
                        GourdPayload.builder().id(gourd.id).content((String) payload).build());
            }else{
                gourd.payload = ObjectConverterFactory.getInstance().get().from(
                        GourdPayload.builder().id(gourd.id).content(ObjectConverterFactory.getInstance().get().from(payload)).build());
            }

            if(Objects.isNull(destination)){
                throw new IllegalArgumentException(" destination must has a not null value ");
            }

            gourd.destination = destination;

            return gourd;
        }
    }

    public static class Recover {

        private String id;

        private String payload;

        private Destination destination;

        public Recover payload(String payload){
            this.payload = payload;
            return this;
        }

        public Recover destination(Destination destination){
            this.destination = destination;
            return this;
        }

        public Recover id(String id){
            this.id = id;
            return this;
        }

        public Gourd build(){
            Gourd gourd = new Gourd();

            if(Objects.isNull(id)){
                throw new IllegalArgumentException(" id must has a not null value ");
            }
            gourd.id = id;

            if(Objects.isNull(payload)){
                throw new IllegalArgumentException(" payload must has a not null value ");
            }
            gourd.payload = payload;

            if(Objects.isNull(destination)){
                throw new IllegalArgumentException(" destination must has a not null value ");
            }
            gourd.destination = destination;

            return gourd;
        }

    }

}
