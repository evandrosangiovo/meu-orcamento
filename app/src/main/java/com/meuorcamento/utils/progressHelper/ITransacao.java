package com.meuorcamento.utils.progressHelper;

public interface ITransacao {

    /**
     * Executar a transação em uma thread separada <br>
     *
     * @throws Exception
     */
    void executeTask() throws Exception;

    /**
     * Atualizar a view sincronizado com a thread principal - Após execução da
     * thread <br>
     */
    void executeOnSuccess();

    /**
     * Método é executado antes de iniciar o procedimento na outra thread <br>
     * Utilizar para atualizar a View principal <br>
     */
    //public void atualizarViewInitProcess();

    void executeOnError(Throwable exception);
}
