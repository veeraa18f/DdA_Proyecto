package InvestTrack.home;

import java.util.ArrayList;
import java.util.List;

public class HomeViewModel {

    public String totalBalanceText;
    public String totalChangeText;
    public String searchQuery;
    public String selectedFilter;
    public List<HomeAssetViewModel> assets = new ArrayList<>();
    public List<HomeAssetViewModel> favoriteAssets = new ArrayList<>();
}
