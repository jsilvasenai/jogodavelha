package br.senai.sp.cotia.jogodavelha.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.snackbar.Snackbar;

import java.util.Arrays;
import java.util.Random;

import br.senai.sp.cotia.jogodavelha.R;
import br.senai.sp.cotia.jogodavelha.databinding.FragmentJogoBinding;
import br.senai.sp.cotia.jogodavelha.util.PrefsUtil;

public class FragmentJogo extends Fragment {

    private FragmentJogoBinding binding;
    private Random random;
    private String simbolo;
    private int placarJog1 = 0, placarJog2 = 0, numJogadas;
    private String[][] tabuleiro;
    private Button[] botoes;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // instancia o random
        random = new Random();
        setHasOptionsMenu(true);

        ((AppCompatActivity) getActivity()).getSupportActionBar().show();

    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentJogoBinding.inflate(inflater, container, false);

        // decide quem inicia o jogo
        sorteia();

        // inicializa o array de botoes
        botoes = new Button[9];

        // associa o listener aos botões
        binding.bt00.setOnClickListener(listenerBotoes);
        botoes[0] = binding.bt00;
        binding.bt01.setOnClickListener(listenerBotoes);
        botoes[1] = binding.bt01;
        binding.bt02.setOnClickListener(listenerBotoes);
        botoes[2] = binding.bt02;
        binding.bt10.setOnClickListener(listenerBotoes);
        botoes[3] = binding.bt10;
        binding.bt11.setOnClickListener(listenerBotoes);
        botoes[4] = binding.bt11;
        binding.bt12.setOnClickListener(listenerBotoes);
        botoes[5] = binding.bt12;
        binding.bt20.setOnClickListener(listenerBotoes);
        botoes[6] = binding.bt20;
        binding.bt21.setOnClickListener(listenerBotoes);
        botoes[7] = binding.bt21;
        binding.bt22.setOnClickListener(listenerBotoes);
        botoes[8] = binding.bt22;

        // inicializa o tabuleiro
        tabuleiro = new String[3][3];

        // preenche o tabuleiro com ""
        zerarTabuleiro();


        atualizaVez(simbolo);
        atualizaPlacar();

        return binding.getRoot();

    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        zerarTabuleiro();
    }

    private void sorteia() {
        if (random.nextBoolean()) {
            simbolo = PrefsUtil.getSimboloJog1(getContext());
        } else {
            simbolo = PrefsUtil.getSimboloJog2(getContext());
        }
    }

    private View.OnClickListener listenerBotoes = e -> {
        // incementa as jogadas
        numJogadas++;
        // obtém o botão clicado através de um casting de e
        Button botaoClicado = (Button) e;
        // descobre o nome do botão clicado
        String nomeDoBotao = getContext().getResources().getResourceName(e.getId());
        // extrai do nomeDoBotao a posição
        String posicao = nomeDoBotao.substring(nomeDoBotao.length() - 2, nomeDoBotao.length());
        // pega linha e coluna da posição
        int linha = Character.getNumericValue(posicao.charAt(0));
        int coluna = Character.getNumericValue(posicao.charAt(1));
        // adiciona o simbolo ao tabuleiro
        tabuleiro[linha][coluna] = simbolo;
        // muda o texto do botão clicado e desabilita ele
        botaoClicado.setText(simbolo);
        botaoClicado.setClickable(false);
        botaoClicado.setBackgroundColor(Color.WHITE);
        // verifica se venceu
        if (venceu(simbolo)) {
            Snackbar.make(getView(), (simbolo.equals("X") ? "Jogador 1" : "Jogador 2") + " venceu!!!", Snackbar.LENGTH_SHORT).show();
            pontua(simbolo);
            zerarTabuleiro();
            sorteia();
        } else {
            // verifica se deu velha
            if (numJogadas == 9) {
                Snackbar.make(getView(), "Deu Velha!!!", Snackbar.LENGTH_SHORT).show();
                zerarTabuleiro();
                sorteia();
            }


            // inverte o simbolo
            simbolo = simbolo == PrefsUtil.getSimboloJog1(getContext()) ? PrefsUtil.getSimboloJog2(getContext()) : PrefsUtil.getSimboloJog1(getContext());
            // atualiza vez
            atualizaVez(simbolo);

        }
    };

    private void pontua(String simbolo) {
        if (simbolo.equals("X")) {
            placarJog1++;
        } else {
            placarJog2++;
        }
        atualizaPlacar();
    }

    private void zerarTabuleiro() {
        for (String[] vetor : tabuleiro) {
            Arrays.fill(vetor, "");
        }
        for (Button b : botoes) {
            b.setText("");
            b.setBackgroundColor(getResources().getColor(R.color.primary));
            b.setClickable(true);
        }
        numJogadas = 0;
    }

    private void atualizaPlacar() {
        binding.tvPlacarJog1.setText(getString(R.string.placar_jog1, PrefsUtil.getSimboloJog1(getContext()), placarJog1));
        binding.tvPlacarJog2.setText(getString(R.string.placar_jog2, PrefsUtil.getSimboloJog2(getContext()), placarJog2));
    }

    private boolean venceu(String simbolo) {
        // verifica se venceu na linha
        for (int i = 0; i < 3; i++) {
            if (tabuleiro[i][0].equals(simbolo) && tabuleiro[i][1].equals(simbolo) && tabuleiro[i][2].equals(simbolo)) {
                return true;
            }
        }
        // verifica se venceu na coluna
        for (int i = 0; i < 3; i++) {
            if (tabuleiro[0][i].equals(simbolo) && tabuleiro[1][i].equals(simbolo) && tabuleiro[2][i].equals(simbolo)) {
                return true;
            }
        }
        // verifica se venceu na diagonal
        if (tabuleiro[0][0].equals(simbolo) && tabuleiro[1][1].equals(simbolo) && tabuleiro[2][2].equals(simbolo)) {
            return true;
        }
        if (tabuleiro[0][2].equals(simbolo) && tabuleiro[1][1].equals(simbolo) && tabuleiro[2][0].equals(simbolo)) {
            return true;
        }
        return false;
    }

    private void atualizaVez(String simbolo) {
        if (simbolo.equals("X")) {
            binding.tvPlacarJog1.setTextColor(Color.RED);
            binding.tvPlacarJog2.setTextColor(Color.BLACK);
        } else {
            binding.tvPlacarJog1.setTextColor(Color.BLACK);
            binding.tvPlacarJog2.setTextColor(Color.RED);
        }

    }


    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Log.w("clicou", "clicou aqui");
        switch (item.getItemId()) {
            case R.id.action_preferencias:
                NavHostFragment.findNavController(FragmentJogo.this)
                        .navigate(R.id.action_FragmentJogo_to_FragmentPref);
                break;
            case R.id.action_reiniciar:
                NavHostFragment.findNavController(FragmentJogo.this)
                        .navigate(R.id.action_FragmentJogo_to_FragmentInicio);
                break;
        }
        return true;
    }
}