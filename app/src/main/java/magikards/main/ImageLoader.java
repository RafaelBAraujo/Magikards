package magikards.main;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;


public class ImageLoader {
    public static Bitmap baixarImagem(String url) throws IOException{
        URL endereco;
        InputStream inputStream;
        Bitmap imagem;

        endereco = new URL(url);
        inputStream = endereco.openStream();
        imagem = BitmapFactory.decodeStream(inputStream);

        inputStream.close();

        return imagem;
    }
}

