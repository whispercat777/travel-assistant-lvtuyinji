package org.example.tmplan.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.tmplan.pojo.Event;
import org.example.tmplan.pojo.Itinerary;
import org.example.tmplan.pojo.dto.ItineraryDTO;
import org.example.tmplan.pojo.vo.ItineraryVo;

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
