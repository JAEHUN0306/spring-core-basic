package hello.core.scope;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Scope;

import static org.assertj.core.api.Assertions.*;

public class PrototypeTest {
    @Test
    void prototypeBeanFind() {
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(prototypeBean.class);
        System.out.println("PrototypeTest.prototypeBeanFind1");
        prototypeBean bean1 = ac.getBean(prototypeBean.class);
        System.out.println("PrototypeTest.prototypeBeanFind2");
        prototypeBean bean2 = ac.getBean(prototypeBean.class);

        System.out.println("bean1 = " + bean1);
        System.out.println("bean2 = " + bean2);

        assertThat(bean1).isNotSameAs(bean2);
        ac.close();
        /*
        prototypeBean.init
        prototypeBean.init
        bean1 = hello.core.scope.PrototypeTest$prototypeBean@6058e535
        bean2 = hello.core.scope.PrototypeTest$prototypeBean@42deb43a

        @PostConstruct 메서드는 2번 실행, @PreDestroy 메서드는 실행 x
         */
    }

    @Scope("prototype")
    static class prototypeBean {
        @PostConstruct
        public void init() {
            System.out.println("prototypeBean.init");
        }

        @PreDestroy
        public void close() {
            System.out.println("prototypeBean.close");
        }
    }
}
