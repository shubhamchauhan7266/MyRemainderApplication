package com.myremainderapplication.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.myremainderapplication.R

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [NotifactionFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [NotifactionFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NotifactionFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notifaction, container, false)
    }
}
