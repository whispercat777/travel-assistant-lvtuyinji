package org.example.tmplan.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 预算视图对象（VO）。
 * 用于在接口层展示或接收预算数据，通常为简化后的数据结构。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Budget {

    /**
     * 预算记录 ID。
     */
    private Integer id;

    /**
     * 所属事件 ID。
     */
    private Integer eveID;

    /**
     * 预算金额（单位：元）。
     */
    private Float money;
}
