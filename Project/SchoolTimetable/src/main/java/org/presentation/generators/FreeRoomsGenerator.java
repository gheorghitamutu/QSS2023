package org.presentation.generators;

import org.domain.models.Room;
import org.domain.models.Timeslot;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class FreeRoomsGenerator extends BaseGenerator{
    @NotNull(message = "Free rooms map must not be null")
    private final Map<Room, Map<Integer, Map<Timeslot.Day, Boolean>>> freeRooms;
    @NotEmpty(message = "Day decoder map must not be empty")
    private final Map<Timeslot.Day, Integer> dayDecoder;
    @NotNull(message = "Lists data map must not be null")
    private final Map<String, String> listsData;

    public FreeRoomsGenerator(
            @NotBlank(message = "Generation string must not be blank")
            String generationDateString,
            @NotNull(message = "Days data map must not be null")
            Map<String, Map<Timeslot.Day, StringBuilder>> timetablesDays,
            @NotNull(message = "Free rooms map must not be null")
            Map<Room, Map<Integer, Map<Timeslot.Day, Boolean>>> freeRooms,
            @NotNull(message = "Lists data map must not be null")
            Map<String, String> listsData){
        super(generationDateString, timetablesDays);
        this.freeRooms = freeRooms;
        this.dayDecoder = generateDayDecoder();
        this.listsData = listsData;
    }

    @NotEmpty(message = "Day decoder map must not be empty")
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
        String freeRoomsData = utils.getBaseTemplateData("free_rooms");
        freeRoomsData = freeRoomsData.replace("$generation_date", this.generationDateString);
        int roomNumber = -1;

        StringBuilder tableEntries = new StringBuilder();

        for (Map.Entry<Room, Map<Integer, Map<Timeslot.Day, Boolean>>> mapEntry1 : freeRooms.entrySet()){
            roomNumber += 1;
            Room room = mapEntry1.getKey();
            String roomData = utils.getBaseTemplateData("atomics" + this.separator + "room_entry");

            if (roomNumber % 3 == 0){
                tableEntries.append("</tr><tr>");
            }

            roomData = roomData.replace("$room_name", Objects.requireNonNull(room.getName(), "Room name should not be null."));
            
            Map<Integer, Map<Timeslot.Day, Boolean>> hoursMap = mapEntry1.getValue();

            for (Map.Entry<Integer, Map<Timeslot.Day, Boolean>> mapEntry2 : hoursMap.entrySet())
            {
                int hour = mapEntry2.getKey();
                Map<Timeslot.Day, Boolean> daysMap = mapEntry2.getValue();

                for (Map.Entry<Timeslot.Day, Boolean> mapEntry3 : daysMap.entrySet()){
                    Timeslot.Day day = mapEntry3.getKey();
                    Boolean isFree = mapEntry3.getValue();

                    int hourIndex = hour - 8;
                    int dayIndex = dayDecoder.get(day);
                    String colorIndex = "$" + hourIndex + dayIndex + "c";

                    if (isFree){
                        roomData = roomData.replace(colorIndex, "#66FF66");
                    }
                    else {
                        roomData = roomData.replace(colorIndex, "#FF0000");
                    }
                }
            }

            tableEntries.append(roomData);
        }

        freeRoomsData = freeRoomsData.replace("$table_entries", tableEntries.toString());
        this.listsData.put("free_rooms", freeRoomsData);
    }
}
