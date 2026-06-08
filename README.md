# Projeto: Controle de Concorrência em API Bancária

Este projeto foi desenvolvido para fins acadêmicos com o objetivo de demonstrar o problema de **Atualização Perdida (Lost Update)** em sistemas de alta concorrência e como resolvê-lo utilizando **Bloqueio Otimista (@Version)** com Spring Boot e Hibernate.

---

## 👥 Aluno:

* **Gabriel Prado Brandão:** Responsável pelo desenvolvimento da API REST (Controllers, Services, tratamento global de exceções),
* configuração estrutural do Bloqueio Otimista no Banco de Dados
* e pela configuração dos cenários de testes concorrentes no Apache JMeter, execução dos testes de estresse e coleta das métricas de erro.
* 
---

## Como Rodar a Aplicação

### Pré-requisitos
* Java 21
* Eclipse IDE (com suporte a Maven)
* H2 DataBase`application.properties`
* Apache JMeter instalado

### Passos para Execução

1. **Clonar o projeto:** Baixe o repositório para a sua máquina local.
2. **Importar na IDE:** Abra o Eclipse e importe o projeto seguindo o caminho: *File -> Import -> Existing Maven Projects*.
3. **Configurar o Banco de Dados:** Certifique-se de que as credenciais do seu banco local estejam corretas no arquivo `src/main/resources/application.properties`.
4. **Subir a Aplicação:** Execute a classe `ContaBancariaConcorrenciaApplication.java` como *Java Application*. O servidor Tomcat inicializará automaticamente na porta `8080`.
5. **Inicializar os Dados de Teste:** Como a estratégia do banco recria as tabelas limpas ao subir, é necessário povoar os registros iniciais. Abra o seu terminal e execute os comandos de acordo com o seu console:

   * **No Prompt de Comando (CMD):**
     ```bash
     curl -X POST http://localhost:8080/contas/inicializar
     curl -X POST http://localhost:8080/contas-versionadas/inicializar
     ```

   * **No PowerShell:**
     ```powershell
     Invoke-WebRequest -Uri http://localhost:8080/contas/inicializar -Method POST
     Invoke-WebRequest -Uri http://localhost:8080/contas-versionadas/inicializar -Method POST
     ```

6. **Rodar os Testes no JMeter:** * Abra o Apache JMeter.
   * Importe os arquivos `Summary_Report.jmx` que estão localizados na raiz deste repositório.
   * Limpe o histórico de execuções anteriores(se houver) clicando no botão da **vassourinha dupla** (Clear All).
   * Clique no botão **Play Verde** para iniciar o teste de estresse concorrente (20 usuários simultâneos realizando loops de depósitos e saques de R$ 100,00).

---

## 📊 Relatório de Conclusão e Análise Comparativa

### Parte 1: Conta Sem Controle de Concorrência 
Utilizando o arquivo de teste cenario1-sem-controle.jmx. No primeiro cenário, o endpoint de teste processou as 400 requisições simultâneas sem nenhuma trava de segurança ou isolamento lógico na aplicação.

* **Comportamento no JMeter:** O relatório apresentou **0% de erro**, indicando que a API aceitou todas as requisições de forma bem-sucedida na camada HTTP.
* **Comportamento no Banco de Dados:** O saldo final da conta no DBeaver apresentou inconsistências graves (divergindo do valor esperado de R$ 1000,00 originais após o balanço simétrico de depósitos e saques).
* **Análise Técnica:** Ocorreu o fenômeno clássico de **Lost Update (Atualização Perdida)**. Como múltiplas threads leram o saldo original simultaneamente no banco, a gravação final de uma thread acabou sobrescrevendo a gravação da outra thread anterior de forma silenciosa, corrompendo a integridade financeira do registro.

<img width="1531" height="878" alt="summary-report-conta-bancaria-sem-controle" src="https://github.com/user-attachments/assets/90066554-abae-4de1-b80e-872784a96f65" />
<img width="1247" height="445" alt="query-before-alter-sem-controle" src="https://github.com/user-attachments/assets/55ff93ae-ac75-43de-9a09-01de316d450f" />
<img width="1241" height="443" alt="query-after-alter-sem-controle" src="https://github.com/user-attachments/assets/15b052e8-4a8a-4165-84cb-c897f3ca49e6" />


### Parte 2: Conta Com Controle Otimista (`@Version`)
No segundo cenário, o atributo `version` foi mapeado na entidade e anotado com `@Version` do JPA/Hibernate. Um manipulador global de exceções foi acoplado para interceptar falhas de concorrência.

* **Comportamento no JMeter:** O relatório do JMeter registrou de forma controlada uma média de **69% de erro** nas requisições
* **Comportamento no Banco de Dados:** O saldo final da conta no DBeaver manteve-se **100% íntegro e protegido**. O saldo refletiu estritamente o balanço das transações sequenciais que conseguiram obter sucesso na concorrência.
* **Análise Técnica:** O Bloqueio Otimista atuou com precisão. Quando threads concorrentes tentaram atualizar a mesma conta ao mesmo tempo, a primeira transação a chegar incrementou o número da versão do registro no banco para o nível seguinte. As threads subsequentes, que ainda carregavam o número de versão obsoleto em memória, falharam ao tentar executar a cláusula condicional de persistência:
  `[update conta_bancaria_versionada set saldo=?, titular=?, version=? where id=? and version=?]`
   Como o banco reportou `0` linhas alteradas, o Hibernate disparou imediatamente a exceção raiz **`org.hibernate.StaleStateException`** (capturada pela camada do Spring como `ObjectOptimisticLockingFailureException`), executando o rollback de segurança da operação antes que ela pudesse sobrescrever dados legítimos.

<img width="1531" height="871" alt="summary-report-conta-bancaria-versionada" src="https://github.com/user-attachments/assets/a4a270f4-216c-45ad-b29f-2070fd769ffd" />


---
