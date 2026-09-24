package com.miniaml.util;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ListUtilTest {
    @Test
    void isEmpty_emptyList_returnsTrue() {
        assertTrue(ListUtil.isEmpty(new ArrayList<>()));
    }

    @Test
    void isEmpty_null_returnsTrue() {
        assertTrue(ListUtil.isEmpty(null));
    }

    @Test
    void isEmpty_nonEmptyList_returnsFalse() {
        List<String> list = List.of("a", "b");
        assertFalse(ListUtil.isEmpty(list));
    }

    @Test
    void getFirst_emptyList_returnsNull() {
        assertNull(ListUtil.getFirst(new ArrayList<>()));
    }

    @Test
    void getFirst_nonEmptyList_returnsFirstElement() {
        List<String> list = List.of("a", "b");
        assertEquals("a", ListUtil.getFirst(list));
    }

    @Test
    void map_convertsEachElement() {
        // 输入 List.of("a", "bb", "ccc")
        List<String> list = List.of("a", "bb", "ccc");
        // 用 map 把每个字符串转成它的长度
        List<Integer> lengths = ListUtil.map(list, s -> s.length());
        // 断言结果等于 List.of(1, 2, 3)
        assertEquals(List.of(1, 2, 3), lengths);
    }

    @Test
    void map_emptyList_returnsEmptyList() {
        // 空列表 map（转成长度）
        List<String> list = new ArrayList<>();
        List<Integer> lengths = ListUtil.map(list, s -> s.length());
        // 断言结果为空列表
        assertEquals(new ArrayList<>(), lengths);
    }

}
