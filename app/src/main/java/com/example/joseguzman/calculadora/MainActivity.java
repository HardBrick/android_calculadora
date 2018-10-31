package com.example.joseguzman.calculadora;

import android.content.DialogInterface;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;

import com.example.joseguzman.calculadora.Modelo.Calculadora;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button btnEjecutar;
    Button btnLimpiar;
    EditText etNumero1;
    EditText etNumero2;
    TextView tvOperacion;
    Switch swGuardar;
    RadioGroup radioGroup;
    RadioButton rdSeleccionado;
    TextView tvResultado;
    List<String> log = new ArrayList<String>();
    ListAdapter adapter;
    ListView lvLog;
    AlertDialog.Builder builder;
    Calculadora calc = new Calculadora();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etNumero1 = (EditText)findViewById(R.id.etNumero1);
        etNumero2 = (EditText)findViewById(R.id.etNumero2);
        tvOperacion = (TextView)findViewById(R.id.tvOperacion);
        swGuardar = (Switch)findViewById(R.id.swGuardar);
        radioGroup = (RadioGroup)findViewById(R.id.radioGroup);
        btnEjecutar = (Button)findViewById(R.id.btnEjecutar);
        btnLimpiar = (Button)findViewById(R.id.btnLimpiar);
        tvResultado = (TextView)findViewById(R.id.tvResultado);
        lvLog = (ListView) findViewById(R.id.lvLog);



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(this);
        }




        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                rdSeleccionado = (RadioButton) findViewById(checkedId);
                if(rdSeleccionado.getText().toString().equals("Pot")){
                    etNumero1.setHint(getResources().getString(R.string.etNumero1HintPot));
                    etNumero2.setHint(getResources().getString(R.string.etNumero2HintPot));
                }else{
                    etNumero1.setHint(getResources().getString(R.string.etNumero1Hint));
                    etNumero2.setHint(getResources().getString(R.string.etNumero2Hint));
                }
                tvOperacion.setText(rdSeleccionado.getText().toString());
            }
        });

        btnEjecutar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Double uno=null,dos=null;
                boolean isDivPorCero = false;
                try{
                    uno = Double.parseDouble(etNumero1.getText().toString());
                }catch (Exception e){
                    etNumero1.setError(getResources().getString(R.string.errorValidaNumero));
                }
                try{
                    dos = Double.parseDouble(etNumero2.getText().toString());
                }catch (Exception e){
                    etNumero2.setError(getResources().getString(R.string.errorValidaNumero));
                }

                if(uno != null && dos != null){

                    switch (tvOperacion.getText().toString()){
                        case "+":
                            tvResultado.setText(calc.Sumar(uno,dos)+"");
                            break;
                        case "-":
                            tvResultado.setText(calc.Restar(uno,dos)+"");
                            break;
                        case "*":
                            tvResultado.setText(calc.Multiplicar(uno,dos)+"");
                            break;
                        case "/":
                            if(dos==0){
                                etNumero2.setError(getResources().getString(R.string.errorDivPorCero));
                                isDivPorCero = true;
                            }else{
                                tvResultado.setText(calc.Dividir(uno,dos)+"");
                            }
                            break;
                        case "Pot":
                            tvResultado.setText(calc.Potenciar(uno,dos)+"");
                            break;
                    }
                    if(!isDivPorCero && swGuardar.isChecked()){
                        log.add(uno + " " + tvOperacion.getText().toString() + " " + dos + " = " + tvResultado.getText().toString() );
                        adapter = new ArrayAdapter<String>(v.getContext(), android.R.layout.simple_list_item_1, log);
                        lvLog.setAdapter(adapter);
                    }
                }
            }
        });

        lvLog.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                builder.setTitle(getResources().getString(R.string.alertTituloEliminarRegistro))
                        .setMessage(getResources().getString(R.string.alertPreguntaEliminarRegistro))
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                log.remove(position);
                                lvLog.setAdapter(adapter);
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //No pasa nada!
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
                return false;
            }
        });


        btnLimpiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etNumero1.setText("");
                etNumero2.setText("");
                tvResultado.setText("0");
            }
        });

    }
}
