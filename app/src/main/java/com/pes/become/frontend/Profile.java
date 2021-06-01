package com.pes.become.frontend;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

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
    private TextView deleteAccount;
    private EditText passText, currPass, newPass, confPass, nameText;
    private Button done, done2;

    private ProgressBar loading;

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

        ImageButton trophiesButton = view.findViewById(R.id.trophiesButton);
        trophiesButton.setOnClickListener(v -> MainActivity.getInstance().setTrophiesScreen());

        TabLayout tabLayout = view.findViewById(R.id.tabLayout);
        ViewPager viewPager = view.findViewById(R.id.viewPager);
        viewPager.setAdapter(new MainAdapter(getChildFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setIcon(R.drawable.bookmark_icon);
        tabLayout.getTabAt(1).setIcon(R.drawable.stats_icon);

        return view;
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

        deleteAccount = sheetView.findViewById(R.id.deleteAcc);
        TextView changePassword = sheetView.findViewById(R.id.changePassword);
        TextView changeUsername = sheetView.findViewById(R.id.changeUsername);
        if(DA.getUserProvider().equals("google.com") || DA.getUserProvider().equals("facebook.com")) {
            changePassword.setVisibility(View.GONE);
            deleteAccount.setVisibility(View.GONE);
            changeUsername.setVisibility(View.GONE);
        } else {
            changePassword.setOnClickListener(v -> changePasswordSheet());
            deleteAccount.setOnClickListener(v -> askForPassword());
            changeUsername.setOnClickListener(v -> showChangeName());
        }

        TextView logout = sheetView.findViewById(R.id.logout);
        logout.setOnClickListener(v -> logOut());

        nameText = sheetView.findViewById(R.id.newName);
        done2 = sheetView.findViewById(R.id.doneButton2);

        passText = sheetView.findViewById(R.id.passText);
        done = sheetView.findViewById(R.id.doneButton);
        loading = sheetView.findViewById(R.id.loading);

        optionsSheet.setContentView(sheetView);
        optionsSheet.show();
    }


    private void changePasswordSheet() {
        optionsSheet.dismiss();
        optionsSheet = new BottomSheetDialog(global,R.style.BottomSheetTheme);
        View sheetView = LayoutInflater.from(getContext()).inflate(R.layout.change_password, view.findViewById(R.id.bottom_sheet));

        currPass = sheetView.findViewById(R.id.currentPassword);
        newPass = sheetView.findViewById(R.id.newPassword);
        confPass = sheetView.findViewById(R.id.confirmedPassword);

        Button done = sheetView.findViewById(R.id.doneButton);
        done.setOnClickListener(v -> changePassword());

        loading = sheetView.findViewById(R.id.loading);

        optionsSheet.setContentView(sheetView);
        optionsSheet.show();
    }

    /**
     * Metode per canviar la contrasenya d'un usuari
     */
    private void changePassword() {
        currPass.setError(null);
        newPass.setError(null);
        confPass.setError(null);

        String current = currPass.getText().toString();
        String new1 = newPass.getText().toString();
        String new2 = confPass.getText().toString();

        if (new1.isEmpty() || new2.isEmpty()) {
            newPass.setError(getString(R.string.notNull));
            confPass.setError(getString(R.string.notNull));
        } else if (!new1.equals(new2)) {
            newPass.setError(getString(R.string.passwords));
            confPass.setError(getString(R.string.passwords));
        } else if (new1.length() < 6) {
            newPass.setError(getString(R.string.shortPassword));
            confPass.setError(getString(R.string.shortPassword));
        } else {
            loading.setVisibility(View.VISIBLE);
            DA.changePassword(current,new1,this);
        }
    }

    public void changePasswordCallback(boolean success) {
        loading.setVisibility(View.GONE);
        if(success) {
            Toast.makeText(global, getString(R.string.passChanged), Toast.LENGTH_SHORT).show();
            optionsSheet.dismiss();
        } else {
            currPass.setError(getString(R.string.wrongPassword));
        }
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
     * Metode per demanar escriure el password si es vol esborrar el compte
     */
    private void askForPassword() {
        passText.setVisibility(View.VISIBLE);
        done.setVisibility(View.VISIBLE);
        done.setOnClickListener(v -> deleteUserAccount());
    }

    private void showChangeName() {
        nameText.setVisibility(View.VISIBLE);
        done2.setVisibility(View.VISIBLE);
        done2.setOnClickListener(v -> changeUsername());
    }

    private void changeUsername() {
        String name = nameText.getText().toString();
        if(name.isEmpty()) nameText.setError(getString(R.string.notNull));
        else {
            optionsSheet.dismiss();
            DA.changeUserName(name);
            username.setText(name);
            Toast.makeText(global, getString(R.string.username_changed), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Metode per solicitar esborrar el compte
     */
    private void deleteUserAccount() {
        loading.setVisibility(View.VISIBLE);
        String password = passText.getText().toString();
        DA.deleteUser(password,this);
    }

    /**
     * Metode per rebre la resposta a la sol.licitud d'esborrat d'usuari
     */
    public void deleteCallback(boolean success) {
        loading.setVisibility(View.GONE);
        if(success) {
            Toast.makeText(global, getString(R.string.accountDeleted), Toast.LENGTH_SHORT).show();
            optionsSheet.dismiss();
            startActivity(new Intent(global, Login.class));
            getActivity().finish();
        } else {
            passText.setError(getString(R.string.wrongPassword));
        }
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
            requestPermissions(new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, 10);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==10) {
            if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                changeProfilePic();
            } else {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(global);
                alertDialogBuilder.setTitle(R.string.permissionDeniedTitle);
                alertDialogBuilder.setMessage(R.string.permissionDenied);
                alertDialogBuilder.setPositiveButton("OK", (dialog, id) -> {});
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
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
            try {
                DA.updateProfilePic(imageUri, MediaStore.Images.Media.getBitmap(MainActivity.getInstance().getContentResolver(), imageUri));
            } catch (IOException e) {
                e.printStackTrace();
            }
            optionsSheet.dismiss();
        }
    }
}