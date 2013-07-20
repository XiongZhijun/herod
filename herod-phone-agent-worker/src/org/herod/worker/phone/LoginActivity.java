package org.herod.worker.phone;

import org.herod.framework.ci.InjectViewHelper;
import org.herod.framework.ci.annotation.InjectView;
import org.herod.framework.utils.DeviceUtils;
import org.herod.framework.utils.StringUtils;
import org.herod.worker.phone.model.Token;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

public class LoginActivity extends Activity implements OnEditorActionListener,
		OnClickListener {
	@InjectView(R.id.userName)
	private EditText userNameEditor;
	@InjectView(R.id.password)
	private EditText passwordEditor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		WorkerContext.init(this);
		new InjectViewHelper().injectViews(this);
		passwordEditor.setOnEditorActionListener(this);
		findViewById(R.id.loginButton).setOnClickListener(this);
		findViewById(R.id.cancelButton).setOnClickListener(this);
		String phoneNumber = DeviceUtils.getPhoneNumber(this);
		userNameEditor.setText(phoneNumber);
	}

	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		if (v.getId() == R.id.password) {
			doLogin();
		}
		return false;
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.loginButton) {
			doLogin();
		} else if (v.getId() == R.id.cancelButton) {
			finish();
		}
	}

	private void doLogin() {
		String userName = userNameEditor.getText().toString();
		String password = passwordEditor.getText().toString();
		boolean invalid = false;
		if (StringUtils.isBlank(userName)) {
			invalid = true;
			userNameEditor.setError("手机号不能为空！");
		}
		if (StringUtils.isBlank(password)) {
			invalid = true;
			passwordEditor.setError("密码不能为空！");
		}
		if (invalid) {
			return;
		}
		new LoginAsyncTask(userName, password).execute();
	}

	private void reset() {
		passwordEditor.setText(StringUtils.EMPTY);
		userNameEditor.setText(StringUtils.EMPTY);
	}

	class LoginAsyncTask extends AsyncTask<Object, Object, Token> {
		String userName;
		String password;

		public LoginAsyncTask(String userName, String password) {
			super();
			this.userName = userName;
			this.password = password;
		}

		@Override
		protected void onPostExecute(Token result) {
			Context context = LoginActivity.this;
			if (result == null || StringUtils.isBlank(result.getTokenString())) {
				Toast.makeText(context, "登录失败，手机号或者密码错误！", Toast.LENGTH_LONG)
						.show();
				reset();
				return;
			}
			Toast.makeText(context, "登录成功！", Toast.LENGTH_SHORT).show();
			WorkerContext.setLoginToken(result);
			startActivity(new Intent(context, MainActivity.class));
			DeviceUtils.setPhoneNumber(context, userName);
			finish();
		}

		@Override
		protected Token doInBackground(Object... params) {
			return WorkerContext.getWorkerService().login(userName, password);
		}

	}

}
