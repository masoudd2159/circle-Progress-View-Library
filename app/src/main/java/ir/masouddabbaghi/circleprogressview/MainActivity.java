package ir.masouddabbaghi.circleprogressview;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SeekBar;

public class MainActivity extends AppCompatActivity {

    private CircleProgress circleProgress;
    private SeekBar seekBar;
    private CheckBox autoColored;
    private CheckBox showPercent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        circleProgress = findViewById(R.id.circleProgress);
        seekBar = findViewById(R.id.seekBar);
        autoColored = findViewById(R.id.checkBoxAutoColored);
        showPercent = findViewById(R.id.checkBoxShowPercent);
        autoColored.setChecked(circleProgress.isAutoColored());
        showPercent.setChecked(circleProgress.isShowPercent());

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser)
                    circleProgress.setProgressWithAnimation(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        autoColored.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                circleProgress.setAutoColored(isChecked);
            }
        });

        showPercent.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                circleProgress.setShowPercent(isChecked);
            }
        });
    }
}
