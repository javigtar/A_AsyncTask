package dam.org.asynctask;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;


public class MainActivity extends Activity {

    TextView salida;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        salida = (TextView)findViewById(R.id.tvSalida);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void calcularOperacion(View view){

        EditText entrada = (EditText)findViewById(R.id.etEntrada);
        int numero = Integer.parseInt(entrada.getText().toString());

        salida.setText("");

        MiTarea tarea = new MiTarea();
        tarea.execute(numero);

    }

    class MiTarea extends AsyncTask<Integer, Integer, Integer>{

        private ProgressDialog progreso;

        //Preparación de ejecución de la tarea
        @Override
        protected void onPreExecute() {
            progreso = new ProgressDialog(MainActivity.this);
            progreso.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progreso.setMessage("Calculando...");
            progreso.setCancelable(true);
            //Se ejecuta cuando cancelamos el ProgressDialog
            progreso.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialogInterface) {
                    MiTarea.this.cancel(true);
                }
            });

            progreso.setMax(100);
            progreso.setProgress(0);
            progreso.show();
        }

        //Se ejecuta despues de onPreExecute y está el código principal de la tarea
        @Override
        protected Integer doInBackground(Integer... n) {

            int res = 1;

            for (int i = 1; i <= n[0] && !isCancelled() ; i++){
                res *= i;
                SystemClock.sleep(1000);
                publishProgress(i*100/n[0]);
            }

            return res;
        }

        //Progreso de la tarea, se ejecuta en el hilo de la interfaz
        @Override
        protected void onProgressUpdate(Integer... values) {
            progreso.setProgress(values[0]);
        }

        //Se ejecuta cuando acabe la tarea, en el hilo de la interfaz
        @Override
        protected void onPostExecute(Integer integer) {
            progreso.dismiss();
            salida.append(integer + "\n");
        }

        //Se ejecuta si se cancela la tarea, en el hilo de la interfaz
        @Override
        protected void onCancelled() {
            salida.append("Cancelado");
        }
    }

}
