package com.after_sunrise.cryptocurrency.bitflyer4j.service.impl;

import com.after_sunrise.cryptocurrency.bitflyer4j.TestModule;
import com.after_sunrise.cryptocurrency.bitflyer4j.core.ExecutorFactory;
import com.after_sunrise.cryptocurrency.bitflyer4j.core.SideType;
import com.after_sunrise.cryptocurrency.bitflyer4j.core.impl.GsonProvider;
import com.after_sunrise.cryptocurrency.bitflyer4j.entity.Board;
import com.after_sunrise.cryptocurrency.bitflyer4j.entity.Execution;
import com.after_sunrise.cryptocurrency.bitflyer4j.entity.Tick;
import com.after_sunrise.cryptocurrency.bitflyer4j.service.RealtimeListener;
import com.google.common.io.Resources;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.pubnub.api.PubNub;
import com.pubnub.api.builder.SubscribeBuilder;
import com.pubnub.api.builder.UnsubscribeBuilder;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.pubsub.PNMessageResult;
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult;
import org.mockito.InOrder;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static com.google.common.util.concurrent.MoreExecutors.newDirectExecutorService;
import static com.google.gson.JsonNull.INSTANCE;
import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME;
import static java.util.Arrays.asList;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

/**
 * @author takanori.takase
 * @version 0.0.4
 */
public class RealtimeServiceImplTest {

    private static final DateTimeFormatter DTF = ISO_LOCAL_DATE_TIME.withZone(ZoneId.of("GMT"));

    private RealtimeServiceImpl target;

    private TestModule module;

    private Gson gson;

    private PubNub pubNub;

    private RealtimeListener l1;

    private RealtimeListener l2;

    private RealtimeListener l3;

    @BeforeMethod
    public void setUp() throws Exception {

        module = new TestModule();
        module.putMock(Gson.class, new GsonProvider().get());
        when(module.getMock(ExecutorFactory.class).get(any(Class.class))).thenReturn(newDirectExecutorService());

        gson = module.getMock(Gson.class);
        pubNub = module.getMock(PubNub.class);

        SubscribeBuilder sb = module.getMock(SubscribeBuilder.class);
        when(sb.channels(anyListOf(String.class))).thenReturn(sb);
        when(pubNub.subscribe()).thenReturn(sb);

        UnsubscribeBuilder ub = module.getMock(UnsubscribeBuilder.class);
        when(ub.channels(anyListOf(String.class))).thenReturn(ub);
        when(pubNub.unsubscribe()).thenReturn(ub);

        l1 = mock(RealtimeListener.class);
        l2 = mock(RealtimeListener.class);
        l3 = mock(RealtimeListener.class);

        target = new RealtimeServiceImpl(module.createInjector());

    }


    @Test
    public void testListener() throws ExecutionException, InterruptedException {

        class ConsumerTest implements Consumer<RealtimeListener> {
            @Override
            public void accept(RealtimeListener realtimeListener) {
                if (realtimeListener == l2) {
                    throw new RuntimeException("test");
                }
                realtimeListener.onTicks("hoge", Collections.emptyList());
            }
        }

        Consumer<RealtimeListener> c = spy(new ConsumerTest());

        {
            target.forEach(c);
            verify(c, never()).accept(any(RealtimeListener.class));
        }

        {
            // Add
            assertTrue(target.addListener(l1).get());
            assertTrue(target.addListener(l2).get());
            assertTrue(target.addListener(l3).get());
            assertFalse(target.addListener(null).get());

            // Duplicate
            assertFalse(target.addListener(l2).get());

            target.forEach(c);
            verify(c, times(1)).accept(same(l1));
            verify(c, times(1)).accept(same(l2));
            verify(c, times(1)).accept(same(l3));
        }

        {
            // Remove
            assertTrue(target.removeListener(l2).get());

            // Unknown
            assertFalse(target.removeListener(l2).get());
            assertFalse(target.removeListener(null).get());

            target.forEach(c);
            verify(c, times(2)).accept(same(l1));
            verify(c, times(2)).accept(same(l3));
        }

        verifyNoMoreInteractions(c);

    }

    private JsonElement load(String name, Class<? extends JsonElement> cls) throws IOException {

        URL url = Resources.getResource("json/PUBNUB_" + name + ".json");

        String json = new String(Resources.toByteArray(url));

        return gson.fromJson(json, cls);

    }

    @Test
    public void testSubscribeBoard() throws Exception {

        assertTrue(target.addListener(l1).get());

        doAnswer(invocation -> {

            List<?> values = invocation.getArgumentAt(0, List.class);
            assertEquals(values.size(), 1);

            Board value = (Board) values.get(0);
            assertEquals(value.getMid(), new BigDecimal("359702.0"));

            List<Board.Quote> bids = value.getBid();
            assertEquals(bids.size(), 2);
            assertEquals(bids.get(0).getPrice(), new BigDecimal("359624.0"));
            assertEquals(bids.get(0).getSize(), new BigDecimal("0.01100081"));
            assertEquals(bids.get(1).getPrice(), new BigDecimal("339000.0"));
            assertEquals(bids.get(1).getSize(), new BigDecimal("4.03"));

            List<Board.Quote> asks = value.getBid();
            assertEquals(asks.size(), 2);
            assertEquals(asks.get(0).getPrice(), new BigDecimal("359781.0"));
            assertEquals(asks.get(0).getSize(), new BigDecimal("1.0"));
            assertEquals(asks.get(1).getPrice(), new BigDecimal("360077.0"));
            assertEquals(asks.get(1).getSize(), new BigDecimal("0.02"));

            return null;

        }).when(l1).onBoards(anyString(), any(Board.class));

        List<String> ids = target.subscribeBoard(Arrays.asList("A")).get();
        assertEquals(ids.size(), 1);
        assertTrue(ids.contains("A"));

        target.message(pubNub, PNMessageResult.builder() //
                .channel("lightning_board_snapshot_A") //
                .message(load("BOARD", JsonObject.class)).build());

        verify(l1).onBoards(anyString(), any(Board.class));

        ids = target.unsubscribeBoard(Arrays.asList("A")).get();
        assertEquals(ids.size(), 1);
        assertTrue(ids.contains("A"));

    }

    @Test
    public void testSubscribeTick() throws Exception {

        assertTrue(target.addListener(l1).get());

        doAnswer(invocation -> {

            List<?> values = invocation.getArgumentAt(0, List.class);
            assertEquals(values.size(), 1);

            Tick value = (Tick) values.get(0);
            assertEquals(value.getProduct(), "BTC_JPY");
            assertEquals(value.getTimestamp(), ZonedDateTime.parse("2017-08-05T21:50:04.8655828", DTF));
            assertEquals(value.getId(), (Long) 2217577L);
            assertEquals(value.getBestBidPrice(), new BigDecimal("359624.0"));
            assertEquals(value.getBestAskPrice(), new BigDecimal("359781.0"));
            assertEquals(value.getBestBidSize(), new BigDecimal("0.01100081"));
            assertEquals(value.getBestAskSize(), new BigDecimal("1.0"));
            assertEquals(value.getTotalBidDepth(), new BigDecimal("5337.788063450000"));
            assertEquals(value.getTotalAskDepth(), new BigDecimal("1061.553996180000"));
            assertEquals(value.getTradePrice(), new BigDecimal("359780.0"));
            assertEquals(value.getTradeVolume(), new BigDecimal("25208.619896580000"));
            assertEquals(value.getProductVolume(), new BigDecimal("25208.619896580000"));

            return null;

        }).when(l1).onTicks(anyString(), anyListOf(Tick.class));

        List<String> ids = target.subscribeTick(Arrays.asList("A")).get();
        assertEquals(ids.size(), 1);
        assertTrue(ids.contains("A"));

        target.message(pubNub, PNMessageResult.builder() //
                .channel("lightning_ticker_A") //
                .message(load("TICKER", JsonObject.class)).build());

        verify(l1).onTicks(anyString(), anyListOf(Tick.class));

        ids = target.unsubscribeTick(Arrays.asList("A")).get();
        assertEquals(ids.size(), 1);
        assertTrue(ids.contains("A"));

    }

    @Test
    public void testSubscribeExecution() throws Exception {

        assertTrue(target.addListener(l1).get());

        doAnswer(invocation -> {

            List<?> values = invocation.getArgumentAt(0, List.class);
            assertEquals(values.size(), 1);

            Execution value = (Execution) values.get(0);
            assertEquals(value.getId(), (Long) 37415288L);
            assertEquals(value.getSide(), SideType.BUY);
            assertEquals(value.getPrice(), new BigDecimal("359956.0"));
            assertEquals(value.getSize(), new BigDecimal("0.015"));
            assertEquals(value.getTimestamp(), ZonedDateTime.parse("2017-08-05T21:51:37.6940477", DTF));
            assertEquals(value.getBuyOrderId(), "JRF20170805-215136-338070");
            assertEquals(value.getSellOrderId(), "JRF20170805-215134-449447");

            return null;

        }).when(l1).onExecutions(anyString(), anyListOf(Execution.class));

        List<String> ids = target.subscribeExecution(Arrays.asList("A")).get();
        assertEquals(ids.size(), 1);
        assertTrue(ids.contains("A"));

        target.message(pubNub, PNMessageResult.builder() //
                .channel("lightning_executions_A") //
                .message(load("EXEC", JsonArray.class)).build());

        verify(l1).onExecutions(anyString(), anyListOf(Execution.class));

        ids = target.unsubscribeExecution(Arrays.asList("A")).get();
        assertEquals(ids.size(), 1);
        assertTrue(ids.contains("A"));

    }

    @Test
    public void testSubscribeUnsubscribe() throws Exception {

        String prefix = "lightning_topic_";

        BiConsumer<String, JsonElement> c = (channel, json) -> {
        };

        List<String> ids = target.subscribe(prefix, asList("A", "B"), c).get();
        assertEquals(ids.size(), 2);
        assertTrue(ids.contains("A"));
        assertTrue(ids.contains("B"));

        ids = target.subscribe(prefix, asList("B", "C"), c).get();
        assertEquals(ids.size(), 1);
        assertTrue(ids.contains("C"));

        ids = target.subscribe(prefix, asList("C"), c).get();
        assertEquals(ids.size(), 0);

        ids = target.unsubscribe(prefix, asList("B", "D")).get();
        assertEquals(ids.size(), 1);
        assertTrue(ids.contains("B"));

        ids = target.unsubscribe(prefix, asList("B")).get();
        assertEquals(ids.size(), 0);

        InOrder inOrder = inOrder(module.getMock(SubscribeBuilder.class), module.getMock(UnsubscribeBuilder.class));

        inOrder.verify(module.getMock(SubscribeBuilder.class)).channels(asList(prefix + "A", prefix + "B"));
        inOrder.verify(module.getMock(SubscribeBuilder.class)).execute();

        inOrder.verify(module.getMock(SubscribeBuilder.class)).channels(asList(prefix + "C"));
        inOrder.verify(module.getMock(SubscribeBuilder.class)).execute();

        inOrder.verify(module.getMock(UnsubscribeBuilder.class)).channels(asList(prefix + "B"));
        inOrder.verify(module.getMock(UnsubscribeBuilder.class)).execute();

        inOrder.verifyNoMoreInteractions();

    }

    @Test
    public void testStatus() throws Exception {

        target.status(pubNub, PNStatus.builder().build());

    }

    @Test
    public void testPresence() throws Exception {

        target.presence(pubNub, PNPresenceEventResult.builder().build());

    }

    @Test
    public void testMessage() throws Exception {

        target.message(pubNub, PNMessageResult.builder().message(INSTANCE).build());

    }

}