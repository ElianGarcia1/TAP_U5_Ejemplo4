package mx.edu.ittepic.tap_u5_ejercicio4

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    val baseRemota = FirebaseFirestore.getInstance()
    val lista = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        cargarDatoDesdeNube()

        button.setOnClickListener {
            val documento = hashMapOf(
                "nombre" to nombre.text.toString(),
                "telefono" to telefono.text.toString(),
                "edad" to edad.text.toString()
            )



            baseRemota.collection("TAP")
                .add(documento)
                .addOnSuccessListener {
                    //EN CASO DE QUE SI HAYA FUNCIONADO
                    AlertDialog.Builder(this)
                        .setMessage("Exito!")
                        .setTitle("Atencion")
                        .setPositiveButton("Ok", { d, i ->})
                        .show()
                }
                .addOnFailureListener {
                    //EN CASO DE QUE NO HAYA FUNCIONADO
                    AlertDialog.Builder(this)
                        .setMessage("No cueentas con internet\nno se inserto")
                        .setTitle("Error")
                        .setPositiveButton("OK") { d, i -> d.dismiss() }
                        .show()
                }
            nombre.setText("")
            telefono.setText("")
            edad.setText("")
        }
        fun cargarDatoDesdeNube() {
            baseRemota.collection("TAP")
                .addSnapshotListener { value, error ->
                    if (error != null) {
                        AlertDialog.Builder(this)
                            .setMessage("no cuentas con internet!\ntu consulta automatica no realizo")
                            .setTitle("Error")
                            .setPositiveButton("Ok", { d, i -> d.dismiss() })
                            .show()
                        return@addSnapshotListener
                    }
                    lista.clear()
                    for (documento in value!!){
                        var cadena = "NOMBRE: "+documento.getString("nombre")+"\nTELEFONO: "+documento.getString("telefono")+"\nEDAD: "+documento.get("edad").toString()

                        lista.add(cadena)
                    }

                    listadocumentos.adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, lista)
                }
        }
    }
}