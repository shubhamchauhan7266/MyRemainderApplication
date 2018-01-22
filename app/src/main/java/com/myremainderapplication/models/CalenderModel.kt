package com.myremainderapplication.models

import java.io.Serializable

/**
 * <h1><font color="orange">CalenderModel</font></h1>
 * this is a model class for Calender
 *
 * @author Shubham Chauhan
 */
class CalenderModel(val year: Int, val month: Int, val day: Int, val hour: Int, val minute: Int) : Serializable