package com.tuempresa.investtrack.data.seed;

import android.content.Context;

import com.tuempresa.investtrack.data.model.Asset;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class InvestmentJsonLoader {

    private static final String ASSET_FILE_NAME = "investments.json";

    private final Context context;

    public InvestmentJsonLoader(Context context) {
        this.context = context.getApplicationContext();
    }

    public List<Asset> loadAssets() {
        List<Asset> assets = new ArrayList<>();

        try {
            JSONObject root = new JSONObject(readAssetFile());
            JSONArray assetArray = root.getJSONArray("assets");
            for (int index = 0; index < assetArray.length(); index++) {
                JSONObject assetJson = assetArray.getJSONObject(index);
                assets.add(new Asset(
                        assetJson.getString("id"),
                        assetJson.getString("name"),
                        assetJson.getString("ticker"),
                        assetJson.getString("type"),
                        assetJson.getDouble("currentPrice"),
                        assetJson.getDouble("quantity"),
                        assetJson.getDouble("averagePrice"),
                        assetJson.getString("logoDrawableName")
                ));
            }
        } catch (IOException | JSONException exception) {
            throw new IllegalStateException("Unable to load initial investment data.", exception);
        }

        return assets;
    }

    private String readAssetFile() throws IOException {
        StringBuilder builder = new StringBuilder();
        try (InputStream stream = context.getAssets().open(ASSET_FILE_NAME);
             BufferedReader reader = new BufferedReader(
                     new InputStreamReader(stream, StandardCharsets.UTF_8)
             )) {
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
        }
        return builder.toString();
    }
}
