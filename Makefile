.DEFAULT_GOAL := help

.PHONY: help setup up down restart build logs ps test clean db-shell

help: ## Lista os comandos disponíveis
	@awk 'BEGIN {FS = ":.*## "; printf "Comandos disponíveis:\n"} /^[a-zA-Z_-]+:.*?## / {printf "  %-12s %s\n", $$1, $$2}' $(MAKEFILE_LIST)

setup: ## Cria o .env local a partir do exemplo
	@test -f .env || cp .env.example .env

up: setup ## Constrói e inicia a aplicação e o banco
	docker compose up --build -d

down: ## Encerra os containers
	docker compose down

restart: down up ## Reinicia todos os containers

build: ## Constrói as imagens Docker
	docker compose build

logs: ## Acompanha os logs dos containers
	docker compose logs -f

ps: ## Exibe o estado dos containers
	docker compose ps

test: setup ## Inicia o banco e executa os testes com Maven
	docker compose up -d --wait database
	./mvnw test

clean: ## Encerra os containers e remove os artefatos locais
	docker compose down
	./mvnw clean

db-shell: ## Abre o terminal do PostgreSQL
	docker compose exec database sh -c 'psql -U "$$POSTGRES_USER" -d "$$POSTGRES_DB"'
