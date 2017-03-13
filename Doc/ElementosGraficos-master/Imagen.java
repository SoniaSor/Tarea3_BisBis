package com.example.pausa.elementosgraficos;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Imagen extends AppCompatActivity {

    final private int SELECT_IMAGE = 1234;
    final private int TAKE_IMAGE = 4321;

    private ImageView image;

    Uri fileUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imagen);

        //Enlazamos el ImageView creado en xml con image y lo hacemos invisible por el momento
        image = (ImageView) findViewById(R.id.image);
        image.setVisibility(View.INVISIBLE);

    }

    //onClick boton Seleccionar Imagen
    public void buscarImagen(View v) {

        //Abrimos galeria para escoger una imagen
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_IMAGE);
    }

    //onClick boton Hacer Foto
    public void hacerFoto(View v) {

        Intent intent = new Intent();
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
       try {
            fileUri = obtenerUriImagen();
            Log.d("Pau.ElementosGraficos","fileUri: " + fileUri);
        } catch (Exception ex) {
            Log.e("Pau.ElementosGraficos", ex.toString());
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        startActivityForResult(intent, TAKE_IMAGE);

    }

    /**
     * Create a file Uri for saving an image or video
     */
    private static Uri obtenerUriImagen() {
        return Uri.fromFile(obtenerFicheroImagen());
    }

    private static File obtenerFicheroImagen() {

        //Directorio donde creara el fichero
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "MyCameraApp");

        // Crear la carpeta si no existe
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("Pau.ElementosGraficos", "failed to create directory");
                return null;
            }
        }

        // Crear el fichero
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        File mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");

        return mediaFile;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent respuesta) {
        Log.e("Pau.ElementosGraficos", "dentro de onActivityResult");
        Uri ruta = null;

        if (requestCode == SELECT_IMAGE && resultCode == RESULT_OK && respuesta != null) { // Intent de Seleccionar Imagen
            ruta = respuesta.getData(); //Obtenemos la ruta del intent
        } else if (requestCode == TAKE_IMAGE && resultCode == RESULT_OK) { //Intent de Hacer Foto
            ruta = fileUri; //Obtenemos la ruta del fichero que hemos creado antes de activar el Intent
        } else { // Si no es ninguno de los anteriores
            Log.d("Pau.ElementosGraficos", "Algo no ha ido bien al recibir los Intents");
        }



        // Comprobamos que la ruta tenga un Uri
        if (ruta != null) {
            Toast.makeText(this, "Tengo la imagen: " + ruta, Toast.LENGTH_SHORT).show();

            // Ponemos la imagen en le ImageView
            ponerImagen(ruta, image);

            //Enviamos la foto por email
            enviarFotoPorEmail(ruta,new String[]{"pasallla@epsg.upv.es"},"Envio de foto por email","Esto es el texto");
        } else {
            Log.e("Pau.ElementosGraficos", "ruta = null !!!!!");
        }




    }

    // Enviamos por email una foto
    private void enviarFotoPorEmail(Uri foto, String[] emails, String asunto, String texto){
        Log.d("Pau.ElementosGraficos","foto a enviar: " + foto.toString());
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("image/jpeg");
        i.putExtra(Intent.EXTRA_STREAM, foto);

        i.putExtra(Intent.EXTRA_EMAIL, emails);
        i.putExtra(Intent.EXTRA_SUBJECT, asunto);
        i.putExtra(Intent.EXTRA_TEXT, texto);

        startActivity(i);

    }

    // Modificamos el ImageView para que muestre la imagen que obtenemos del Uri
    private void ponerImagen(Uri ruta, ImageView i){

        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), ruta);
            ponerImagen(bitmap,i);
        } catch (IOException ex) {
            Toast.makeText(this, "Estoy en el catch", Toast.LENGTH_SHORT).show();
        }


    }

    // Modificamos el ImageView para que muestre la imagen que obtenemos del Bitmap
    private void ponerImagen (Bitmap b, ImageView i){
        i.setImageBitmap(b);
        i.setVisibility(View.VISIBLE);
    }

}
