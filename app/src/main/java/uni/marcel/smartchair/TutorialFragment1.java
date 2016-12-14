package uni.marcel.smartchair;

import android.media.Image;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class TutorialFragment1 extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        try {
            ViewGroup view = (ViewGroup) inflater.inflate(R.layout.tutorial_fragment_1, container, false);
            return view;
        }
        catch (Exception ex) {
            Log.e("fragment1", "oncreateview " + ex.getMessage());
            return null;
        }

    }
}
