package br.com.futureday.reserveandplay.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import br.com.futureday.reserveandplay.fragment.AmigosFragment;
import br.com.futureday.reserveandplay.fragment.MesasFragment;

/**
 * Created by FJ on 16/02/2018.
 */

public class TabAdapter extends FragmentPagerAdapter {

    private String[] titulosAbas = {"Mesas","Amigos"};

    public TabAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;

        switch (position){
            case 0:
                fragment = new MesasFragment();
                break;
            case 1:
                fragment = new AmigosFragment();
                break;
        }

        return fragment;
    }

    @Override
    public int getCount() {
        return titulosAbas.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titulosAbas[position];
    }
}
