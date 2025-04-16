package hello.core.singleton;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;

import static org.junit.jupiter.api.Assertions.*;

class StatefulServiceTest {

    @Test
    void statefulServiceSingleton() {
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(TestConfig.class);
        StatefulService statefulService1 = ac.getBean(StatefulService.class);
        StatefulService statefulService2 = ac.getBean(StatefulService.class);

        //ThreadA: A사용자 10000원
        statefulService1.order("userA", 10000);

        //ThreadB: B사용자 20000원
        statefulService2.order("userB", 20000);

        int price1 = statefulService1.getPrice();
        System.out.println("price1 = " + price1);

        int price2 = statefulService2.getPrice();
        System.out.println("price2 = " + price2);

        // 주의점 : 객체의 상태가 공유된다. (price2로) --> 스프링 빈은 항상 무상태로 설계해야 한다.
        Assertions.assertThat(price1).isEqualTo(price2);
    }

    static class TestConfig {
        @Bean
        public StatefulService statefulService() {
            return new StatefulService();
        }
    }

}