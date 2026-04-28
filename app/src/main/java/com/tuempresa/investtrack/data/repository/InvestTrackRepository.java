package com.tuempresa.investtrack.data.repository;

import android.content.Context;

import com.tuempresa.investtrack.data.local.AssetDao;
import com.tuempresa.investtrack.data.local.AssetEntity;
import com.tuempresa.investtrack.data.local.FavoriteDao;
import com.tuempresa.investtrack.data.local.FavoriteEntity;
import com.tuempresa.investtrack.data.local.InvestTrackRoomDatabase;
import com.tuempresa.investtrack.data.local.UserAssetDao;
import com.tuempresa.investtrack.data.local.UserAssetEntity;
import com.tuempresa.investtrack.data.local.UserDao;
import com.tuempresa.investtrack.data.local.UserEntity;
import com.tuempresa.investtrack.data.model.Asset;
import com.tuempresa.investtrack.data.seed.InvestmentJsonLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class InvestTrackRepository {

    public static final String GUEST_USER_ID = "guest-user";
    public static final String DEMO_USER_ID = "demo-user";

    private static InvestTrackRepository instance;

    private final InvestTrackRoomDatabase database;
    private final AssetDao assetDao;
    private final UserDao userDao;
    private final UserAssetDao userAssetDao;
    private final FavoriteDao favoriteDao;
    private final InvestmentJsonLoader jsonLoader;
    private final ExecutorService databaseExecutor = Executors.newSingleThreadExecutor();

    private InvestTrackRepository(Context context) {
        Context appContext = context.getApplicationContext();
        database = InvestTrackRoomDatabase.getInstance(appContext);
        assetDao = database.assetDao();
        userDao = database.userDao();
        userAssetDao = database.userAssetDao();
        favoriteDao = database.favoriteDao();
        jsonLoader = new InvestmentJsonLoader(appContext);
        ensureDefaultUsers();
        ensureSeedData();
    }

    public static synchronized InvestTrackRepository getInstance(Context context) {
        if (instance == null) {
            instance = new InvestTrackRepository(context);
        }
        return instance;
    }

    public static synchronized void resetInstanceForTests(Context context) {
        if (instance != null) {
            instance.databaseExecutor.shutdownNow();
            instance = null;
        }
        InvestTrackRoomDatabase.resetInstanceForTests(context);
    }

    public List<Asset> getAllAssets() {
        return getAllAssets(GUEST_USER_ID);
    }

    public List<Asset> getAllAssets(String userId) {
        ensureSeedData();
        String resolvedUserId = resolveUserId(userId);
        return execute(() -> mapAssets(userAssetDao.getPortfolioAssets(resolvedUserId)));
    }

    public Asset getAssetById(String assetId) {
        return getAssetById(GUEST_USER_ID, assetId);
    }

    public Asset getAssetById(String userId, String assetId) {
        ensureSeedData();
        String resolvedUserId = resolveUserId(userId);
        return execute(() -> mapAsset(userAssetDao.getPortfolioAssetById(resolvedUserId, assetId)));
    }

    public List<Asset> getAvailableCatalogAssets(String userId) {
        ensureSeedData();
        String resolvedUserId = resolveUserId(userId);
        if (GUEST_USER_ID.equals(resolvedUserId)) {
            return new ArrayList<>();
        }

        return execute(() -> {
            List<Asset> availableAssets = new ArrayList<>();
            for (Asset asset : jsonLoader.loadAssets()) {
                if (userAssetDao.containsAsset(resolvedUserId, asset.getId()) == 0) {
                    availableAssets.add(asset);
                }
            }
            return availableAssets;
        });
    }

    public boolean addAsset(
            String userId,
            String name,
            String ticker,
            String type,
            double currentPrice,
            double quantity,
            double averagePrice
    ) {
        if (!hasText(name)
                || !hasText(ticker)
                || !isSupportedAssetType(type)
                || currentPrice < 0
                || quantity < 0
                || averagePrice < 0) {
            return false;
        }

        String resolvedUserId = resolveUserId(userId);
        if (GUEST_USER_ID.equals(resolvedUserId)) {
            return false;
        }

        String normalizedTicker = ticker.trim().toUpperCase(Locale.US);
        String normalizedType = normalizeAssetType(type);
        String id = buildAssetId(normalizedTicker);
        String logoDrawableName = getDefaultLogoDrawableName(normalizedTicker, normalizedType);

        try {
            return execute(() -> {
                AssetEntity existingAsset = assetDao.getAssetByTicker(normalizedTicker);
                if (existingAsset != null) {
                    if (userAssetDao.containsAsset(resolvedUserId, existingAsset.id) > 0) {
                        return false;
                    }

                    userAssetDao.insertUserAsset(new UserAssetEntity(
                            resolvedUserId,
                            existingAsset.id,
                            currentPrice,
                            quantity,
                            averagePrice,
                            userAssetDao.getMaxDisplayOrder(resolvedUserId) + 1
                    ));
                    return true;
                }

                if (assetDao.getAssetById(id) != null) {
                    return false;
                }

                AssetEntity asset = new AssetEntity(
                        id,
                        name.trim(),
                        normalizedTicker,
                        normalizedType,
                        currentPrice,
                        quantity,
                        averagePrice,
                        logoDrawableName,
                        assetDao.getMaxDisplayOrder() + 1
                );
                assetDao.insertAsset(asset);
                userAssetDao.insertUserAsset(new UserAssetEntity(
                        resolvedUserId,
                        id,
                        currentPrice,
                        quantity,
                        averagePrice,
                        userAssetDao.getMaxDisplayOrder(resolvedUserId) + 1
                ));
                return true;
            });
        } catch (IllegalStateException exception) {
            return false;
        }
    }

    public boolean addCatalogAsset(String userId, String assetId, double quantity) {
        if (!hasText(assetId) || quantity <= 0) {
            return false;
        }

        String resolvedUserId = resolveUserId(userId);
        if (GUEST_USER_ID.equals(resolvedUserId)) {
            return false;
        }

        ensureSeedData();
        return execute(() -> database.runInTransaction(() -> {
            AssetEntity asset = assetDao.getAssetById(assetId);
            if (asset == null || userAssetDao.containsAsset(resolvedUserId, asset.id) > 0) {
                return false;
            }

            userAssetDao.insertUserAsset(new UserAssetEntity(
                    resolvedUserId,
                    asset.id,
                    asset.currentPrice,
                    quantity,
                    asset.currentPrice,
                    userAssetDao.getMaxDisplayOrder(resolvedUserId) + 1
            ));
            return true;
        }));
    }

    public boolean updateAssetPosition(String assetId, double currentPrice, double quantity) {
        return updateAssetPosition(GUEST_USER_ID, assetId, currentPrice, quantity);
    }

    public boolean updateAssetPosition(String userId, String assetId, double currentPrice, double quantity) {
        if (!hasText(assetId) || currentPrice < 0 || quantity < 0) {
            return false;
        }
        String resolvedUserId = resolveUserId(userId);
        if (GUEST_USER_ID.equals(resolvedUserId)) {
            return false;
        }
        return execute(() -> userAssetDao.updatePosition(
                resolvedUserId,
                assetId,
                currentPrice,
                quantity
        ) > 0);
    }

    public boolean removeAsset(String userId, String assetId) {
        if (!hasText(assetId)) {
            return false;
        }

        String resolvedUserId = resolveUserId(userId);
        if (GUEST_USER_ID.equals(resolvedUserId)) {
            return false;
        }

        return execute(() -> {
            final int[] deletedRows = new int[1];
            database.runInTransaction(() -> {
                favoriteDao.deleteFavorite(resolvedUserId, assetId);
                deletedRows[0] = userAssetDao.deleteUserAsset(resolvedUserId, assetId);
            });
            return deletedRows[0] > 0;
        });
    }

    public UserEntity authenticateUser(String email, String password) {
        if (!hasText(email) || !hasText(password)) {
            return null;
        }
        return execute(() -> userDao.authenticate(normalizeEmail(email), password));
    }

    public UserEntity createUser(String name, String email, String password) {
        if (!hasText(name) || !hasText(email) || !hasText(password)) {
            return null;
        }

        String normalizedEmail = normalizeEmail(email);
        String normalizedName = name.trim();
        try {
            UserEntity user = execute(() -> {
                if (userDao.getUserByEmail(normalizedEmail) != null) {
                    return null;
                }

                UserEntity createdUser = new UserEntity(
                        UUID.randomUUID().toString(),
                        normalizedEmail,
                        password,
                        normalizedName,
                        "+34 600 000 000",
                        1
                );
                userDao.insertUser(createdUser);
                return createdUser;
            });
            return user;
        } catch (IllegalStateException exception) {
            return null;
        }
    }

    public UserEntity getUserById(String userId) {
        if (!hasText(userId)) {
            return null;
        }
        return execute(() -> userDao.getUserById(userId));
    }

    public boolean userExistsByEmail(String email) {
        if (!hasText(email)) {
            return false;
        }
        String normalizedEmail = normalizeEmail(email);
        return execute(() -> userDao.getUserByEmail(normalizedEmail) != null);
    }

    public boolean resetPassword(String email, String newPassword) {
        if (!hasText(email) || !hasText(newPassword)) {
            return false;
        }

        String normalizedEmail = normalizeEmail(email);
        return execute(() -> userDao.updatePassword(normalizedEmail, newPassword) > 0);
    }

    public boolean updateUserProfile(String userId, String name, String phone, String email) {
        if (!hasText(userId) || !hasText(name) || !hasText(phone) || !hasText(email)) {
            return false;
        }

        try {
            return execute(() -> userDao.updateProfile(
                    userId,
                    name.trim(),
                    phone.trim(),
                    normalizeEmail(email)
            ) > 0);
        } catch (IllegalStateException exception) {
            return false;
        }
    }

    public boolean updateUserProfilePhoto(String userId, int profilePhotoIndex) {
        if (!hasText(userId)) {
            return false;
        }
        return execute(() -> userDao.updateProfilePhoto(userId, profilePhotoIndex) > 0);
    }

    public boolean isFavorite(String userId, String assetId) {
        if (!hasText(assetId)) {
            return false;
        }
        String resolvedUserId = resolveUserId(userId);
        return execute(() -> favoriteDao.isFavorite(resolvedUserId, assetId) > 0);
    }

    public boolean toggleFavorite(String userId, String assetId) {
        if (!hasText(assetId)) {
            return false;
        }

        String resolvedUserId = resolveUserId(userId);
        return execute(() -> {
            if (userAssetDao.containsAsset(resolvedUserId, assetId) == 0) {
                return false;
            }

            if (favoriteDao.isFavorite(resolvedUserId, assetId) > 0) {
                favoriteDao.deleteFavorite(resolvedUserId, assetId);
                return false;
            }

            favoriteDao.insertFavorite(new FavoriteEntity(resolvedUserId, assetId));
            return true;
        });
    }

    public List<Asset> getFavoriteAssets(String userId) {
        String resolvedUserId = resolveUserId(userId);
        return execute(() -> mapAssets(favoriteDao.getFavoriteAssets(resolvedUserId)));
    }

    private void ensureDefaultUsers() {
        executeVoid(() -> userDao.insertUsers(
                new UserEntity(
                        GUEST_USER_ID,
                        "guest@investtrack.app",
                        "guest123",
                        "Guest investor",
                        "+34 600 000 000",
                        1
                ),
                new UserEntity(
                        DEMO_USER_ID,
                        "demo@investtrack.app",
                        "password",
                        "Demo investor",
                        "+34 600 000 000",
                        1
                )
        ));
    }

    private void ensureSeedData() {
        executeVoid(() -> database.runInTransaction(() -> {
            List<Asset> assets = jsonLoader.loadAssets();
            if (assetDao.countAssets() == 0) {
                List<AssetEntity> entities = new ArrayList<>();
                for (int index = 0; index < assets.size(); index++) {
                    entities.add(mapAssetEntity(assets.get(index), index));
                }
                assetDao.insertAssets(entities);
                return;
            }

            for (int index = 0; index < assets.size(); index++) {
                assetDao.insertAssetIfAbsent(mapAssetEntity(assets.get(index), index));
            }
        }));
        syncGuestDemoPortfolio();
        seedUserPortfolio(DEMO_USER_ID);
        clearAutoSeededPortfolioForRegularUsers();
    }

    private void seedUserPortfolio(String userId) {
        String resolvedUserId = resolveUserId(userId);
        executeVoid(() -> database.runInTransaction(() -> {
            if (userAssetDao.countPortfolioAssets(resolvedUserId) > 0) {
                return;
            }

            List<Asset> assets = jsonLoader.loadAssets();
            for (int index = 0; index < assets.size(); index++) {
                Asset asset = assets.get(index);
                assetDao.insertAssetIfAbsent(mapAssetEntity(asset, index));
                userAssetDao.insertUserAsset(new UserAssetEntity(
                        resolvedUserId,
                        asset.getId(),
                        asset.getCurrentPrice(),
                        asset.getQuantity(),
                        asset.getAveragePrice(),
                        index
                ));
            }
        }));
    }

    private void syncGuestDemoPortfolio() {
        executeVoid(() -> database.runInTransaction(() -> {
            List<Asset> assets = jsonLoader.loadAssets();
            for (int index = 0; index < assets.size(); index++) {
                Asset asset = assets.get(index);
                assetDao.insertAssetIfAbsent(mapAssetEntity(asset, index));
                userAssetDao.insertUserAsset(new UserAssetEntity(
                        GUEST_USER_ID,
                        asset.getId(),
                        asset.getCurrentPrice(),
                        asset.getQuantity(),
                        asset.getAveragePrice(),
                        index
                ));
            }
        }));
    }

    private void clearAutoSeededPortfolioForRegularUsers() {
        executeVoid(() -> database.runInTransaction(() -> {
            int catalogAssetCount = assetDao.countAssets();
            if (catalogAssetCount == 0) {
                return;
            }

            List<UserEntity> users = userDao.getUsersExcluding(new String[]{
                    GUEST_USER_ID,
                    DEMO_USER_ID
            });
            for (UserEntity user : users) {
                int portfolioCount = userAssetDao.countPortfolioAssets(user.id);
                int matchingCatalogCount = userAssetDao.countPortfolioAssetsMatchingCatalog(user.id);
                if (portfolioCount == catalogAssetCount && matchingCatalogCount == catalogAssetCount) {
                    favoriteDao.deleteFavoritesForUser(user.id);
                    userAssetDao.deletePortfolioForUser(user.id);
                }
            }
        }));
    }

    private String resolveUserId(String userId) {
        if (!hasText(userId)) {
            return GUEST_USER_ID;
        }
        UserEntity user = getUserById(userId);
        return user == null ? GUEST_USER_ID : user.id;
    }

    private AssetEntity mapAssetEntity(Asset asset, int displayOrder) {
        return new AssetEntity(
                asset.getId(),
                asset.getName(),
                asset.getTicker(),
                asset.getType(),
                asset.getCurrentPrice(),
                asset.getQuantity(),
                asset.getAveragePrice(),
                asset.getLogoDrawableName(),
                displayOrder
        );
    }

    private List<Asset> mapAssets(List<AssetEntity> entities) {
        List<Asset> assets = new ArrayList<>();
        for (AssetEntity entity : entities) {
            assets.add(mapAsset(entity));
        }
        return assets;
    }

    private Asset mapAsset(AssetEntity entity) {
        if (entity == null) {
            return null;
        }
        return new Asset(
                entity.id,
                entity.name,
                entity.ticker,
                entity.type,
                entity.currentPrice,
                entity.quantity,
                entity.averagePrice,
                entity.logoDrawableName
        );
    }

    private String buildAssetId(String ticker) {
        return ticker.toLowerCase(Locale.US).replaceAll("[^a-z0-9]+", "_");
    }

    private boolean isSupportedAssetType(String type) {
        return "stock".equalsIgnoreCase(type) || "crypto".equalsIgnoreCase(type);
    }

    private String normalizeAssetType(String type) {
        return "crypto".equalsIgnoreCase(type) ? "Crypto" : "Stock";
    }

    private String getDefaultLogoDrawableName(String ticker, String type) {
        switch (ticker) {
            case "AAPL":
                return "logo_apple";
            case "MSFT":
                return "logo_microsoft";
            case "GOOG":
            case "GOOGL":
                return "logo_google";
            case "AMZN":
                return "logo_amazon";
            case "NVDA":
                return "logo_nvidia";
            case "META":
                return "logo_meta";
            case "TSLA":
                return "logo_tesla";
            case "JPM":
                return "logo_jpmorgan";
            case "V":
                return "logo_visa";
            case "WMT":
                return "logo_walmart";
            case "XOM":
                return "logo_exxon";
            case "UNH":
                return "logo_unitedhealth";
            case "JNJ":
                return "logo_johnson_johnson";
            case "PG":
                return "logo_procter_gamble";
            case "MA":
                return "logo_mastercard";
            case "HD":
                return "logo_home_depot";
            case "COST":
                return "logo_costco";
            case "NFLX":
                return "logo_netflix";
            case "ORCL":
                return "logo_oracle";
            case "DIS":
                return "logo_disney";
            case "BTC":
                return "logo_bitcoin";
            case "ETH":
                return "logo_ethereum";
            case "SOL":
                return "logo_solana";
            default:
                break;
        }
        return "Crypto".equals(type) ? "logo_bitcoin" : "ic_chart_line";
    }

    private String normalizeEmail(String email) {
        return email == null ? "" : email.trim().toLowerCase(Locale.US);
    }

    private boolean hasText(String value) {
        return value != null && !value.trim().isEmpty();
    }

    private <T> T execute(Callable<T> callable) {
        try {
            return databaseExecutor.submit(callable).get();
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Database operation interrupted.", exception);
        } catch (ExecutionException exception) {
            throw new IllegalStateException("Database operation failed.", exception.getCause());
        }
    }

    private void executeVoid(Runnable runnable) {
        execute(() -> {
            runnable.run();
            return null;
        });
    }
}
