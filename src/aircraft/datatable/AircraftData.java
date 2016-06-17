package aircraft.datatable;

import aircraft.Aircraft;
import aircraft.TextureHolder;
import org.jsfml.system.Time;

import java.util.ArrayList;

/**
 * Created by roski on 5/13/2016.
 */
public class AircraftData {
    public static ArrayList<AircraftData> initializeAircraftData() {
        ArrayList<AircraftData> data = new ArrayList<>(Aircraft.Type.values().length);
        AircraftData aircraftData = new AircraftData();

        aircraftData.hitpoints = 100;
        aircraftData.speed = 20.f;
        aircraftData.fireInterval = Time.getSeconds(1);
        aircraftData.texture = TextureHolder.ID.EAGLE;

        data.add(Aircraft.Type.EAGLE.ordinal(), aircraftData);
        aircraftData = new AircraftData();

        aircraftData.hitpoints = 20;
        aircraftData.speed = 80.f;
        aircraftData.texture = TextureHolder.ID.RAPTOR;
        aircraftData.directions.add(new Direction(+45.f, 80.f));
        aircraftData.directions.add(new Direction(-45.f, 160.f));
        aircraftData.directions.add(new Direction(+45.f, 80.f));
        aircraftData.fireInterval = Time.ZERO;

        data.add(Aircraft.Type.RAPTOR.ordinal(), aircraftData);
        aircraftData = new AircraftData();

        aircraftData.hitpoints = 40;
        aircraftData.speed = 50.f;
        aircraftData.texture = TextureHolder.ID.AVENGER;
        aircraftData.directions.add(new Direction(+45.f,  50.f));
        aircraftData.directions.add(new Direction(  0.f,  50.f));
        aircraftData.directions.add(new Direction(-45.f, 100.f));
        aircraftData.directions.add(new Direction(  0.f,  50.f));
        aircraftData.directions.add(new Direction(+45.f,  50.f));
        aircraftData.fireInterval = Time.getSeconds(2);

        data.add(Aircraft.Type.RAPTOR.ordinal(), aircraftData);

        return data;
    }

    public int	hitpoints;
    public float speed;
    public TextureHolder.ID texture;
    public Time fireInterval = Time.ZERO;
    public ArrayList<Direction> directions = new ArrayList<>();
}
