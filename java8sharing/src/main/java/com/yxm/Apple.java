package com.yxm;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Apple {
    /**
     * 颜色
     */
    private String color;
    /**
     * 重量
     */
    private long weight;
}
