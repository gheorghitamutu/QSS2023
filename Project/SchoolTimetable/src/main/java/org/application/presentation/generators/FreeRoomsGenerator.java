package org.application.presentation.generators;

import org.application.domain.models.Room;
import org.application.domain.models.Timeslot;

import java.util.HashMap;
import java.util.Map;

public class FreeRoomsGenerator extends BaseGenerator{
    private final Map<Room, Map<Integer, Map<Timeslot.Day, Boolean>>> freeRooms;
    private final Map<Timeslot.Day, Integer> dayDecoder;
    public FreeRoomsGenerator(String generationDateString, Map<String, Map<Timeslot.Day, StringBuilder>> timetablesDays, Map<Room, Map<Integer, Map<Timeslot.Day, Boolean>>> freeRooms){
        super(generationDateString, timetablesDays);
        this.freeRooms = freeRooms;
        this.dayDecoder = generateDayDecoder();
    }

    private Map<Timeslot.Day, Integer> generateDayDecoder(){
        Map<Timeslot.Day, Integer> decoder = new HashMap<>();
        Timeslot.Day[] daysArray = Timeslot.Day.class.getEnumConstants();
        int index = 0;

        for (Timeslot.Day day : daysArray){
            decoder.put(day, index);
            index += 1;
        }

        return decoder;
    }

    public void generate(){
        System.out.println(dayDecoder);

        String freeRoomsData = utils.getBaseTemplateData("free_rooms");

        for (Map.Entry<Room, Map<Integer, Map<Timeslot.Day, Boolean>>> mapEntry1 : freeRooms.entrySet()){
            Room room = mapEntry1.getKey();
            Map<Integer, Map<Timeslot.Day, Boolean>> hoursMap = mapEntry1.getValue();

            for (Map.Entry<Integer, Map<Timeslot.Day, Boolean>> mapEntry2 : hoursMap.entrySet())
            {
                int hour = mapEntry2.getKey();
                Map<Timeslot.Day, Boolean> daysMap = mapEntry2.getValue();

                for (Map.Entry<Timeslot.Day, Boolean> mapEntry3 : daysMap.entrySet()){
                    Timeslot.Day day = mapEntry3.getKey();
                    Boolean isFree = mapEntry3.getValue();

                    int hourIndex = hour - 8;
                }
            }
        }
    }
}
