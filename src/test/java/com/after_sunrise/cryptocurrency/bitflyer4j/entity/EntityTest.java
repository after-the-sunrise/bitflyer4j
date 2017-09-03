package com.after_sunrise.cryptocurrency.bitflyer4j.entity;

import com.after_sunrise.cryptocurrency.bitflyer4j.core.*;
import com.after_sunrise.cryptocurrency.bitflyer4j.entity.impl.*;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.StringUtils;
import org.mockito.Mockito;
import org.testng.annotations.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.testng.Assert.*;

/**
 * Testing auto-generated codes for coverage.
 *
 * @author takanori.takase
 * @version 0.0.3
 */
public class EntityTest {

    private static final Map<Class<?>, Object> ARGS = new HashMap<>();

    static {
        put(EntityTest.class.getSimpleName());
        put(Boolean.TRUE);
        put(Integer.MAX_VALUE);
        put(Long.MAX_VALUE);
        put(BigDecimal.TEN);
        put(Instant.now());
        put(ZonedDateTime.now());
        put(Mockito.mock(List.class));
        put(ConditionType.MARKET);
        put(ConfigurationType.VERSION);
        put(DepositStatusType.COMPLETED);
        put(KeyType.VERSION);
        put(MethodType.GET);
        put(ParentType.OCO);
        put(PathType.HEALTH);
        put(SideType.BUY);
        put(StateType.ACTIVE);
        put(StatusType.NORMAL);
        put(TimeInForceType.GTC);
        put(WithdrawalStatusType.COMPLETED);
    }

    private static void put(Object o) {
        ARGS.put(o.getClass(), o);
    }

    @Test
    public void test() throws ReflectiveOperationException {

        test(OrderCancel.Request.class);
        test(OrderCreate.Request.class);
        test(OrderList.Request.class);
        test(Pagination.class);
        test(ParentCancel.Request.class);
        test(ParentCreate.Request.class);
        test(ParentCreate.Request.Parameter.class);
        test(ParentDetail.Request.class);
        test(ParentList.Request.class);
        test(ProductCancel.Request.class);
        test(TradeCollateral.Request.class);
        test(TradeCommission.Request.class);
        test(TradeExecution.Request.class);
        test(TradePosition.Request.class);
        test(Withdraw.Request.class);

        test(AddressImpl.class);
        test(BalanceImpl.class);
        test(BankImpl.class);
        test(BoardImpl.class);
        test(BoardImpl.QuoteImpl.class);
        test(ChatImpl.class);
        test(CoinInImpl.class);
        test(CoinOutImpl.class);
        test(CollateralImpl.class);
        test(DepositImpl.class);
        test(ExecutionImpl.class);
        test(MarginImpl.class);
        test(OrderCancelResponse.class);
        test(OrderCreateResponse.class);
        test(OrderListResponse.class);
        test(ParentCancelResponse.class);
        test(ParentCreateResponse.class);
        test(ParentDetailResponse.class);
        test(ParentDetailResponse.ParentDetailParameter.class);
        test(ParentListResponse.class);
        test(ProductCancelResponse.class);
        test(ProductImpl.class);
        test(StatusImpl.class);
        test(TickImpl.class);
        test(TradeCollateralResponse.class);
        test(TradeCommissionResponse.class);
        test(TradeExecutionResponse.class);
        test(TradePositionResponse.class);
        test(WithdrawalImpl.class);
        test(WithdrawResponse.class);

    }

    private <E extends Entity> void test(Class<E> clazz) throws ReflectiveOperationException {

        try {

            clazz.getMethod("builder");

            testBuilder(clazz);

        } catch (ReflectiveOperationException e) {
            // Ignore
        }

        for (Constructor<?> c : clazz.getConstructors()) {

            Object[] args = new Object[c.getParameterTypes().length];

            Object entity = testGet(c.newInstance(args));

            assertNotNull(entity.toString());

            assertEquals(entity.hashCode(), entity.hashCode());

            assertTrue(entity.equals(entity));
            assertFalse(entity.equals(clazz));
            assertFalse(entity.equals(null));

        }

    }

    private void testBuilder(Class<?> clazz) throws ReflectiveOperationException {

        Object builder = clazz.getMethod("builder").invoke(clazz);
        assertNotNull(builder.toString());
        assertEquals(builder.hashCode(), builder.hashCode());
        assertTrue(builder.equals(builder));

        Method build = builder.getClass().getMethod("build");

        Object emptyEntity = testGet(build.invoke(builder));
        assertNotNull(emptyEntity.toString());
        assertEquals(emptyEntity.hashCode(), emptyEntity.hashCode());
        assertTrue(emptyEntity.equals(emptyEntity));
        assertFalse(emptyEntity.equals(clazz));
        assertFalse(emptyEntity.equals(null));

        for (Field field : clazz.getDeclaredFields()) {

            if (!field.isAnnotationPresent(SerializedName.class)) {
                continue;
            }

            for (Method method : builder.getClass().getMethods()) {

                if (method.getReturnType() != builder.getClass()) {
                    continue;
                }

                method.invoke(builder, ARGS.get(method.getParameterTypes()[0]));

            }

        }

        Object filledEntity = testGet(build.invoke(builder));
        assertNotNull(filledEntity.toString());
        assertEquals(filledEntity.hashCode(), filledEntity.hashCode());
        assertTrue(filledEntity.equals(filledEntity));
        assertFalse(filledEntity.equals(clazz));
        assertFalse(filledEntity.equals(null));

    }

    private Object testGet(Object o) throws ReflectiveOperationException {

        for (Method m : o.getClass().getMethods()) {

            if (m.getParameterCount() != 0) {
                continue;
            }

            if (!StringUtils.startsWith(m.getName(), "get")) {
                continue;
            }

            m.invoke(o);

        }

        return o;

    }

}