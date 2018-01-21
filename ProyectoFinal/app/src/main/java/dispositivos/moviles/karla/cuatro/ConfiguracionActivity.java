package dispositivos.moviles.karla.cuatro;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by ferKarly.
 * Clase-Programación de Dispositivos Moviles
 * Version -mil ocho mil
 */
public class ConfiguracionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracion);
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new MainSettingsFragment())
                .commit();

    }

    private class MainSettingsFragment extends PreferenceFragment {

        @Override
        public void onCreate( Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_general);
        }
    }
}
