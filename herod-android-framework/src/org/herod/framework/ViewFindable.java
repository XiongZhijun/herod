/*
 * 香港摩比科技有限公司 版权所有
 *
 * www.mobi-inf.com
 */
package org.herod.framework;

import android.app.Activity;
import android.app.Dialog;
import android.support.v4.app.Fragment;
import android.view.View;

public interface ViewFindable {

	View findViewById(int id);

	public static class ActivityWrapper implements ViewFindable {
		private Activity activity;

		public ActivityWrapper(Activity activity) {
			super();
			this.activity = activity;
		}

		public View findViewById(int id) {
			return activity.findViewById(id);
		}
	}

	public static class DialogWrapper implements ViewFindable {
		private Dialog dialog;

		public DialogWrapper(Dialog dialog) {
			super();
			this.dialog = dialog;
		}

		public View findViewById(int id) {
			return dialog.findViewById(id);
		}
	}

	public static class FragmentWrapper implements ViewFindable {
		private Fragment fragment;

		public FragmentWrapper(Fragment fragment) {
			super();
			this.fragment = fragment;
		}

		public View findViewById(int id) {
			return fragment.getView().findViewById(id);
		}
	}

	public static class ViewWrapper implements ViewFindable {
		private View view;

		public ViewWrapper(View view) {
			super();
			this.view = view;
		}

		public View findViewById(int id) {
			return view.findViewById(id);
		}
	}
}
