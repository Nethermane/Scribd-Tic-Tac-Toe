package nishimura.bruce.scribbed.ui.activity

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import nishimura.bruce.scribbed.R

class TicTacToeActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tic_tac_toe)
    }
}