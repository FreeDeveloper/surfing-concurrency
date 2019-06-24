package cn.smile.spring.container.bean.factoryBean;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.FactoryBean;

public class CarFactoryBean implements FactoryBean<Car> {

    @Getter
    @Setter
    private String carInfo;

    public Car getObject() throws Exception {
        Car car = new Car();
        String [] infos = carInfo.split(",");
        car.setBrand(infos[0]);
        car.setMaxSpeed(Integer.valueOf(infos[1]));
        car.setPrice(Double.valueOf(infos[2]));
        return car;
    }

    public Class<?> getObjectType() {
        return Car.class;
    }

    public boolean isSingleton() {
        return false;
    }
}
