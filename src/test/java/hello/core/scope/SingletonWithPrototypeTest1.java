package hello.core.scope;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Scope;

public class SingletonWithPrototypeTest1 {

    @Test
    void prototypeFind() {
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(PrototypeBean.class);
        System.out.println("SingletonWithPrototypeTest1.prototypeFind1");
        PrototypeBean bean1 = ac.getBean(PrototypeBean.class);
        bean1.addCount();

        System.out.println("SingletonWithPrototypeTest1.prototypeFind1");
        PrototypeBean bean2 = ac.getBean(PrototypeBean.class);
        bean2.addCount();

        Assertions.assertThat(bean1.getCount()).isEqualTo(1);
        Assertions.assertThat(bean2.getCount()).isEqualTo(1);
    }

    @Test
    void singletonClientUsePrototype() {
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(PrototypeBean.class, ClientBean.class);

        ClientBean bean1 = ac.getBean(ClientBean.class);
        int logic1 = bean1.logic();

        ClientBean bean2 = ac.getBean(ClientBean.class);
        int logic2 = bean2.logic();

        Assertions.assertThat(logic1).isEqualTo(1);
        Assertions.assertThat(logic2).isEqualTo(1);
    }

    @Scope("prototype")
    static class PrototypeBean {
        private int count = 0;

        public void addCount() {
            count++;
        }

        public int getCount() {
            return count;
        }

        @PostConstruct
        public void init() {
            System.out.println("prototypeBean.init " + this);
        }

        @PreDestroy
        public void destroy() {
            System.out.println("prototypeBean.destory");
        }
    }

    @Scope
    static class ClientBean {

        @Autowired
        private ObjectProvider<PrototypeBean> prototypeBeanProvider;

        public int logic() {
            PrototypeBean prototypeBean = prototypeBeanProvider.getObject();
            prototypeBean.addCount();
            return prototypeBean.getCount();
        }

        @PostConstruct
        public void init() {
            System.out.println("ClientBean.init");
        }

        @PreDestroy
        public void destroy() {
            System.out.println("ClientBean.destroy");
        }
    }

}
