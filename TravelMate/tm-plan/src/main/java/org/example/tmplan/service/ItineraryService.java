package org.example.tmplan.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.tmplan.domain.po.Itinerary;
import org.example.tmplan.domain.dto.ItineraryDTO;
import org.example.tmplan.domain.vo.ItineraryVo;

import java.util.List;

public interface ItineraryService extends IService<Itinerary> {
    Integer addItinerary(Itinerary itinerary);
    List<Itinerary> getItinerariesByUserID(Integer userID);
    ItineraryVo getItineraryByID(Integer ID);
    Integer modifyItinerary(Itinerary itinerary);
    boolean deleteItinerary(Integer id);

    ItineraryDTO getItineraryDetails(Integer id);
    List<Integer> getEventByUserID(Integer userID);

}
