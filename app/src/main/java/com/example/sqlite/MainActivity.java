package com.example.sqlite;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    EditText ID, Nombre, Area;
    ListView Lista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ID = findViewById(R.id.txtID);
        Nombre = findViewById(R.id.txtNombre);
        Area = findViewById(R.id.txtArea);
        Lista = findViewById(R.id.ListaUsuario);
        CargaUsuarios();
    }

    public void RegistrarUsuario(View view){
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "Produccion", null, 1);
        SQLiteDatabase BaseDatos = admin.getWritableDatabase();
        String IDUsuario = ID.getText().toString();
        String NombreUsuario = Nombre.getText().toString();
        String AreaUsuario = Area.getText().toString();
        if (!NombreUsuario.isEmpty() && !AreaUsuario.isEmpty()){
            ContentValues DatosUsuario = new ContentValues();
            DatosUsuario.put("idUsuario", IDUsuario);
            DatosUsuario.put("NombreUsuario", NombreUsuario);
            DatosUsuario.put("AreaUsuario", AreaUsuario);
            BaseDatos.insert("Usuarios", null, DatosUsuario);
            BaseDatos.close();
            Nombre.setText("");
            ID.setText("");
            Area.setText("");
            CargaUsuarios();
            Toast.makeText(this,"Usuario registrado exitosamente", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this,"Debes completar toda la informacion", Toast.LENGTH_SHORT).show();
        }
    }
    public void BuscarUsuario(View view){
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "Produccion", null, 1);
        SQLiteDatabase BaseDatos = admin.getWritableDatabase();
        String idUsuario = ID.getText().toString();
        if (!idUsuario.isEmpty()){
            Cursor fila = BaseDatos.rawQuery("Select NombreUsuario, AreaUsuario from Usuarios where idUsuario =" + idUsuario, null);
            if(fila.moveToFirst()){
                Nombre.setText(fila.getString(0));
                Area.setText(fila.getString(1));
                BaseDatos.close();
            } else {
                Toast.makeText(this, "ID ingresado no existe", Toast.LENGTH_LONG).show();
                BaseDatos.close();
            }
        } else {
            Toast.makeText(this, "Campo ID usuario no debe estar vacio", Toast.LENGTH_LONG).show();
        }
    }

    public void EliminarUsuario(View view){
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "Produccion", null, 1);
        SQLiteDatabase BaseDatos = admin.getWritableDatabase();
        String idUsuario = ID.getText().toString();
        if (!idUsuario.isEmpty()){
            int Eliminar = BaseDatos.delete("Usuarios", "idUsuario=" +idUsuario, null);
            BaseDatos.close();
            ID.setText("");
            Nombre.setText("");
            Area.setText("");
            if (Eliminar == 1){
                Toast.makeText(this, "Usuario eliminado correctamente", Toast.LENGTH_LONG).show();
                CargaUsuarios();
            } else {
                Toast.makeText(this, "No se encontro el usuario a eliminar", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "Campo ID usuario no debe estar vacio", Toast.LENGTH_LONG).show();
        }

    }

    public void ModificarUsuario(View view){
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "Produccion", null, 1);
        SQLiteDatabase BaseDatos = admin.getWritableDatabase();
        String idUsuario = ID.getText().toString();
        String NombreUsuario = Nombre.getText().toString();
        String AreaUsuario = Area.getText().toString();
        if (!NombreUsuario.isEmpty() && !AreaUsuario.isEmpty()){
            ContentValues DatosUsuario = new ContentValues();
            DatosUsuario.put("NombreUsuario", NombreUsuario);
            DatosUsuario.put("AreaUsuario", AreaUsuario);
            int Cantidad = BaseDatos.update("Usuarios", DatosUsuario, "idUsuario="+ idUsuario, null);
            BaseDatos.close();
            if (Cantidad == 1){
                Toast.makeText(this, "Usuario modificado exitosamente", Toast.LENGTH_LONG).show();
                ID.setText("");
                Nombre.setText("");
                Area.setText("");
                CargaUsuarios();
            } else {
                Toast.makeText(this, "No se ha podido encontrar el usuario a modificar", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "No pueden haber campos vacios", Toast.LENGTH_LONG).show();
        }
    }

    public void CargaUsuarios() {
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "Produccion", null, 1);
        SQLiteDatabase BaseDatos = admin.getReadableDatabase();
        Cursor fila = BaseDatos.rawQuery("SELECT * FROM Usuarios", null);
        ArrayList<String> ListaUsuarios = new ArrayList<>();
        if (fila.moveToFirst()) {
            do {
                String idUsuario = fila.getString(0);
                String nombreUsuario = fila.getString(1);
                String areaUsuario = fila.getString(2);
                String userInfo = "ID: " + idUsuario + ", Nombre: " + nombreUsuario + ", Área: " + areaUsuario;
                ListaUsuarios.add(userInfo);
            } while (fila.moveToNext());
        }
        BaseDatos.close();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, ListaUsuarios);
        Lista.setAdapter(adapter);
    }


}