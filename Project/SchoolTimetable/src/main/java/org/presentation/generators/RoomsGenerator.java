package org.presentation.generators;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.domain.models.Room;
import org.domain.models.Timeslot;
import org.presentation.GUI;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class RoomsGenerator extends BaseGenerator{
    @NotNull(message = "Rooms list must not be null")
    private final List<@Valid Room> rooms;
    @NotNull(message = "Timetables names map must not be null")
    private final Map<String, String> timetablesNames;
    @NotNull(message = "Lists data map must not be null")
    private final Map<String, String> listsData;

    public RoomsGenerator(
            @NotBlank(message = "Generation string must not be blank")
            String generationDateString,
            @NotNull(message = "Days data map must not be null")
            Map<String, Map<Timeslot.Day, StringBuilder>> timetablesDays,
            @NotNull(message = "Timetables names map must not be null")
            Map<String, String> timetablesNames,
            @NotNull(message = "Lists data map must not be null")
            Map<String, String> listsData){
        super(generationDateString, timetablesDays);

        this.rooms = GUI.app.roomsService.getRooms();
        this.timetablesNames = timetablesNames;
        this.listsData = listsData;
    }

    public void generate(){
        String roomsData = utils.getBaseTemplateData("rooms");
        roomsData = roomsData.replace("$generation_date", this.generationDateString);
        StringBuilder labElementsData = new StringBuilder();
        StringBuilder courseElementsData = new StringBuilder();

        for (Room room : this.rooms){
            String roomEntry = utils.getBaseTemplateData("atomics" + this.separator + "list_entry");

            String name = Objects.requireNonNull(room.getName(), "Room name should not be null.");
            int floor = room.getFloor();
            int capacity = room.getCapacity();
            String timetableName = "timetable_r_" + name.toLowerCase().replace(" ", "_");

            roomEntry = roomEntry.replace("$entry_name", name);
            roomEntry = roomEntry.replace("$entry_ref", "./" + timetableName + ".html");

            if (room.getType() == Room.Type.COURSE){
                courseElementsData.append(roomEntry);
            }
            else if (room.getType() == Room.Type.LABORATORY){
                labElementsData.append(roomEntry);
            }

            this.addToDaysMap(timetableName);
            this.timetablesNames.put(timetableName, "Timetable of Room " + name + ", Floor " + floor + ", Capacity of " + capacity + " seats");
        }

        roomsData = roomsData.replace("$lab_elements", labElementsData.toString());
        roomsData = roomsData.replace("$course_elements", courseElementsData.toString());
        this.listsData.put("rooms", roomsData);
    }
}
