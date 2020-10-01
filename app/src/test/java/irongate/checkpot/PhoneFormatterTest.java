package irongate.checkpot;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import irongate.checkpot.view.screens.player.registration.PhoneFormatter;

public class PhoneFormatterTest {

    PhoneFormatter formatter;

    @Before
    public void init() {
        formatter = new PhoneFormatter();
    }

    @Test
    public  void checkOne() {
        formatter.beforeTextChanged(0,0);
        formatter.add("1", 1);
        Assert.assertEquals("+7(1", formatter.getPhone());
    }

    @Test
    public  void checkThree() {
        formatter.beforeTextChanged(0,0);
        formatter.add("123", 1);
        Assert.assertEquals("+7(123", formatter.getPhone());
    }

    @Test
    public  void checkFour() {
        formatter.beforeTextChanged(0,0);
        formatter.add("1234", 1);
        Assert.assertEquals("+7(123)4", formatter.getPhone());
    }

    @Test
    public  void checkDelete0() {
        formatter.beforeTextChanged(1,0);
        formatter.delete("+(", 2);
        Assert.assertEquals("+7(", formatter.getPhone());
    }

}
