package ru.mirea.ostrovskiy.mireaproject.ui;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.Locale;

import ru.mirea.ostrovskiy.mireaproject.databinding.FragmentAltitudeBinding;

public class AltitudeFragment extends Fragment implements SensorEventListener {

    private FragmentAltitudeBinding binding;
    private SensorManager sensorManager;
    private Sensor pressureSensor;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAltitudeBinding.inflate(inflater, container, false);
        sensorManager = (SensorManager) requireActivity().getSystemService(Context.SENSOR_SERVICE);
        pressureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        sensorManager.registerListener(this, pressureSensor, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_PRESSURE) {
            float pressure = event.values[0];
            float altitude = SensorManager.getAltitude(SensorManager.PRESSURE_STANDARD_ATMOSPHERE, pressure);
            binding.textViewAltitude.setText(String.format(Locale.getDefault(), "%.1f м", altitude));
            updateAltitudeInfo(altitude);
        }
    }

    private void updateAltitudeInfo(float altitude) {
        String info;
        if (altitude < 1500) {
            info = "Вы на безопасной высоте. Влияние на организм минимально.";
        } else if (altitude < 3500) {
            info = "Высокогорье. Возможны симптомы горной болезни: головная боль, тошнота. Рекомендуется акклиматизация.";
        } else if (altitude < 5500) {
            info = "Экстремальная высота. Серьезный риск отека легких и мозга. Необходима специальная подготовка и оборудование.";
        } else {
            info = "Смертельная зона. Длительное пребывание без кислорода невозможно.";
        }
        binding.textViewAltitudeInfo.setText(info);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}