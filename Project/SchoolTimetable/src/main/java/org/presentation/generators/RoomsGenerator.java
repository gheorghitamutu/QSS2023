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


/**
 * The RoomGenerator class is responsible for generating information about rooms and their associated timetables.
 * It extends the BaseGenerator class and inherits its functionality for generating and managing timetable data.
 * The generated information includes room names, types, floors, capacities, and their associated timetable names.
 * It updates the lists data map with the generated information.
 */
public class RoomsGenerator extends BaseGenerator{
    @NotNull(message = "Rooms list must not be null")
    private final List<@Valid Room> rooms;
    @NotNull(message = "Timetables names map must not be null")
    private final Map<String, String> timetablesNames;
    @NotNull(message = "Lists data map must not be null")
    private final Map<String, String> listsData;

    /**
     * Constructs a RoomsGenerator object with the specified generation date string, timetables days data, timetables names map, and lists data map.
     *
     * @param generationDateString The generation date string to be displayed on the page. Must not be blank.
     * @param timetablesDays       The map containing the timetable data organized by table names and days. Must not be null.
     * @param timetablesNames      The map containing the timetable title organized by table names. Must not be null.
     * @param listsData            The map containing the generated data for various lists used on the page. Must not be null.
     */
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

    /**
     * Generates the information about the rooms and adds it to the lists data map.
     * Overrides the abstract method 'generate' inherited from the BaseGenerator class.
     * The generated data includes room names, types, floors, capacities, and their associated timetable names.
     * It iterates over each room and creates an entry for the room in the corresponding category (lab or course).
     * It adds the generated rooms data to the lists data map under the "rooms" key.
     */
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
