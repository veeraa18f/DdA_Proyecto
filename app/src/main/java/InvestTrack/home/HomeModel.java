package InvestTrack.home;

import android.content.Context;

import java.util.List;

import InvestTrack.data.Asset;
import InvestTrack.data.InvestTrackRepository;

public class HomeModel implements HomeContract.Model {

    private final InvestTrackRepository repository;

    public HomeModel(Context context) {
        repository = InvestTrackRepository.getInstance(context);
    }

    @Override
    public List<Asset> getAssets() {
        return repository.getAllAssets();
    }
}
