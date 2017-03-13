//Autor: Manu Larrosa

package com.example.manu.variasactivities;

import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.content.Intent;
import android.view.Menu;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.net.Uri;
import android.graphics.Bitmap;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.io.IOException;


public class MainActivity extends AppCompatActivity {

    //ImageView donde se representará la imagen cargada
    private ImageView lienzo;
    //Código de respuesta cuando la app de cámara realiza la foto
    final static int RESULTADO_FOTO = 1;
    //Código de respuesta cuando la app de la galería escoge una foto
    final static int RESULTADO_GALERIA = 2;
    //Uri de la foto guardada
    Uri uriFoto;

    // ---------------- FUNCIONES CALLBACK -------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("manu.hola", "inicio OnCreate");

        setContentView(R.layout.activity_main);
        Log.d("manu.hola", "Activity displayed");

        /*Como he desactivado la ActionBar por defecto creo una nueva ActionBar en una nueva toolbar
        donde mostrar el menú de opciones*/
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.show(); //Al mostrarse llama a la funcion onCreateOptionsMenu()
        actionBar.setDisplayHomeAsUpEnabled(true);

        //El ImageView donde se mostrara la imagen
        lienzo = (ImageView) super.findViewById(R.id.elLienzo);

        Log.d("manu.hola", "fin onCreate");


    }

    //------------------------------------------------------------------------------------------------------------

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d("manu.hola", "inicio onCreateOptionsMenu");
        //Muestra el menú definido en res/menu/menu.xml
        getMenuInflater().inflate(R.menu.menu, menu);
        Log.d("manu.hola", "Menu displayed");
        return true;
    }

    //-------------------------------------------------------------------------------------------------------------

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /*Esta función callback se llama al pulsar una de las opciones del menú,
        comprueba qué botón ha sido pulsado y realiza la correspondiente acción*/

        switch (item.getItemId()) {
            case R.id.boton1Menu: //boton de capturar foto pulsado
                Log.d("manu", "BOTON FOTO PULSADO");
                //Llamo a la función que se encarga de llamar a la app de la cámara para que capture la foto
                capturarFoto();
                return true;
            case R.id.boton2Menu: //Sin implementar
                Log.d("manu.hola", "BOTON GALERIA PULSADO");
                //A implementar: cogerFotoGaleria();
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

    //------------------------------------------------------------------------------------------------------------

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        /*Esta función callback se llama al recibir el resultado de una activity a la que hemos llamado,
        comprueba el código del resultado y realiza la correspondiente acción*/

        if (requestCode == RESULTADO_FOTO && resultCode == RESULT_OK) {
            Log.d("manu.hola", "RESULTADO FOTO OK");

            //Guarda los extras en un Bundle
            Bundle extras = data.getExtras();

            //DA ERROR:
//            if (intentFoto.resolveActivity(getPackageManager()) != null) {
//                File photoFile = null;
//                try {
//                    photoFile = crearFicheroImagen(); //Crea el fichero donde se guardará la imagen
//                    Log.d("manu.hola", "Fichero de imagen creado");
//                } catch (IOException ex) {
//                    Log.d("manu.hola", "ERROR CREANDO EL FICHERO");
//                }
//                if (photoFile != null) {
//                    Uri photoURI = FileProvider.getUriForFile(this,
//                            "com.example.android.fileprovider",
//                            photoFile);
//                    intentFoto.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
//                    startActivityForResult(intentFoto, RESULTADO_FOTO);
//                }
//            }
            for (String key : extras.keySet()) {
                Log.d("manu.hola", "clave dentro de extras: " + key);

            }
           // Bitmap imageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.parse(mCurrentPhotoPath));

            //Recoge la imagen guardada en el extra
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            //Consultamos información sobre la imagen para verificarla
            Log.d("manu.hola", "informacion de la image: byteCount=" + imageBitmap.getByteCount());
            Log.d("manu.hola", "informacion de la image: altura=" + imageBitmap.getHeight());
            Log.d("manu.hola", "informacion de la image: ancho=" + imageBitmap.getWidth());
            Log.d("manu.hola", "extras.get OK");
            Log.d ("manu.hola", " vale null lasImagenes? = " + lienzo);

            mostrarFoto(imageBitmap);
            Log.d("manu.hola", "FIN RESULTADO_FOTO");
        }
    }

    //------------------- MIS FUNCIONES --------------------------------------------------------------------------


    private void capturarFoto(){
        //Intent que llama a la aplicación de la cámara
        Intent intentFoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //Arranca la activity de la cámara a la espera del resultado. Al acabar llamará a onActivityResult()
        startActivityForResult(intentFoto, RESULTADO_FOTO);
        Log.d("manu.hola", "startActitivity para la camara hecho");
    }

    //------------------------------------------------------------------------------------------------------------

    private void mostrarFoto(Bitmap foto){
        // Muestra una imagen bitmap en Lienzo
        lienzo.setImageBitmap(foto);
    }
    //------------------------------------------------------------------------------------------------------------

    //CON ERRORES QUE SOLUCIONAR
    private File crearFicheroImagen() throws IOException {
        Log.d("manu.hola", "inicio createImageFile");
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        Log.d("manu.hola", "nombre del fichero OK");
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        Log.d("manu.hola", "getExternalFilesDir OK");

        File image = File.createTempFile(
                imageFileName,  /* prefijo */
                ".jpg",         /* sufijo */
                storageDir      /* directorio */
        );
        Log.d("manu.hola", "FICHERO CREADO");
        return image;
    }

}
