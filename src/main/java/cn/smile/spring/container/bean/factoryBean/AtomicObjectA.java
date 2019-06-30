package cn.smile.spring.container.bean.factoryBean;


import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.atomic.AtomicInteger;

public class AtomicObjectA {
    @Getter
    @Setter
    private AtomicInteger a = new AtomicInteger();

    public void addOne(){
        a.incrementAndGet();
    }
}
