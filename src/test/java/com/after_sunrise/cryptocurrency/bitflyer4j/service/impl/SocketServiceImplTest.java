package com.after_sunrise.cryptocurrency.bitflyer4j.service.impl;

import com.after_sunrise.cryptocurrency.bitflyer4j.TestModule;
import com.after_sunrise.cryptocurrency.bitflyer4j.core.ExecutorFactory;
import com.after_sunrise.cryptocurrency.bitflyer4j.core.impl.GsonProvider;
import com.after_sunrise.cryptocurrency.bitflyer4j.service.RealtimeListener;
import com.google.common.io.Resources;
import com.google.gson.Gson;
import io.socket.client.Socket;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.net.URL;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static com.google.common.util.concurrent.MoreExecutors.newDirectExecutorService;
import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME;
import static java.util.Collections.singletonList;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

/**
 * @author takanori.takase
 * @version 0.0.4
 */
public class SocketServiceImplTest {

    private static final DateTimeFormatter DTF = ISO_LOCAL_DATE_TIME.withZone(ZoneId.of("GMT"));

    private SocketServiceImpl target;

    private TestModule module;

    private Socket socket;

    private RealtimeListener l1;

    private RealtimeListener l2;

    private RealtimeListener l3;

    @BeforeMethod
    public void setUp() throws Exception {

        module = new TestModule();
        module.putMock(Gson.class, new GsonProvider().get());
        when(module.getMock(ExecutorFactory.class).get(any(Class.class))).thenReturn(newDirectExecutorService());

        socket = module.getMock(Socket.class);

        l1 = mock(RealtimeListener.class);
        l2 = mock(RealtimeListener.class);
        l3 = mock(RealtimeListener.class);

        target = spy(new SocketServiceImpl(module.createInjector()));

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

    private String load(String name) throws IOException {

        URL url = Resources.getResource("json/SOCKET_" + name + ".json");

        return new String(Resources.toByteArray(url));

    }

    @Test
    public void testSubscribeBoard() throws Exception {


        doAnswer(i -> {

            assertEquals(i.getArgumentAt(0, String.class), "lightning_board_");

            assertEquals(i.getArgumentAt(1, List.class), singletonList("FOO"));

            @SuppressWarnings("unchecked")
            BiConsumer<String, String> c = (BiConsumer<String, String>) i.getArgumentAt(2, BiConsumer.class);

            c.accept("BAR", load("BOARD"));

            return CompletableFuture.completedFuture(singletonList("HOGE"));

        }).when(target).subscribe(any(), any(), any());

        assertTrue(target.addListener(l1).get());
        assertTrue(target.addListener(l2).get());
        assertTrue(target.addListener(l3).get());
        doThrow(new RuntimeException("test")).when(l2).onBoards(any(), any());

        CompletableFuture<List<String>> result = target.subscribeBoard(singletonList("FOO"));
        assertTrue(result.isDone());
        assertEquals(result.get(), singletonList("HOGE"));

        verify(l1).onBoards(eq("BAR"), any());
        verify(l2).onBoards(eq("BAR"), any());
        verify(l3).onBoards(eq("BAR"), any());

    }

    @Test
    public void testSubscribeBoardSnapshot() throws Exception {


        doAnswer(i -> {

            assertEquals(i.getArgumentAt(0, String.class), "lightning_board_snapshot_");

            assertEquals(i.getArgumentAt(1, List.class), singletonList("FOO"));

            @SuppressWarnings("unchecked")
            BiConsumer<String, String> c = (BiConsumer<String, String>) i.getArgumentAt(2, BiConsumer.class);

            c.accept("BAR", load("BOARDS"));

            return CompletableFuture.completedFuture(singletonList("HOGE"));

        }).when(target).subscribe(any(), any(), any());

        assertTrue(target.addListener(l1).get());
        assertTrue(target.addListener(l2).get());
        assertTrue(target.addListener(l3).get());
        doThrow(new RuntimeException("test")).when(l2).onBoardsSnapshot(any(), any());

        CompletableFuture<List<String>> result = target.subscribeBoardSnapshot(singletonList("FOO"));
        assertTrue(result.isDone());
        assertEquals(result.get(), singletonList("HOGE"));

        verify(l1).onBoardsSnapshot(eq("BAR"), any());
        verify(l2).onBoardsSnapshot(eq("BAR"), any());
        verify(l3).onBoardsSnapshot(eq("BAR"), any());

    }

    @Test
    public void testSubscribeTick() throws Exception {


        doAnswer(i -> {

            assertEquals(i.getArgumentAt(0, String.class), "lightning_ticker_");

            assertEquals(i.getArgumentAt(1, List.class), singletonList("FOO"));

            @SuppressWarnings("unchecked")
            BiConsumer<String, String> c = (BiConsumer<String, String>) i.getArgumentAt(2, BiConsumer.class);

            c.accept("BAR", load("TICKER"));

            return CompletableFuture.completedFuture(singletonList("HOGE"));

        }).when(target).subscribe(any(), any(), any());

        assertTrue(target.addListener(l1).get());
        assertTrue(target.addListener(l2).get());
        assertTrue(target.addListener(l3).get());
        doThrow(new RuntimeException("test")).when(l2).onTicks(any(), any());

        CompletableFuture<List<String>> result = target.subscribeTick(singletonList("FOO"));
        assertTrue(result.isDone());
        assertEquals(result.get(), singletonList("HOGE"));

        verify(l1).onTicks(eq("BAR"), any());
        verify(l2).onTicks(eq("BAR"), any());
        verify(l3).onTicks(eq("BAR"), any());

    }

    @Test
    public void testSubscribeExecution() throws Exception {


        doAnswer(i -> {

            assertEquals(i.getArgumentAt(0, String.class), "lightning_executions_");

            assertEquals(i.getArgumentAt(1, List.class), singletonList("FOO"));

            @SuppressWarnings("unchecked")
            BiConsumer<String, String> c = (BiConsumer<String, String>) i.getArgumentAt(2, BiConsumer.class);

            c.accept("BAR", load("EXEC"));

            return CompletableFuture.completedFuture(singletonList("HOGE"));

        }).when(target).subscribe(any(), any(), any());

        assertTrue(target.addListener(l1).get());
        assertTrue(target.addListener(l2).get());
        assertTrue(target.addListener(l3).get());
        doThrow(new RuntimeException("test")).when(l2).onExecutions(any(), any());

        CompletableFuture<List<String>> result = target.subscribeExecution(singletonList("FOO"));
        assertTrue(result.isDone());
        assertEquals(result.get(), singletonList("HOGE"));

        verify(l1).onExecutions(eq("BAR"), any());
        verify(l2).onExecutions(eq("BAR"), any());
        verify(l3).onExecutions(eq("BAR"), any());

    }

}
