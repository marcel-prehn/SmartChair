package uni.marcel.smartchair;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class TutorialFragment2 extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        try {
            ViewGroup view = (ViewGroup) inflater.inflate(R.layout.tutorial_fragment_2, container, false);
            return view;
        }
        catch (Exception ex) {
            Log.e("fragment2", "oncreateview " + ex.getMessage());
            return null;
        }

    }
}
