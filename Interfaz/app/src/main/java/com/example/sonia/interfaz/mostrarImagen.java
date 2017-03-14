package com.example.sonia.interfaz;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.example.sonia.interfaz.R.id.foto;

public class mostrarImagen extends AppCompatActivity {

    Button abrir_foto;
    Button fer_foto;
    private ImageView imagen;
    Uri fileUri;
    //Constantes para indicar el código al intent:
    private static final int ELEGIR_FOTO=1;
    private static final int FOTO_CAMARA=2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar_imagen);

        //Enlazamos el imageView creado en xml con imagen
        imagen=(ImageView)findViewById(foto);
        abrir_foto=(Button)findViewById(R.id.abrir_foto);
        fer_foto=(Button)findViewById(R.id.fer_foto);

//=========================== OnClick ABRIR FOTO ===================================================
        abrir_foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buscarImagen(v);
            }//final onClick
        });//final setOnClickListener

//=========================== OnClick HACER FOTO ===================================================
        fer_foto.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                ferFoto(v);
            }//final onClick
        });//final setOnClickListener
    }//final onCreate

//====================== BUSCAR IMAGEN =============================================================
    public void buscarImagen(View v){
        //Abrimos la galeria para escoger una imagen
        Intent intent = new Intent ();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"),ELEGIR_FOTO);
    }//final buscarImagen

    public void ferFoto (View v){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri foto_save = null;
        if (takePictureIntent.resolveActivity(getPackageManager()) != null){
            startActivityForResult(takePictureIntent,FOTO_CAMARA);
        }//final if
    }

    public void hacerFoto (View v){
        Intent intent = new Intent();
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        try{
            fileUri = obtenerUriImagen();
            Log.d("Sonia","fileUri"+fileUri);
        }catch(Exception ex){
            Log.d("Sonia", ex.toString());
        }//final try/catch
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        startActiviyForResult(intent,FOTO_CAMARA);
    }//final hacerFoto

//==================================================================================================
    //Crear un file Uri para guardar la imagen
    private static Uri obtenerUriImagen(){
        return Uri.fromFile(obtenerFicheroImagen());
    }//final obtenerUriImagen

//========================== OBTENER URI IMAGEN ====================================================
    private static File obtenerFicheroImagen(){
        //Directorio donde creara el fichero:
        File mediaStorageDir = new File (Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),"MyCameraApp");
        //Crear la carpeta si no existe:
        if (!mediaStorageDir.mkdirs()){
            Log.d("Sonia","failed to create directory");
            return null;
        }//final if
        //Crear el fichero
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile = new File(mediaStorageDir.getPath()+ File.separator+"IMG_" + timeStamp + ".jpg");
        return mediaFile;
    } //final obtenerFicheroImagen

//==================================== ON ACTIVITY RESULT ==========================================
    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent respuesta){
        Log.d("Sonia","Dentro del onActivityResult");
        Uri ruta = null;
        if (requestCode == ELEGIR_FOTO && resultCode == RESULT_OK && respuesta !=null){
            //Intent de elegir una imagen
            ruta = respuesta.getData(); //obtenemos la ruta del intent
        }//final if
        if (requestCode == FOTO_CAMARA && resultCode == RESULT_OK){
            //Intent de Hacer Foto
            Bundle extras = respuesta.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imagen.setImageBitmap(imageBitmap);
            ruta = fileUri; //Obtenemos la ruta del fichero que hemos creado antes de activar el Intent
        }//final if
        //Comprobamos que la ruta tenga un Uri
        if (ruta != null) {
            Toast.makeText(this, "Tengo la imagen: " + ruta, Toast.LENGTH_LONG).show();
            //Ponemos la imagen en la ImageView
            ponerImagenRuta(ruta, imagen);
            //Enviamos la foto por email:
            enviarFotoPorEmail(ruta, new String[]{"sososo@epsg.upv.es"}, "Envio de foto por email", "Esto es el texto");
        }else{
            Log.d("Sonia","ruta mal!!");
        }//final else/if
    }//final onActivityResult

//==============================ENVIAR FOTO POR EMAIL ==============================================
    //Enviamos por email una foto
    private void enviarFotoPorEmail(Uri foto, String[] emails,String asunto, String texto){
        Log.d("Sonia","Foto a enviar: " + foto.toString());
        Intent i = new Intent (Intent.ACTION_SEND);
        i.setType("image/jpeg");
        i.putExtra(Intent.EXTRA_STREAM, foto);
        i.putExtra(Intent.EXTRA_EMAIL, emails);
        i.putExtra(Intent.EXTRA_SUBJECT, asunto);
        i.putExtra(Intent.EXTRA_TEXT, texto);
        startActivity(i);
    }//final enviarFotoPorEmail

//================================== PONER IMAGEN (RUTA) ===========================================
    //Modificamos el ImageView para que muestre la imagen que obtenemos del Uri
    private void ponerImagenRuta (Uri ruta, ImageView i){
        try{
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),ruta);
            //Modificamos el ImageView para que muestre la imagen que obtenemos del Bitmap
            i.setImageBitmap(bitmap);
        }catch (IOException ex){
            Toast.makeText(this,"Estoy en el catch", Toast.LENGTH_LONG).show();
        }//final try/catch
    }//final ponerImagenRuta

//=============================START ACTIVITY FOR RESULT ===========================================
    private void startActiviyForResult(Intent intent, int fotoCamara) {
    }//final startActivityForResult
}//final mostrarImagen class
