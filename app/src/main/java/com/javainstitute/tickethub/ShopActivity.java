package com.javainstitute.tickethub;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.javainstitute.tickethub.model.User;

public class ShopActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        if(HomeActivity.userObject.isSeller() == true){
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragmentContainerShop, new ViewShop());
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }else{
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragmentContainerShop, new CreateShop());
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }





        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.bottom_shop);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.bottom_home) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            }

            if (item.getItemId() == R.id.bottom_search) {
                startActivity(new Intent(getApplicationContext(), ActivitySearch.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            }

            if (item.getItemId() == R.id.bottom_shop) {
                return true;
            }

            if (item.getItemId() == R.id.bottom_profile) {
                startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            }

            return false;
        });
    }

    @Override
    public void onBackPressed() {
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragmentContainerShop);

        if (currentFragment instanceof AddTicket) {
            // If the current fragment is AddTicket, go back to ViewShop
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainerShop, new ViewShop())
                    .commit();
        } else {
            super.onBackPressed();
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
            finish();
        }

    }

}