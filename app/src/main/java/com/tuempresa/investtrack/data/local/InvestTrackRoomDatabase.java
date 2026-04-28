package com.tuempresa.investtrack.data.local;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(
        entities = {
                UserEntity.class,
                AssetEntity.class,
                UserAssetEntity.class,
                FavoriteEntity.class
        },
        version = 2,
        exportSchema = false
)
public abstract class InvestTrackRoomDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "investtrack_room.db";

    private static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL(
                    "CREATE TABLE IF NOT EXISTS user_assets ("
                            + "user_id TEXT NOT NULL, "
                            + "asset_id TEXT NOT NULL, "
                            + "current_price REAL NOT NULL, "
                            + "quantity REAL NOT NULL, "
                            + "average_price REAL NOT NULL, "
                            + "display_order INTEGER NOT NULL, "
                            + "PRIMARY KEY(user_id, asset_id), "
                            + "FOREIGN KEY(user_id) REFERENCES users(id) ON DELETE CASCADE, "
                            + "FOREIGN KEY(asset_id) REFERENCES assets(id) ON DELETE CASCADE"
                            + ")"
            );
            database.execSQL("CREATE INDEX IF NOT EXISTS index_user_assets_user_id ON user_assets(user_id)");
            database.execSQL("CREATE INDEX IF NOT EXISTS index_user_assets_asset_id ON user_assets(asset_id)");
            database.execSQL(
                    "INSERT OR IGNORE INTO user_assets("
                            + "user_id, asset_id, current_price, quantity, average_price, display_order"
                            + ") "
                            + "SELECT users.id, assets.id, assets.current_price, "
                            + "assets.quantity, assets.average_price, assets.display_order "
                            + "FROM users CROSS JOIN assets"
            );
        }
    };

    private static volatile InvestTrackRoomDatabase instance;

    public static InvestTrackRoomDatabase getInstance(Context context) {
        if (instance == null) {
            synchronized (InvestTrackRoomDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(
                            context.getApplicationContext(),
                            InvestTrackRoomDatabase.class,
                            DATABASE_NAME
                    ).addMigrations(MIGRATION_1_2).build();
                }
            }
        }
        return instance;
    }

    public static synchronized void resetInstanceForTests(Context context) {
        if (instance != null) {
            instance.close();
            instance = null;
        }
        context.getApplicationContext().deleteDatabase(DATABASE_NAME);
    }

    public abstract AssetDao assetDao();

    public abstract UserDao userDao();

    public abstract UserAssetDao userAssetDao();

    public abstract FavoriteDao favoriteDao();
}
