package org.example.tmplan.service;

import java.time.LocalDate;

/**
 * 行程推荐服务接口。
 * 提供基于行程信息生成个性化旅行推荐的功能。
 */
public interface IntRecService {

    /**
     * 根据行程 ID 获取智能推荐的行程内容。
     * 推荐结果通常由大模型（如 GLM）生成，基于地点、时间等参数。
     *
     * @param id 行程 ID
     * @return 推荐文本结果（按日程规划格式）
     */
    String getIntRec(Integer id);
}
