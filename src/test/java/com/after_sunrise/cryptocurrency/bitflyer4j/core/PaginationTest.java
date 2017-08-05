package com.after_sunrise.cryptocurrency.bitflyer4j.core;

import com.after_sunrise.cryptocurrency.bitflyer4j.core.Pagination.PaginationBuilder;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

/**
 * @author takanori.takase
 * @version 0.0.1
 */
public class PaginationTest {

    @Test
    public void testBuilder() throws Exception {

        PaginationBuilder b = Pagination.builder();
        assertEquals(b.toString(), "Pagination.PaginationBuilder(count=null, before=null, after=null)");

        Pagination p0 = b.build();

        assertNull(p0.getCount());
        assertNull(p0.getBefore());
        assertNull(p0.getAfter());

        assertTrue(p0.equals(p0));
        assertTrue(p0.equals(b.build()));
        assertFalse(p0.equals(null));
        assertFalse(p0.equals(new Object()));
        assertFalse(p0.equals(new String()));

        assertEquals(b.build().hashCode(), p0.hashCode());
        assertEquals(b.build().toString(), p0.toString());

        Pagination p1 = b.count(1).before(2).after(3).build();
        assertFalse(p0.equals(p1));
        assertFalse(p1.equals(p0));
        assertEquals(b.build().hashCode(), p1.hashCode());

        p1 = b.count(1).before(null).after(null).build();
        assertFalse(p0.equals(p1));
        assertFalse(p1.equals(p0));
        assertEquals(b.build().hashCode(), p1.hashCode());

        p1 = b.count(null).before(2).after(null).build();
        assertFalse(p0.equals(p1));
        assertFalse(p1.equals(p0));
        assertEquals(b.build().hashCode(), p1.hashCode());

        p1 = b.count(null).before(null).after(3).build();
        assertFalse(p0.equals(p1));
        assertFalse(p1.equals(p0));
        assertEquals(b.build().hashCode(), p1.hashCode());

    }

}