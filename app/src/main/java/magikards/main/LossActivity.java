package magikards.main;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class LossActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loss);


        Button lossButton = (Button) findViewById(R.id.lossButton);
        lossButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishActivity();
            }
        });

        ImageView magikarpImageView = (ImageView) findViewById(R.id.lossImgView);
        Glide.with(this)
                .load(R.drawable.magikarp)
                .into(magikarpImageView);

    }

    public void finishActivity() {
        this.startActivity(new Intent(this, MainActivity.class));
        this.finish();
    }
}
