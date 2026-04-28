package com.tuempresa.investtrack.data.local;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "assets",
        indices = {
                @Index(value = {"ticker"}, unique = true)
        }
)
public class AssetEntity {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    public final String id;

    @NonNull
    @ColumnInfo(name = "name")
    public final String name;

    @NonNull
    @ColumnInfo(name = "ticker")
    public final String ticker;

    @NonNull
    @ColumnInfo(name = "type")
    public final String type;

    @ColumnInfo(name = "current_price")
    public final double currentPrice;

    @ColumnInfo(name = "quantity")
    public final double quantity;

    @ColumnInfo(name = "average_price")
    public final double averagePrice;

    @NonNull
    @ColumnInfo(name = "logo_drawable_name")
    public final String logoDrawableName;

    @ColumnInfo(name = "display_order")
    public final int displayOrder;

    public AssetEntity(
            @NonNull String id,
            @NonNull String name,
            @NonNull String ticker,
            @NonNull String type,
            double currentPrice,
            double quantity,
            double averagePrice,
            @NonNull String logoDrawableName,
            int displayOrder
    ) {
        this.id = id;
        this.name = name;
        this.ticker = ticker;
        this.type = type;
        this.currentPrice = currentPrice;
        this.quantity = quantity;
        this.averagePrice = averagePrice;
        this.logoDrawableName = logoDrawableName;
        this.displayOrder = displayOrder;
    }
}
