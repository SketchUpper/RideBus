package org.xtimms.trackbus.presenter;

import android.os.AsyncTask;

import org.xtimms.trackbus.model.ModelFactory;
import org.xtimms.trackbus.model.Route;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class MinibusFragmentPresenter {
    private final View mView;

    public MinibusFragmentPresenter(View view) {
        mView = view;
    }

    public void setAdapter() {
        new GetTramsAsyncTask(mView).execute();
    }

    public interface View {
        void setAdapter(ArrayList<Route> routeList);
    }

    public static class GetTramsAsyncTask extends AsyncTask<Void, Void, Void> {
        private final WeakReference<View> mFragmentWeakReference;
        private List<Route> mRouteList;
        private Exception mException;

        private GetTramsAsyncTask(View context) {
            super();
            mFragmentWeakReference = new WeakReference<>(context);
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                mRouteList = ModelFactory.getModel().getAllRoutesMinibus();
            } catch (Exception e) {
                mException = e;
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            super.onPostExecute(v);
            if (mException == null) {
                mFragmentWeakReference.get().setAdapter((ArrayList<Route>) mRouteList);
            } else {
                return;
            }
        }
    }
}
