package org.herod.buyer.phone;

import org.herod.framework.widget.ActionBar;
import org.herod.framework.widget.ActionBar.Action;
import org.herod.framework.widget.ActionBar.IntentAction;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class HomeActivity extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);

		ActionBar actionBar = (ActionBar) findViewById(R.id.actionbar);

		Action shareAction = new IntentAction(this, createShareIntent(),
				R.drawable.ic_title_share_default);
		actionBar.addAction(shareAction);
		Action shoppingCartAction = new IntentAction(this, new Intent(this,
				ShoppingCartActivity.class), R.drawable.shopping_cart);
		actionBar.addAction(shoppingCartAction);

	}

	public void showTakeOutStores(View view) {
		 startActivity(new Intent(this, StoresActivity.class));
	}

	public void showConvenienceStores(View view) {
		 startActivity(new Intent(this, StoresActivity.class));

	}

	public static Intent createIntent(Context context) {
		Intent i = new Intent(context, HomeActivity.class);
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		return i;
	}

	private Intent createShareIntent() {
		final Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_TEXT, "Shared from the ActionBar widget.");
		return Intent.createChooser(intent, "Share");
	}
}