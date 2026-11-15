package com.petshop.factory;

import com.petshop.command.Comando;

public interface ComandoFactory {
    Comando criar();
}