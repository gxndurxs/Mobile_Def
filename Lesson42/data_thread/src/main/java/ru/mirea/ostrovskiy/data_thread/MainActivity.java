package ru.mirea.ostrovskiy.data_thread;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import ru.mirea.ostrovskiy.data_thread.databinding.ActivityMainBinding;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        final Runnable runn1 = () -> binding.tvInfo.setText("Поток runn1");
        final Runnable runn2 = () -> binding.tvInfo.setText("Поток runn2");
        final Runnable runn3 = () -> {
            String info = "Различия методов:\n" +
                    "runOnUiThread - метод Activity, выполняет код в UI-потоке.\n" +
                    "post - метод View, ставит Runnable в очередь UI-потока.\n" +
                    "postDelayed - то же, что post, но с отложенным выполнением.\n\n" +
                    "Последовательность запуска: runn1 -> runn2 -> runn3";
            binding.tvInfo.setText(info);
        };

        Thread t = new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(2);
                runOnUiThread(runn1);
                TimeUnit.SECONDS.sleep(1);
                binding.tvInfo.post(runn2);
                TimeUnit.SECONDS.sleep(2);
                binding.tvInfo.post(runn3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        t.start();
    }
}