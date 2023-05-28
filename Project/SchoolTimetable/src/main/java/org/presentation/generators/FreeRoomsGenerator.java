package org.presentation.generators;

import org.domain.models.Room;
import org.domain.models.Timeslot;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


/**
 * The FreeRoomsGenerator class is responsible for generating data and templates related to free rooms,
 * including their availability and scheduling information.
 * It extends the BaseGenerator class and inherits common functionality for generating data and templates.
 */
public class FreeRoomsGenerator extends BaseGenerator{
    @NotNull(message = "Free rooms map must not be null")
    private final Map<Room, Map<Integer, Map<Timeslot.Day, Boolean>>> freeRooms;
    @NotEmpty(message = "Day decoder map must not be empty")
    private final Map<Timeslot.Day, Integer> dayDecoder;
    @NotNull(message = "Lists data map must not be null")
    private final Map<String, String> listsData;

    /**
     * Constructs a FreeRoomsGenerator object with the specified generation date string, timetables days data, free rooms map, and lists data map.
     *
     * @param generationDateString The generation date string to be displayed on the page. Must not be blank.
     * @param timetablesDays       The map containing the timetable data organized by table names and days. Must not be null.
     * @param freeRooms            The map containing the free rooms organized by days and hours. Must not be null.
     * @param listsData            The map containing the generated data for various lists used on the page. Must not be null.
     */
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

    /**
     * Generates a day decoder map that maps each day of the week to its corresponding index.
     *
     * @return The day decoder map containing the mapping of days to their indices.
     */
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

    /**
     * Generates data for free rooms in the timetables.
     * Overrides the abstract method 'generate' inherited from the BaseGenerator class.
     * The generated data includes information about available rooms and their occupancy status for each hour of the day.
     * The HTML templates are populated with the generated data and stored in the lists data map.
     */
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
