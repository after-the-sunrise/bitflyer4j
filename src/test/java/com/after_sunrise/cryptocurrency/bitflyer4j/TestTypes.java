package com.after_sunrise.cryptocurrency.bitflyer4j;

import com.after_sunrise.cryptocurrency.bitflyer4j.core.*;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertSame;

/**
 * @author takanori.takase
 * @version 0.0.1
 */
public class TestTypes {

    @Test
    public void test() throws ReflectiveOperationException {

        test(BoardStatusType.class);
        test(ConditionType.class);
        test(ConfigurationType.class);
        test(DepositStatusType.class);
        test(KeyType.class);
        test(MethodType.class);
        test(ParentType.class);
        test(PathType.class);
        test(SideType.class);
        test(StateType.class);
        test(StatusType.class);
        test(TimeInForceType.class);
        test(WithdrawalStatusType.class);

    }

    private <E extends Enum<E>> void test(Class<E> c) throws ReflectiveOperationException {

        for (E type : c.getEnumConstants()) {

            assertEquals(type.toString(), type.name());

            assertSame(Enum.valueOf(c, type.name()), type);

            assertSame(c.getDeclaredMethod("valueOf", String.class).invoke(c, type.name()), type);

        }

    }

}
