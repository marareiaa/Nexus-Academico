# 🎓 Nexus Acadêmico

### Sistema de Gestão de Eventos e Ligas Acadêmicas

> Plataforma voltada à gestão e organização de eventos, workshops e
> consultorias juniores, com foco no **ODS 8 – Trabalho Decente e
> Crescimento Econômico**.

---

## 📌 Sobre o Projeto

O **Nexus Acadêmico** foi desenvolvido com o objetivo de centralizar e
organizar o gerenciamento de eventos e ligas acadêmicas, facilitando o
controle de participantes, inscrições, presenças e emissão de
certificados.

A arquitetura do sistema foi planejada para apoiar a organização de
eventos, workshops e consultorias juniores, proporcionando maior
organização, rastreabilidade e eficiência aos processos acadêmicos.

---

## 🎯 Objetivo

O sistema busca solucionar dificuldades relacionadas à organização de
eventos acadêmicos, especialmente no controle de inscrições,
participantes, presença e certificação.

O projeto também está alinhado ao **ODS 8 – Trabalho Decente e
Crescimento Econômico**, contribuindo para a qualificação profissional
dos estudantes e para a profissionalização da gestão de atividades
acadêmicas.

---

## 👥 Participantes

| Participante |
|---|
| **Igor Henrique Tentemplis Martins** |
| **Jhonny Braga Reis** |
| **Maria Clara Lima da Silva** |

---

## 📅 Cronograma de Desenvolvimento — 1° BIMESTRE

O cronograma apresenta as principais etapas do desenvolvimento do
**Nexus Acadêmico**, organizando as atividades, responsabilidades e
prazos definidos pela equipe. Seu objetivo é auxiliar no acompanhamento
e no cumprimento das etapas previstas para o projeto.

| 📅 Dia | 📌 Tarefa | 👤 Realizador |
|---|---|---|
| **07/08/2026** | Reunião de alinhamento inicial sobre o Nexus Acadêmico | Maria Clara Lima da Silva, Jhonny Braga Reis e Igor Henrique Tentemplis Martins |
| **24/08/2026** | Definição da Concepção e Alinhamento com a ODS 8; Levantamento de Requisitos Funcionais | Jhonny Braga Reis |
| **24/08/2026** | Descoberta – levantamento dos principais requisitos, problemas, dores e necessidades das partes interessadas | Maria Clara Lima da Silva |
| **25/08/2026** | Organização do GitHub – estruturação e organização inicial do repositório do projeto | Maria Clara Lima da Silva |
| **25/08/2026** | Listagem e Definição dos Requisitos | Jhonny Braga Reis |
| **03/09/2026** | Início da modelagem de especialização e generalização no DER | Jhonny Braga Reis |
| **03/09/2026** | Criação do Diagrama de Classe e descrição dos elementos | Maria Clara Lima da Silva |
| **03/09/2026** | Definição e Criação das Justificativas Técnicas e Arquiteturais do sistema | Igor Henrique Tentemplis Martins |
| **04/09/2026** | Finalização do diagrama ER (DER) | Jhonny Braga Reis |
| **05/09/2026** | Revisão com o professor Aparecido Vilela Junior para feedback sobre melhorias; início dos ajustes indicados | Jhonny Braga Reis |
| **08/09/2026** | Finalização dos ajustes e reenvio do material ao professor para nova revisão | Jhonny Braga Reis |
| **09/09/2026** | Revisão e correção dos pontos apontados pelo professor; modelo validado e aprovado; revisão e formatação do documento da AEP | Jhonny Braga Reis |

---

## 🧩 Diagramas do Sistema

A modelagem do **Nexus Acadêmico** é composta pelos diagramas
responsáveis por representar a estrutura do sistema e a organização
dos seus dados.

### 📐 Diagrama de Classes

O diagrama apresenta a estrutura estática do sistema, contemplando suas
principais classes, atributos, métodos e relacionamentos.

**📄 [Visualizar o Diagrama de Classes em PDF](docs/diagrama-de-classe.pdf)**

---

### 🗄️ Diagrama de Entidade-Relacionamento (DER)

O diagrama apresenta a estrutura do banco de dados e os principais
elementos que compõem a aplicação, incluindo entidades, atributos e
relacionamentos entre usuários, eventos, inscrições, presenças e
certificados.

**📄 [Visualizar o DER em PDF](docs/diagrama-entidade-relacionamento.pdf)**

---

## 💻 Arquitetura do Sistema

O sistema utiliza uma organização baseada no padrão **MVC
(Model-View-Controller)**, complementada pela camada **DAO** para
acesso aos dados.

### 🔄 Fluxo Arquitetural

```text
View
  ↓
Controller
  ↓
Model / Serviço
  ↓
DAO
  ↓
JDBC
  ↓
MySQL
