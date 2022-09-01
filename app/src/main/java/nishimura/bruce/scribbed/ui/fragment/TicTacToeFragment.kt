package nishimura.bruce.scribbed.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.launch
import nishimura.bruce.scribbed.R
import nishimura.bruce.scribbed.databinding.FragmentTicTacToeBinding
import nishimura.bruce.scribbed.model.TicTacToeBoard
import nishimura.bruce.scribbed.model.TicTacToeGameStatus
import nishimura.bruce.scribbed.model.XOState
import nishimura.bruce.scribbed.viewmodel.TicTacToeViewModel


class TicTacToeFragment : Fragment() {

    private val viewModel by viewModels<TicTacToeViewModel>()
    private var _binding: FragmentTicTacToeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTicTacToeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupClickListeners()
        handleStateChanges()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupClickListeners() {
        for (v in binding.ticTacToeGrid.children) {
            val y: Int = binding.ticTacToeGrid.indexOfChild(v) / binding.ticTacToeGrid.rowCount
            val x: Int = binding.ticTacToeGrid.indexOfChild(v) % binding.ticTacToeGrid.columnCount
            v.setOnClickListener { viewModel.clickTile(x, y) }
        }
    }

    private fun handleStateChanges() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.gameState.collect { ticTacToeState ->
                    updateBoardUI(ticTacToeState.board)
                    when (ticTacToeState.gameStatus) {
                        TicTacToeGameStatus.IN_PROGRESS -> null
                        TicTacToeGameStatus.X_WIN -> getString(R.string.tic_tac_toe_win, "X")
                        TicTacToeGameStatus.O_WIN -> getString(R.string.tic_tac_toe_win, "O")
                        TicTacToeGameStatus.DRAW -> getString(R.string.tic_tac_toe_draw)
                    }?.let {
                        showResetDialogWithMessage(it, requireContext())
                    }
                    binding.ticTacToeTurnText.text =
                        getString(
                            R.string.tic_tac_toe_turn_label,
                            ticTacToeState.playerTurn.toString()
                        )
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.error.collect {
                    showResetDialogWithMessage(it.message.toString(), requireContext())
                }
            }
        }
    }

    private fun updateBoardUI(board: TicTacToeBoard) {
        for (v in binding.ticTacToeGrid.children) {
            val y: Int = binding.ticTacToeGrid.indexOfChild(v) / binding.ticTacToeGrid.rowCount
            val x: Int = binding.ticTacToeGrid.indexOfChild(v) % binding.ticTacToeGrid.columnCount
            when (board.getAt(x, y)) {
                XOState.X -> {
                    (v as? TextView)?.text = "X"
                    (v as? TextView)?.setTextColor(binding.root.context.getColor(android.R.color.holo_red_dark))
                    v.isEnabled = false
                }
                XOState.O -> {
                    (v as? TextView)?.text = "O"
                    (v as? TextView)?.setTextColor(binding.root.context.getColor(android.R.color.holo_blue_bright))
                    v.isEnabled = false
                }
                XOState.BLANK -> {
                    (v as? TextView)?.text = ""
                    v.isEnabled = true
                }
            }
        }
    }

    private fun showResetDialogWithMessage(text: String, context: Context) {
        AlertDialog.Builder(context).apply {
            setMessage(text)
            setPositiveButton(R.string.tic_tac_toe_reset) { dialog, _ ->
                dialog.cancel()
                viewModel.clear()
            }
        }.create().show()
    }
}