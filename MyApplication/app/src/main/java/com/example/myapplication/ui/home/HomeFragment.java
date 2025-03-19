package com.example.myapplication.ui.home;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.myapplication.R;
import com.example.myapplication.logica.Base_Fragment;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HomeFragment extends Fragment {
    TextView btgenerar, btsubir, bttomar;
    Spinner spdificultades,sptama;
    private int PICK_IMAGE_REQUEST = 1;
    private int REQUEST_IMAGE_CAPTURE = 2;
    private int REQUEST_CAMERA_PERMISSION = 100;
    private String currentPhotoPath;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_home, container, false);
        btgenerar = view.findViewById(R.id.tvGenerar);
        btsubir = view.findViewById(R.id.tvSubir);
        bttomar = view.findViewById(R.id.tvTomar);
        spdificultades = view.findViewById(R.id.spdificultades);
        sptama = view.findViewById(R.id.spTama);

        String[] opciones = getResources().getStringArray(R.array.dificultades);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), R.layout.item_dificultad, opciones);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spdificultades.setAdapter(adapter);

        String[] tama = getResources().getStringArray(R.array.tamanos);
        ArrayAdapter<String> adapterTam = new ArrayAdapter<>(requireContext(), R.layout.item_tam, tama);
        adapterTam.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sptama.setAdapter(adapterTam);

        permisos();

        btgenerar.setOnClickListener(v -> {
            String dificultad = spdificultades.getSelectedItem().toString();
            Base_Fragment fragment = new Base_Fragment();
            Bundle args = new Bundle();
            args.putString("dificultad", dificultad);
            fragment.setArguments(args);

            Navigation.findNavController(v).navigate(R.id.action_nav_home_to_baseFragment);
        });

        btsubir.setOnClickListener(v -> {
            String dificultad = spdificultades.getSelectedItem().toString();
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            intent.putExtra("dificultad", dificultad);
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        });

        bttomar.setOnClickListener(v -> camara());

        return view;
    }
    private void permisos() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
        }
    }

    private void camara() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(requireActivity().getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = guardarimagen();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(requireContext(), "com.example.myapplication.fileprovider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private File guardarimagen() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", java.util.Locale.US).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String dificultad = spdificultades.getSelectedItem().toString();
        String tama = sptama.getSelectedItem().toString();
        Bundle bundle = new Bundle();
        bundle.putString("dificultad", dificultad);
        bundle.putString("tamaño", tama);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == requireActivity().RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            bundle.putString("imageUri", imageUri.toString());
            if(tama.equals("2x2")) {
                Navigation.findNavController(requireView())
                        .navigate(R.id.action_nav_home_to_dosFragment, bundle);
            } else {
                if(tama.equals("4x4")) {
                    Navigation.findNavController(requireView())
                            .navigate(R.id.action_nav_home_to_cuatroFragment, bundle);
                }else{
                    Navigation.findNavController(requireView())
                            .navigate(R.id.action_nav_home_to_tresFragment, bundle);
                }
            }
        } else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == requireActivity().RESULT_OK) {
            Uri imageUri = Uri.fromFile(new File(currentPhotoPath));
            bundle.putString("imageUri", imageUri.toString());
            if(tama.equals("2x2")) {
                Navigation.findNavController(requireView())
                        .navigate(R.id.action_nav_home_to_dosFragment, bundle);
            } else {
                if(tama.equals("4x4")) {
                    Navigation.findNavController(requireView())
                            .navigate(R.id.action_nav_home_to_cuatroFragment, bundle);
                }else{
                    Navigation.findNavController(requireView())
                            .navigate(R.id.action_nav_home_to_tresFragment, bundle);
                }
            }
        }
    }
}