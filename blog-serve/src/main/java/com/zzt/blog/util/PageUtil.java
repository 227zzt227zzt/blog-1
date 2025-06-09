package com.zzt.blog.util;

import lombok.Data;

import java.util.List;

/**
 * @author 227
 */
@Data
public class PageUtil<T> {
        private List<T> records;
        private long total;
        private long size;
        private long current;

        /**
         * 将List转换为分页结果
         * @param list 原始数据列表
         * @param current 当前页码 (从1开始)
         * @param size 每页大小
         */
        public static <T> PageUtil<T> listToPage(List<T> list, long current, long size) {
            PageUtil<T> page = new PageUtil<>();
            page.setCurrent(current);
            page.setSize(size);
            page.setTotal(list.size());

            // 计算分页起始位置
            int fromIndex = (int) ((current - 1) * size);
            // 确保不越界
            fromIndex = Math.max(fromIndex, 0);
            int toIndex = (int) Math.min(fromIndex + size, list.size());

            page.setRecords(list.subList(fromIndex, toIndex));
            return page;
        }
}
