package com.example.systemnews;

import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.example.systemnews.databinding.ActivityMainBinding;
import android.view.Menu;
import android.view.MenuItem;

/**
 * MainActivity: The entry point of the application.
 * Manages the top-level navigation, the Action Bar, and the overall UI structure.
 */
public class MainActivity extends AppCompatActivity {

    // Configuration for the top-level destinations in the app bar
    private AppBarConfiguration appBarConfiguration;
    // View binding instance to access UI elements in activity_main.xml
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Enables edge-to-edge display, allowing content to flow behind system bars
        EdgeToEdge.enable(this);

        // Inflate the layout using View Binding and set it as the content view
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Adjust padding to accommodate system bars (status bar, navigation bar) to prevent overlapping
        ViewCompat.setOnApplyWindowInsetsListener(binding.main, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Setup the Toolbar as the app's action bar
        setSupportActionBar(binding.toolbar);

        // Initialize the NavController to manage app navigation within the NavHostFragment
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        
        // Define top-level destinations where the Up button will not be shown
        appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.LoginFragment, R.id.ResourcesFragment
        ).build();

        // Connect the NavController with the ActionBar for automatic title updates and navigation handling
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu resource into the existing menu
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle menu item clicks
        int id = item.getItemId();

        // Check if the logout action was selected
        if (id == R.id.action_logout) {
            NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
            // Navigate back to the Login screen
            navController.navigate(R.id.LoginFragment);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        // Delegate the Up button press to the NavController
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }
}
