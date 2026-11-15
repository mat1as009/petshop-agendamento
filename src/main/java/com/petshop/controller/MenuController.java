package com.petshop.controller;

import com.petshop.factory.ComandoFactory;
import com.petshop.view.MenuView;
import java.util.Map;

public class MenuController {
    private final MenuView view;
    private final Map<String, ComandoFactory> fabricas;

    public MenuController(MenuView view, Map<String, ComandoFactory> fabricas) {
        this.view = view;
        this.fabricas = fabricas;
    }

    public void iniciar() {
        boolean rodando = true;
        while (rodando) {
            view.exibirMenu();
            String opcao = view.lerOpcao();

            if (opcao.equals("0")) {
                rodando = false;
            } else {
                ComandoFactory fabrica = fabricas.get(opcao);
                if (fabrica == null) {
                    view.mostrarMensagem("Opção inválida.");
                } else {
                    fabrica.criar().executar();
                }
            }
        }
        view.mostrarMensagem("Até logo!");
    }
}