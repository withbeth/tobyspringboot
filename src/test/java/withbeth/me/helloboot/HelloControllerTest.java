package withbeth.me.helloboot;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.Test;

class HelloControllerTest {

    @Test
    void ok() {
        HelloController helloController = new HelloController(name -> name);
        assertThat(helloController.hello("withbeth")).isEqualTo("withbeth");
    }

    @Test
    void failWhenNullOrEmpty() {
        HelloController helloController = new HelloController(name -> name);
        assertAll(
            () -> assertThatThrownBy(() -> helloController.hello(null))
                .isInstanceOf(IllegalArgumentException.class),
            () -> assertThatThrownBy(() -> helloController.hello(""))
                .isInstanceOf(IllegalArgumentException.class)
        );
    }

}
