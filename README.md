# AgroGestão Web - Sistema Integrado de Modernização Agropecuária

Projeto Integrador Full Stack individual de modernização de sistema legado em conformidade com as diretrizes e critérios acadêmicos do curso de Engenharia de Computação / Programação Web (2º Semestre de 2026).

---

## 1. Visão Geral da Solução e Justificativas de Evolução

O projeto evoluiu um esquema de banco de dados legado desprovido de integridade e regras de negócio para uma aplicação corporativa robusta desenvolvida em **Java 21**, **Spring Boot 4**, **Spring Data JPA**, **Spring Security** e **Thymeleaf**.

### Principais Lacunas Identificadas no Legado e Soluções Implementadas:
1. **Ausência de Integridade e Máquina de Estados:**
   - *Legado:* Situações de safra eram armazenadas em texto livre, permitindo inconsistências (ex: safras concluídas sem data de colheita ou safras canceladas reabertas).
   - *Solução:* Criação do enum `StatusSafra` com máquina de estados finita e validação estrita no serviço (`SafraService`), impedindo transições ilógicas.
2. **Insegurança de Acesso e Falta de Autorização no Servidor:**
   - *Legado:* Não havia autenticação ou qualquer barreira de perfil.
   - *Solução:* Integração do **Spring Security** com hashing de senhas via **BCrypt**, dois perfis de acesso (`ROLE_ADMIN` e `ROLE_OPERADOR`) e proteção de operações de exclusão no servidor.
3. **Interface e Apresentação de Dados:**
   - *Legado / Protótipo:* Tabelas com dados mockados em JavaScript que se perdiam ao recarregar a página.
   - *Solução:* Telas dinâmicas em **Thymeleaf** com **Bootstrap Grid** e CSS customizado, com CRUDs completos integrados ao banco de dados relacional H2.

---

## 2. Tecnologias Utilizadas

- **Linguagem:** Java 21 (LTS)
- **Framework:** Spring Boot (Spring MVC, Spring Data JPA, Spring Security, Validation)
- **Persistência / Banco de Dados:** JPA / Hibernate com H2 Database persistido em arquivo local (`./database/agronegocio`)
- **Renderização Web:** Thymeleaf 3 com Thymeleaf Extras Spring Security 6
- **Frontend:** HTML5, CSS3, Bootstrap 5 (Grid Utilities), FontAwesome 6
- **Testes Automatizados:** JUnit 5, Mockito

---

## 3. Instruções de Instalação, Configuração e Execução

### Pré-requisitos
- **Java JDK 21** instalado e configurado nas variáveis de ambiente (`JAVA_HOME`).
- Maven ou uso do wrapper embutido (`mvnw.cmd` no Windows / `./mvnw` no Linux/Mac).
- IDE recomendada: IntelliJ IDEA ou VS Code.

### Passos para Execução

1. **Clonar ou abrir o projeto:**
   Abra a pasta do projeto no terminal ou no IntelliJ IDEA.

2. **Executar via Maven (Linha de Comando):**
   ```bash
   # No Windows (PowerShell / CMD)
   .\mvnw.cmd spring-boot:run
   ```

3. **Executar via IntelliJ IDEA:**
   - Abra a classe principal: `src/main/java/br/com/agroehtop/top/TopApplication.java`
   - Clique com o botão direito e selecione **Run 'TopApplication'** (ou atalho `Shift + F10`).

4. **Acessar a Aplicação:**
   - **Sistema Web:** [http://localhost:8082](http://localhost:8082)
   - **Console H2 (Banco de Dados):** [http://localhost:8082/h2-console](http://localhost:8082/h2-console)
     - *JDBC URL:* `jdbc:h2:file:./database/agronegocio`
     - *Usuário:* `sa`
     - *Senha:* *(em branco)*

---

## 4. Credenciais de Acesso (Dois Perfis Obrigatórios)

O sistema possui carga inicial automática via `CargaInicialDadosConfig.java` com as seguintes contas cadastradas:

| Perfil | Usuário (Login) | Senha | Permissões no Sistema |
| :--- | :--- | :--- | :--- |
| **Administrador** (`ROLE_ADMIN`) | `admin` | `admin123` | Acesso total irrestrito: visualização, cadastro, edição e **exclusão** em todos os módulos. |
| **Operador** (`ROLE_OPERADOR`) | `operador` | `operador123` | Acesso operacional: visualiza dashboards, relatórios e realiza cadastros/edições. **Bloqueado de excluir registros** (tanto na UI quanto por barreira no servidor). |

---

## 5. Módulos e Entidades do Sistema

1. **Gestão de Safras (`/safras`):** Entidade central. Controles de área plantada, produtividade, custo, data de plantio/colheita e transições de status (`PLANEJADA`, `EM_ANDAMENTO`, `CONCLUIDA`, `CANCELADA`).
2. **Fazendas & Terras (`/fazendas`):** Cadastro e monitoramento territorial de propriedades rurais.
3. **Maquinário Agrícola (`/maquinas`):** Monitoramento de frotas de tratores e colheitadeiras vinculadas a fazendas, com medição de horímetro.
4. **Vendas Comerciais (`/vendas`):** Contratos de comercialização vinculando Safras a Clientes, com cálculo automático de faturamento.

---

## 6. Testes Automatizados e Evidências

Os testes automatizados cobrem as regras de negócio centrais e a integridade de domínio:
- `SafraServiceTest`: Testa salvamento de safras válidas, rejeição de áreas negativas/zeradas, validação temporal (colheita < plantio) e transições permitidas vs bloqueadas de status.
- `VendaTest`: Testa o cálculo financeiro do valor total com arredondamento e tratamento de valores nulos.

Para rodar todos os testes automatizados via terminal:
```bash
.\mvnw.cmd test
```
Ou no IntelliJ: clique com o botão direito na pasta `src/test/java` e selecione **Run 'All Tests'**.

---
estrangeira `@JoinColumn(name = "fazenda_id")`), enquanto `Fazenda` possui o lado inverso (`mappedBy = "fazenda"` com `orphanRemoval = true`).
- **Demonstrar segurança entre perfis:** Efetue login como `operador` e mostre que os botões de exclusão desaparecem e requisições diretas de exclusão são barradas pelo servidor; depois faça login como `admin` e demonstre a exclusão funcional.
