-- Criar o banco de dados 'gatos'
CREATE DATABASE IF NOT EXISTS gatos;

-- Selecionar o banco de dados 'gatos'
USE gatos;

-- Criar a tabela 'gatos'
CREATE TABLE IF NOT EXISTS gatos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(255),
    idade INT,
    castrado BOOLEAN,
    sexo VARCHAR(10)
);