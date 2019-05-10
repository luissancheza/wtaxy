package com.example.wtaxi;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.example.wtaxi.Model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

public class MainActivity extends AppCompatActivity {

    RelativeLayout rellay1, rellay2;
    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            rellay1.setVisibility(View.VISIBLE);
            rellay2.setVisibility(View.VISIBLE);
        }
    };

    Button btnLogin, btnRegistrodrive, btnforgetpass;
    RelativeLayout rootLayout;

    //FirebaseAuth auth;
    private FirebaseAuth auth;
    FirebaseDatabase db;
    DatabaseReference users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rellay1 = (RelativeLayout) findViewById(R.id.rellay1);
        rellay2 = (RelativeLayout) findViewById(R.id.rellay2);
        handler.postDelayed(runnable, 2000);

        //Init Firebase
        auth = FirebaseAuth.getInstance();
        FirebaseApp.initializeApp(this);
        
        db = FirebaseDatabase.getInstance();
        users = db.getReference("Users");
        //FirebaseApp.initializeApp(this);

        //Init View
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnRegistrodrive = (Button) findViewById(R.id.btnRegistro);
        btnforgetpass = (Button) findViewById(R.id.btnOlvidoPass);
        rootLayout = (RelativeLayout) findViewById(R.id.rootLayout);

        //Events
        btnRegistrodrive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRegisterDialog();
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoginDialog();
            }
        });
    }

    private void showLoginDialog() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Entrar... ");
        //dialog.setMessage("Por favor utilice su correo para entrar");

        LayoutInflater inflater = LayoutInflater.from(this);
        View login_layout = inflater.inflate(R.layout.layout_login, null);

        final MaterialEditText correo = login_layout.findViewById(R.id.inputEmail);
        final MaterialEditText pass = login_layout.findViewById(R.id.inputPass);

        dialog.setView(login_layout);

        //setButton
        dialog.setPositiveButton("Entrar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        dialogInterface.dismiss();

                        //Check validation
                        if (TextUtils.isEmpty(correo.getText().toString())) {
                            Snackbar.make(rootLayout, "Introduzca su correo", Snackbar.LENGTH_SHORT).show();
                            return;
                        }
                        if (TextUtils.isEmpty(pass.getText().toString())) {
                            Snackbar.make(rootLayout, "Introduzca su contraseña", Snackbar.LENGTH_SHORT).show();
                            return;
                        }

                        //Login
                        auth.signInWithEmailAndPassword(correo.getText().toString(),pass.getText().toString())
                                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                    @Override
                                    public void onSuccess(AuthResult authResult) {
                                        startActivity(new Intent(MainActivity.this, Welcome.class));
                                        finish();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Snackbar.make(rootLayout, "Failed "+e.getMessage(), Snackbar.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                dialogInterface.dismiss();
            }
        });

        dialog.show();
    }

    private void showRegisterDialog() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Registro ");
        //dialog.setMessage("Por favor utilice su correo para registrarse");

        LayoutInflater inflater = LayoutInflater.from(this);
        View register_layout = inflater.inflate(R.layout.layout_register, null);

        final MaterialEditText nombre = register_layout.findViewById(R.id.inputNombre);
        final MaterialEditText apellido1 = register_layout.findViewById(R.id.inputAp1);
        final MaterialEditText apellido2 = register_layout.findViewById(R.id.inputAp2);
        final MaterialEditText telefono = register_layout.findViewById(R.id.inputTelefono);
        final MaterialEditText correo = register_layout.findViewById(R.id.inputEmail);
        final MaterialEditText fechanac = register_layout.findViewById(R.id.inputFechanac);
        final MaterialEditText pass = register_layout.findViewById(R.id.inputPass);

        dialog.setView(register_layout);

        //setButton
        dialog.setPositiveButton("Registrar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                dialogInterface.dismiss();

                //Check validation
                if(TextUtils.isEmpty(nombre.getText().toString())){
                    Snackbar.make(rootLayout, "Introduzca su nombre", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(apellido1.getText().toString())){
                    Snackbar.make(rootLayout, "Introduzca su apellido 1", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(apellido2.getText().toString())){
                    Snackbar.make(rootLayout, "Introduzca su apellido 2 ", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(telefono.getText().toString())){
                    Snackbar.make(rootLayout, "Introduzca su telefono", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(correo.getText().toString())){
                    Snackbar.make(rootLayout, "Introduzca su correo", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(fechanac.getText().toString())){
                    Snackbar.make(rootLayout, "Introduzca su Fecha de nacimiento", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(pass.getText().toString())){
                    Snackbar.make(rootLayout, "Introduzca su contraseña", Snackbar.LENGTH_SHORT).show();
                    return;
                }

                //Register new User
                auth.createUserWithEmailAndPassword(correo.getText().toString(), pass.getText().toString())
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                //Save user to db
                                User user = new User();
                                user.setNombre(nombre.getText().toString());
                                user.setNombre(apellido1.getText().toString());
                                user.setNombre(apellido2.getText().toString());
                                user.setNombre(telefono.getText().toString());
                                user.setNombre(correo.getText().toString());
                                user.setNombre(fechanac.getText().toString());
                                user.setNombre(pass.getText().toString());

                                //Use correo to key
                                users.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .setValue(user)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Snackbar.make(rootLayout, "Registrado correctamente!!!", Snackbar.LENGTH_SHORT).show();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Snackbar.make(rootLayout, "El registro fallo!!!", Snackbar.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Snackbar.make(rootLayout, "Fallo "+e.getMessage(), Snackbar.LENGTH_SHORT).show();
                    }
                });
            }
        });

        dialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                dialogInterface.dismiss();
            }
        });

        dialog.show();
    }
}
