package com.pes.become.frontend;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.tabs.TabLayout;
import com.pes.become.R;
import com.pes.become.backend.adapters.DomainAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

public class Profile extends Fragment {

    private DomainAdapter DA;

    private Context global;

    private View view;

    private CircleImageView profilePic;
    private TextView username;

    private BottomSheetDialog optionsSheet;

    /**
     * Pestanyes del TabLayout
     */
    private ArrayList<Fragment> tabs;

    /**
     * Constructora del Profile
     */
    public Profile() {}

    /**
     * Funcio del Profile que s'executa al crear-la
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.profile, container, false);
        super.onCreate(savedInstanceState);

        global = this.getActivity();

        DA = DomainAdapter.getInstance();

        tabs = new ArrayList<>();
        tabs.add(new RoutinesList());
        tabs.add(new Stats());

        username = view.findViewById(R.id.usernameText);
        profilePic = view.findViewById(R.id.profilePic);
        loadUserInfo();

        ImageButton settingsButton = view.findViewById(R.id.settingsButton);
        settingsButton.setOnClickListener(v -> createOptionsSheet());

        TabLayout tabLayout = view.findViewById(R.id.tabLayout);
        ViewPager viewPager = view.findViewById(R.id.viewPager);
        viewPager.setAdapter(new MainAdapter(getChildFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setIcon(R.drawable.bookmark_icon);
        tabLayout.getTabAt(1).setIcon(R.drawable.stats_icon);

        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==10) {
            if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                changeProfilePic(); // no s'obre la galeria quan li dones a permetre, has de tornar a clicar
            }
        }
    }

    /**
     * Classe que controla els canvis de pestanya
     */
    private class MainAdapter extends FragmentPagerAdapter {

        public MainAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return tabs.get(position);
        }

        @Override
        public int getCount() {
            return tabs.size();
        }

    }

    /**
     * Métode per obtenir la imatge de perfil i el nom d'usuari
     */
    private void loadUserInfo() {
        ArrayList<Object> userInfo = DA.loadUserInfo();
        username.setText((String) userInfo.get(0));
        if(userInfo.get(1) != null)
            profilePic.setImageBitmap((Bitmap) userInfo.get(1));
    }

    /**
     * Metode que obre el menú d'opcions
     */
    private void createOptionsSheet() {
        optionsSheet = new BottomSheetDialog(global,R.style.BottomSheetTheme);
        View sheetView = LayoutInflater.from(getContext()).inflate(R.layout.profile_settings_menu, view.findViewById(R.id.bottom_sheet));

        TextView updateProfilePic = sheetView.findViewById(R.id.updateProfilePic);
        updateProfilePic.setOnClickListener(v -> changeProfilePic());

        TextView changePassword = sheetView.findViewById(R.id.changePassword);
        changePassword.setOnClickListener(v -> changePassword());

        TextView logout = sheetView.findViewById(R.id.logout);
        logout.setOnClickListener(v -> logOut());

        optionsSheet.setContentView(sheetView);
        optionsSheet.show();
    }

    /**
     * Metode per canviar la contrasenya d'un usuari
     */
    private void changePassword() {

        //do something

        optionsSheet.dismiss();
    }

    /**
     * Metode per fer log out de l'usuari actual
     */
    private void logOut() {
        optionsSheet.dismiss();
        DA.logoutUser();
        startActivity(new Intent(global, Login.class));
        Objects.requireNonNull(getActivity()).finish();
    }

    /**
     * Métode per obrir la galeria per tal que l'usuari seleccioni una imatge de perfil
     */
    private void changeProfilePic() {
        if(ContextCompat.checkSelfPermission(global, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent, 1);
        } else {
            ActivityCompat.requestPermissions((Activity) global,new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, 10);
        }
    }

    /**
     * Métode que recull el resultat de la selecció que l'usuari ha fet a la galeria
     * @param requestCode codi que ha de coincidir amb el de la crida startActivityForResult
     * @param resultCode resultat de la operació
     * @param data imatge seleccionada
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode==RESULT_OK && data!=null && data.getData()!=null) {
            Uri imageUri = data.getData();
            profilePic.setImageURI(imageUri);
            DA.updateProfilePic(imageUri);
            try {
                DA.updateProfilePic(MediaStore.Images.Media.getBitmap(MainActivity.getInstance().getContentResolver(), imageUri));
            } catch (IOException e) {
                e.printStackTrace();
            }
            optionsSheet.dismiss();
        }
    }
}