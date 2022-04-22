package com.example.arichbrof

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.io.ByteArrayOutputStream
import java.io.IOException

class MainActivity : AppCompatActivity() {

    lateinit var btSelecConvet: Button
    lateinit var btCargarImagen: Button
    lateinit var btSubir: Button
    lateinit var btBajar: Button
    lateinit var ivFoto: ImageView
    lateinit var ImageUri : Uri
    lateinit var sImage: String
    lateinit var sTitulo: String
    lateinit var f64: String
    lateinit var t64: String
    lateinit var btRv : Button
    lateinit var tvTitulo: TextView
 //   lateinit var aImagenes: MutableList<String>

    lateinit var etTitulo: EditText
    val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btRv = findViewById(R.id.btRv)
        btSelecConvet = findViewById(R.id.btSelecConvert)
        btCargarImagen = findViewById(R.id.btCargarImagen)
        btSubir = findViewById(R.id.btSubir)
        btBajar = findViewById(R.id.btBajar)
        ivFoto = findViewById(R.id.ivFoto)
        etTitulo = findViewById(R.id.etTitulo)
        tvTitulo = findViewById(R.id.tvTitulo)



        btSelecConvet.setOnClickListener{
            seleccionarImagen()
        }

        btCargarImagen.setOnClickListener{
            decodificarImagen()
        }

        btSubir.setOnClickListener{

            sTitulo = etTitulo.text.toString()
            MandarDatos()


        }
        btBajar.setOnClickListener{

            BajarDatos()
           // bajartodoslosdatos()
        }



    }






    private fun seleccionarImagen() {

        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        resultLauncher.launch(intent)


    }

    private fun decodificarImagen(){

        val bytes = Base64.decode(sImage, Base64.DEFAULT)
        // Initialize bitmap
        val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
        // set bitmap on imageView
        ivFoto.setImageBitmap(bitmap)

        Toast.makeText(this@MainActivity, "Imagen decodificada", Toast.LENGTH_SHORT).show()

    }

    private fun MandarDatos(){

        // Create a new user with a first and last name
        val user = hashMapOf(
            "Titulo" to sTitulo,
            "Code64" to sImage
        )

// Add a new document with a generated ID
        db.collection("users")
            .add(user)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                Toast.makeText(this@MainActivity, "Imagen subida", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
                Toast.makeText(this@MainActivity, "Imagen no subida", Toast.LENGTH_SHORT).show()

            }

    }


    private fun bajartodoslosdatos(){

        db.collection("users")
            .whereEqualTo("Code64", true)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    Log.d("MainActivity", "${document.id} => ${document.data}")
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents: ", exception)
            }
    }

    private fun BajarDatos(){

        val listaFotos = ArrayList<Imagenes>()


        db.collection("users")
            .get()
            .addOnSuccessListener { result ->

                for (document in result) {
                    Log.d("MainActivity", "${document.id} => ${document.data}")

                   f64 = document.data.get("Code64").toString()
                   t64 = document.data.get("Titulo").toString()

                    val bytes = Base64.decode(f64, Base64.DEFAULT)
                    // Initialize bitmap
                    val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)

                    // Creo objeto foto
                    var foto = Imagenes("",t64)

                    // Anhado la foto a la lista
                    listaFotos.add(foto)








                    // set bitmap on imageView
                    ivFoto.setImageBitmap(bitmap)
                    tvTitulo.text = t64
break;
                    Handler().postDelayed({
                    }, 3000)
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }


    }


    var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // There are no request codes
            val data: Intent? = result.data
            if (result.resultCode == RESULT_OK){
                ImageUri = data?.data!!
                // when result is ok
                // initialize uri
                val uri = data.data
                // Initialize bitmap
                try {
                    val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
                    // initialize byte stream
                    val stream = ByteArrayOutputStream()
                    // compress Bitmap
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
                    // Initialize byte array
                    val bytes = stream.toByteArray()
                    // get base64 encoded string
                    sImage = Base64.encodeToString(bytes, Base64.DEFAULT)
                    // set encoded text on textview
                   // tvCodigo!!.text = sImage
                } catch (e: IOException) {
                    e.printStackTrace()
                }
              //  ivFoto.setImageURI(ImageUri)
            }

}}}