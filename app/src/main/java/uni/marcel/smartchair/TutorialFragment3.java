package uni.marcel.smartchair;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

public class TutorialFragment3 extends Fragment {

    private Button btClose;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        try {

            ViewGroup view = (ViewGroup) inflater.inflate(R.layout.tutorial_fragment_3, container, false);
            btClose = (Button) view.findViewById(R.id.btClose);
            btClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Close();
                }
            });

            return view;
        }
        catch (Exception ex) {
            Log.e("fragment2", "oncreateview " + ex.getMessage());
            return null;
        }

    }

    private void Close() {
        Intent i = new Intent(getActivity(), SelectDevice.class);
        startActivity(i);
    }
}
