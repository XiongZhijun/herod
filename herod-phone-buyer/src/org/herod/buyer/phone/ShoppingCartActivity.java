package org.herod.buyer.phone;

import org.herod.framework.widget.ActionBar;
import org.herod.framework.widget.ActionBar.AbstractAction;
import org.herod.framework.widget.ActionBar.IntentAction;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class ShoppingCartActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);

        ActionBar actionBar = (ActionBar) findViewById(R.id.actionbar);
        // You can also assign the title programmatically by passing a
        // CharSequence or resource id.
        //actionBar.setTitle(R.string.some_title);
        actionBar.setHomeAction(new IntentAction(this, HomeActivity.createIntent(this), R.drawable.ic_title_home_default));
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.addAction(new IntentAction(this, createShareIntent(), R.drawable.ic_title_share_default));
        actionBar.addAction(new ExampleAction());
    }

    private Intent createShareIntent() {
        final Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, "Shared from the ActionBar widget.");
        return Intent.createChooser(intent, "Share");
    }

    private class ExampleAction extends AbstractAction {

        public ExampleAction() {
            super(R.drawable.ic_title_export_default);
        }

        @Override
        public void performAction(View view) {
            Toast.makeText(ShoppingCartActivity.this,
                    "Example action", Toast.LENGTH_SHORT).show();
        }

    }

}