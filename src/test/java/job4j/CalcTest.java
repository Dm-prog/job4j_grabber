
package job4j;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class CalcTest {

    Calc calc = new Calc();

    @Test
    public void resSum() {
        assertThat(5, is(calc.res(2, 3)));
    }
}
