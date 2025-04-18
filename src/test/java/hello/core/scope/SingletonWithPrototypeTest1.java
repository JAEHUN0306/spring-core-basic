package hello.core.scope;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
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
        PrototypeBean prototypeBean = bean1.getPrototypeBean();
        System.out.println("prototypeBean = " + prototypeBean);
        ClientBean bean2 = ac.getBean(ClientBean.class);

        int logic2 = bean2.logic();
        PrototypeBean prototypeBean2 = bean2.getPrototypeBean();
        System.out.println("prototypeBean2 = " + prototypeBean2);

        Assertions.assertThat(logic1).isEqualTo(1);
        Assertions.assertThat(logic2).isEqualTo(2);
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
        private final PrototypeBean prototypeBean;

        public PrototypeBean getPrototypeBean() {
            return prototypeBean;
        }

        public ClientBean(PrototypeBean prototypeBean) {
            this.prototypeBean = prototypeBean;
        }

        public int logic() {
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
