package aircraft.datatable;

import aircraft.Aircraft;
import aircraft.Pickup;
import aircraft.TextureHolder;

import java.util.ArrayList;

/**
 * Created by roski on 5/13/2016.
 */
public class PickupData {

    @FunctionalInterface
    public interface Action {
        void exec(Aircraft aircraft);
    }

//    public static ArrayList<PickupData> initializePickupData() {
//        ArrayList<PickupData> data = new ArrayList<>(Pickup.Type.values().length);
//
//        data.get(Pickup.Type.HEALTH_REFILL.ordinal()).texture = TextureHolder.ID.HEALTH_REFILL;
//        data.get(Pickup.Type.HEALTH_REFILL.ordinal()).action = aircraft -> aircraft.repair(25);
//
//        data.get(Pickup.Type.MISSILE_REFILL.ordinal()).texture = TextureHolder.ID.MISSILE_REFILL;
//        data.get(Pickup.Type.MISSILE_REFILL.ordinal()).action = std::bind(&Aircraft::collectMissiles, _1, 3);
//
//        data.get(Pickup.Type.FIRE_SPREAD.ordinal()).texture = TextureHolder.ID.FIRE_SPREAD;
//        data.get(Pickup.Type.FIRE_SPREAD.ordinal()).action = std::bind(&Aircraft::increaseSpread, _1);
//
//        data.get(Pickup.Type.FIRE_RATE.ordinal()).texture = TextureHolder.ID.FIRE_RATE;
//        data.get(Pickup.Type.FIRE_RATE.ordinal()).action = std::bind(&Aircraft::increaseFireRate, _1);
//
//        return data;
//    }

    public Action action;
    public TextureHolder.ID texture;
}
