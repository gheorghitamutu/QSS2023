package org.application.presentation.generators;

import org.application.domain.models.Room;
import org.application.domain.models.Timeslot;
import org.application.presentation.GUI;

import java.util.List;
import java.util.Map;

public class RoomsGenerator extends BaseGenerator{
    private final List<Room> rooms;
    private final Map<String, String> timetablesNames;
    private final Map<String, String> listsData;

    public RoomsGenerator(String generationDateString, Map<String, Map<Timeslot.Day, StringBuilder>> timetablesDays, Map<String, String> timetablesNames, Map<String, String> listsData){
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

            String name = room.getName();
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
