package aircraft.command;

/**
 * Created by roski on 5/12/2016.
 */
//public enum Category {
//        NONE,
//        SCENE_AIR_LAYER,
//        PLAYER_AIRCRAFT,
//        ALLIED_AIRCRAFT,
//        ENEMY_AIRCRAFT,
//        PICKUP,
//        AlliedProjectile,
//        EnemyProjectile,
//        Aircraft = PlayerAircraft | AlliedAircraft | EnemyAircraft,
//        Projectile = AlliedProjectile | EnemyProjectile,
//}

public class Category {
        public final static byte NONE              = 0;
        public final static byte SCENE_AIR_LAYER   = 1 << 0;
        public final static byte PLAYER_AIRCRAFT   = 1 << 1;
        public final static byte ALLIED_AIRCRAFT   = 1 << 2;
        public final static byte ENEMY_AIRCRAFT    = 1 << 3;
        public final static byte PICKUP            = 1 << 4;
        public final static byte ALLIED_PROJECTILE = 1 << 5;
        public final static byte ENEMY_PROJECTILE  = 1 << 6;

        public final static byte AIRCRAFT = PLAYER_AIRCRAFT | ALLIED_AIRCRAFT | ENEMY_AIRCRAFT;
        public final static byte PROJECTILE = ALLIED_PROJECTILE | ENEMY_PROJECTILE;
}
