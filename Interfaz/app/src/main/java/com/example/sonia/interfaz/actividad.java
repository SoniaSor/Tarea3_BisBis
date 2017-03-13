package com.example.sonia.interfaz;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class actividad extends AppCompatActivity {
//OnCreate de la actividad principal
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("Sonia","Estamos en el onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividad);
    }//final onCreate
//==================================================================================================
//Creación del menu
    @Override
    public boolean onCreateOptionsMenu (Menu menu){
        //Inflar el menú de la Action Bar
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
        //El método recibe como parámetro una instancia de tipo Menu, equivalente al elemento XML "menu".
    }// final onCreateOptionsMenu
//==================================================================================================
//Selector de opciones del menú
    @Override
    public boolean onOptionsItemSelected (MenuItem item){
        //Asignar las funciones a cada botón:
        switch(item.getItemId()){
            case R.id.op1:
                //La opción 1 abre la actividad listaOpciones
                Log.d("Sonia","Se ha pulsado la opción para ir a la lista de opciones");
                Intent intencion1 = new Intent (this,listaOpciones.class);
                startActivity(intencion1);
                return true;
            case R.id.op2:
                //La opción 2 abre la actividad mostrarImagen
                Log.d("Sonia","Se ha pulsado la opción para ir a mostrar Imagen");
                Intent intencion2 = new Intent (this,mostrarImagen.class);
                startActivity(intencion2);
                return true;
        }//final switch
        return true;
    }//final onOptionsItemSelected
//==================================================================================================
}//final class
