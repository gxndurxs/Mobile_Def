package ru.mirea.ostrovskiy.mireaproject.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.io.File;
import java.util.List;
import ru.mirea.ostrovskiy.mireaproject.R;

public class AudioAdapter extends RecyclerView.Adapter<AudioAdapter.AudioViewHolder> {

    private List<File> audioFiles;
    private OnPlayButtonClickListener listener;

    public interface OnPlayButtonClickListener {
        void onPlayButtonClick(File file);
    }

    public AudioAdapter(List<File> audioFiles, OnPlayButtonClickListener listener) {
        this.audioFiles = audioFiles;
        this.listener = listener;
    }

    @NonNull
    @Override
    public AudioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_audio, parent, false);
        return new AudioViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AudioViewHolder holder, int position) {
        File audioFile = audioFiles.get(position);
        holder.textViewAudioName.setText(audioFile.getName());
        holder.buttonPlayAudio.setOnClickListener(v -> {
            if (listener != null) {
                listener.onPlayButtonClick(audioFile);
            }
        });
    }

    @Override
    public int getItemCount() {
        return audioFiles.size();
    }

    static class AudioViewHolder extends RecyclerView.ViewHolder {
        TextView textViewAudioName;
        ImageButton buttonPlayAudio;
        public AudioViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewAudioName = itemView.findViewById(R.id.textViewAudioName);
            buttonPlayAudio = itemView.findViewById(R.id.buttonPlayAudio);
        }
    }
}