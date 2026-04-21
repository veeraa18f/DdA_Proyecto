package InvestTrack.home;

import android.content.Context;

import java.util.List;

import InvestTrack.data.Asset;
import InvestTrack.data.InvestTrackRepository;
import InvestTrack.utils.AppPreferences;

public class HomeModel implements HomeContract.Model {

    private final Context context;
    private final InvestTrackRepository repository;

    public HomeModel(Context context) {
        this.context = context.getApplicationContext();
        repository = InvestTrackRepository.getInstance(context);
    }

    @Override
    public List<Asset> getAssets() {
        return repository.getAllAssets(AppPreferences.getActiveUserId(context));
    }
}
