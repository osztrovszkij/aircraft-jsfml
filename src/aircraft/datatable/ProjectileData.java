package aircraft.datatable;

import aircraft.Projectile;
import aircraft.TextureHolder;

import java.util.ArrayList;

/**
 * Created by roski on 5/13/2016.
 */
public class ProjectileData {
    public static ArrayList<ProjectileData> initializeProjectileData() {
        ArrayList<ProjectileData> data = new ArrayList<>(Projectile.Type.values().length);
        ProjectileData projectileData = new ProjectileData();

        projectileData.damage = 10;
        projectileData.speed = 300.f;
        projectileData.texture = TextureHolder.ID.BULLET;

        data.add(Projectile.Type.ALLIED_BULLET.ordinal(), projectileData);
        projectileData = new ProjectileData();

        projectileData.damage = 10;
        projectileData.speed = 300.f;
        projectileData.texture = TextureHolder.ID.BULLET;

        data.add(Projectile.Type.ENEMY_BULLET.ordinal(), projectileData);
        projectileData = new ProjectileData();

        projectileData.damage = 200;
        projectileData.speed = 150.f;
        projectileData.texture = TextureHolder.ID.MISSILE;

        data.add(Projectile.Type.MISSILE.ordinal(), projectileData);

        return data;
    }

    public int	damage;
    public float speed;
    public TextureHolder.ID texture;
}
